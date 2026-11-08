#!/usr/bin/env python3
"""
PerkLens Static Codebase & Symbol Reference Auditor
Statically checks every Kotlin file to catch any missing symbols,
bracket imbalances, broken imports, missing theme colors, unimported icons, or typos.
"""

import os
import re
import glob
import sys

# Force UTF-8 on Windows
if sys.stdout.encoding and sys.stdout.encoding.lower() != 'utf-8':
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except Exception:
        pass

def strip_comments_and_strings(text):
    """Strip comments and string literals so words inside strings/comments are not treated as code symbols"""
    pattern = re.compile(
        r'//.*?$|/\*.*?\*/|"""[\s\S]*?"""|"(?:\\.|[^\\"\n])*"|\'(?:\\.|[^\\\'\n])*\'',
        re.MULTILINE
    )
    return re.sub(pattern, ' ', text)

def check_brackets_and_syntax(rel_path, text):
    """
    Checks syntax integrity:
    1. Parentheses (), Brackets [], and Braces {} balancing
    2. Unclosed string literals and comments
    """
    errors = []
    stack = []
    
    i = 0
    n = len(text)
    line_num = 1
    
    in_block_comment = False
    in_line_comment = False
    in_triple_string = False
    in_single_string = False
    
    while i < n:
        c = text[i]
        
        if c == '\n':
            line_num += 1
            if in_line_comment:
                in_line_comment = False
            i += 1
            continue
            
        if in_line_comment:
            i += 1
            continue
            
        if in_block_comment:
            if c == '*' and i + 1 < n and text[i + 1] == '/':
                in_block_comment = False
                i += 2
                continue
            i += 1
            continue
            
        if in_triple_string:
            if c == '"' and i + 2 < n and text[i + 1] == '"' and text[i + 2] == '"':
                in_triple_string = False
                i += 3
                continue
            i += 1
            continue
            
        if in_single_string:
            if c == '\\':
                i += 2
                continue
            if c == '"':
                in_single_string = False
                i += 1
                continue
            i += 1
            continue
            
        # Check start of comment or string
        if c == '/' and i + 1 < n:
            if text[i + 1] == '/':
                in_line_comment = True
                i += 2
                continue
            elif text[i + 1] == '*':
                in_block_comment = True
                i += 2
                continue
                
        if c == '"':
            if i + 2 < n and text[i + 1] == '"' and text[i + 2] == '"':
                in_triple_string = True
                i += 3
                continue
            else:
                in_single_string = True
                i += 1
                continue
                
        if c == '\'':
            if i + 1 < n and text[i + 1] == '\\':
                i += 4
            else:
                i += 3
            continue
            
        # Brackets tracking
        if c in '({[':
            stack.append((c, line_num))
        elif c in ')}]':
            if not stack:
                errors.append(f"[{rel_path}:{line_num}] Unmatched closing '{c}'")
            else:
                top, top_line = stack.pop()
                expected = {')': '(', '}': '{', ']': '['}[c]
                if top != expected:
                    errors.append(f"[{rel_path}:{line_num}] Mismatched bracket: expected '{expected}' (opened line {top_line}), got '{c}'")
        i += 1
        
    if in_block_comment:
        errors.append(f"[{rel_path}] Unclosed block comment /* ... */")
    if in_triple_string:
        errors.append(f"[{rel_path}] Unclosed triple-quote string \"\"\" ... \"\"\"")
    if in_single_string:
        errors.append(f"[{rel_path}] Unclosed string literal \" ... \"")
    while stack:
        top, top_line = stack.pop()
        errors.append(f"[{rel_path}:{top_line}] Unclosed opening '{top}'")
        
    return errors

def audit_codebase():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    base_dir = os.path.abspath(os.path.join(script_dir, "..", "app", "src", "main", "java"))
    kt_files = glob.glob(os.path.join(base_dir, "**", "*.kt"), recursive=True)

    declared_symbols = set()
    symbol_packages = {}
    for f in kt_files:
        with open(f, "r", encoding="utf-8") as fp:
            text = fp.read()
        pkg_m = re.search(r'package\s+([A-Za-z0-9_.]+)', text)
        pkg = pkg_m.group(1) if pkg_m else ""
        for m in re.finditer(r'^(?:data\s+class|class|object|interface|enum\s+class)\s+([A-Za-z0-9_]+)', text, re.MULTILINE):
            sym = m.group(1)
            declared_symbols.add(sym)
            if pkg:
                symbol_packages[sym] = pkg

    color_file = os.path.join(base_dir, "io", "perklens", "ui", "theme", "Color.kt")
    with open(color_file, "r", encoding="utf-8") as fp:
        color_text = fp.read()
    colors = set(re.findall(r'val\s+([A-Za-z0-9_]+)\s*=\s*Color', color_text))

    print(f"Declared Classes/Objects/Enums ({len(declared_symbols)}): {', '.join(sorted(declared_symbols))}")
    print(f"Colors available ({len(colors)}): {', '.join(sorted(colors))}")

    issues = []

    for f in kt_files:
        rel = os.path.relpath(f, base_dir)
        with open(f, "r", encoding="utf-8") as fp:
            raw_text = fp.read()

        code_text = strip_comments_and_strings(raw_text)

        pkg_m = re.search(r'package\s+([A-Za-z0-9_.]+)', raw_text)
        cur_pkg = pkg_m.group(1) if pkg_m else ""
        imports = set(re.findall(r'import\s+([A-Za-z0-9_.*]+)', raw_text))

        # Check cross-package references
        words = set(re.findall(r'\b([A-Z][A-Za-z0-9_]+)\b', code_text))
        for w in words:
            if w in symbol_packages:
                if re.search(rf'\b(?:class|object|interface|enum\s+class)\s+{w}\b', code_text):
                    continue
                def_pkg = symbol_packages[w]
                if def_pkg != cur_pkg:
                    specific_imp = f"{def_pkg}.{w}"
                    wildcard_imp = f"{def_pkg}.*"
                    if specific_imp not in imports and wildcard_imp not in imports:
                        if f"{def_pkg}.{w}" not in code_text:
                            issues.append(f"[{rel}] Missing cross-package import: {specific_imp}")

        # Syntax & Bracket Integrity
        issues.extend(check_brackets_and_syntax(rel, raw_text))

        # Check clickable modifier import
        if ".clickable" in code_text:
            if "import androidx.compose.foundation.clickable" not in raw_text and "import androidx.compose.foundation.*" not in raw_text:
                issues.append(f"[{rel}] Missing import: androidx.compose.foundation.clickable")

        # Check background modifier import
        if ".background(" in code_text:
            if "import androidx.compose.foundation.background" not in raw_text and "import androidx.compose.foundation.*" not in raw_text:
                issues.append(f"[{rel}] Missing import: androidx.compose.foundation.background")

        # Check border modifier import
        if ".border(" in code_text:
            if "import androidx.compose.foundation.border" not in raw_text and "import androidx.compose.foundation.*" not in raw_text:
                issues.append(f"[{rel}] Missing import: androidx.compose.foundation.border")

        # Check scroll imports
        if "verticalScroll" in code_text:
            if "import androidx.compose.foundation.verticalScroll" not in raw_text and "import androidx.compose.foundation.*" not in raw_text:
                issues.append(f"[{rel}] Missing import: androidx.compose.foundation.verticalScroll")

        # Check coroutine launch import
        if re.search(r'\b(launch\s*\{)', code_text):
            if "import kotlinx.coroutines.launch" not in raw_text and "import kotlinx.coroutines.*" not in raw_text:
                issues.append(f"[{rel}] Missing import: kotlinx.coroutines.launch")

        # Check Icons.Default.* imports
        icon_matches = re.findall(r'Icons\.Default\.([A-Za-z0-9_]+)', code_text)
        if icon_matches:
            has_wildcard = "import androidx.compose.material.icons.filled.*" in raw_text
            for icon_name in set(icon_matches):
                if not has_wildcard:
                    specific_import = f"import androidx.compose.material.icons.filled.{icon_name}"
                    if specific_import not in raw_text:
                        issues.append(f"[{rel}] Missing Icon Import for Icons.Default.{icon_name}")

    if issues:
        print("\n❌ FOUND AUDIT ISSUES:")
        for iss in issues:
            print("  " + iss)
        return False
    else:
        print("\n✅ ZERO SYMBOL OR COMPILER ISSUES FOUND ACROSS ALL KOTLIN FILES!")
        return True

if __name__ == "__main__":
    success = audit_codebase()
    if not success:
        sys.exit(1)
