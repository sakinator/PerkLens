# 💳 PerkLens v1.0.0 — Your AI Lens on Every Card, Lounge & Swipe

### 🌟 Initial Production Release Highlights

PerkLens is a privacy-first, zero-KYC Native Android application built with Kotlin, Jetpack Compose, and Material 3, designed as a companion to **ThreatLens** to maximize card privileges, lounge access, and interest-free runway.

#### 1. 🎨 Cyber Neon Green & Obsidian Brand Identity
- Custom vector logo featuring a 10-lobe soft wave scalloped rosette badge and rounded pill `%` geometry.
- Premium OLED Dark (`#070A0F`) and Cyber Neon Green (`#00E676` / `#00FF87`) visual design system.

#### 2. 📸 AI Multi-Card Vision Scanner (ML Kit)
- Spread multiple credit/debit cards on a desk and capture them in a single frame.
- Automatically isolates card entities, matching catalog names while **strictly stripping sensitive 16-digit PANs and CVVs**.

#### 3. ✈️ Airport & Railway Lounge Pass Hub
- Comprehensive directory spanning Delhi (T1/T2/T3), Mumbai (T1/T2), Bengaluru (080 T1/T2), Hyderabad, Chennai, Goa, and Indian Railway Executive lounges.
- Real-time "You Can Enter ✓" qualification badge.
- Access criteria classification: Unconditional (₹2 swipe) vs Spend-Linked quarterly thresholds.

#### 4. 🤖 Intelligent Multi-Horizon Exhaustion Engine
- Parses local bank transaction SMS & statement notifications to track what has already been consumed across multiple horizons:
  - **Monthly:** BookMyShow / PVR INOX cinema BOGO quotas and Swiggy / Zomato cashback caps.
  - **Quarterly:** Airport lounge complimentary visit allowances.
  - **Annual:** Milestone spend trackers and annual fee waiver progress (₹1L / ₹2L / ₹3L).
- Multi-card fallback logic that dynamically re-routes transactions when caps are exhausted.

#### 5. 🗓️ Statement Day & 50-Day Interest-Free Runway Optimizer
- Configurable statement generation date (Day 1–31) per card.
- Live swipe optimizer calculating maximum interest-free runway (up to 50 days).

#### 6. 🔒 Zero-KYC & Local-Only Key Storage Architecture
- Zero card numbers, CVVs, or OTPs collected or stored.
- All AI API keys (Gemini, Groq, Claude, OpenAI) are strictly stored on-device in `Context.MODE_PRIVATE`.
- 100% on-device SMS regex parsing and OCR scanning.

#### 7. 🛡️ ThreatLens-Grade Static Auditor & 36-Test Regression Suite
- Bundled with `tools/codebase_auditor.py` and `tools/run_regression_suite.py`.
- **36/36 Regression Tests Passing** across 11 test suites.

#### 8. 🔑 Guaranteed In-Place Upgrades (30-Year Keystore)
- Signed with a permanent 30-year release keystore (`keystore/release.jks`, valid through **2056**) shared across both debug and release configurations.
- Guaranteed seamless updates over older versions without data loss or Android signature mismatch errors (`INSTALL_FAILED_UPDATE_INCOMPATIBLE`).

#### 9. ✨ 100% Vibe Coded with AI
- Built through symbiotic human-AI agentic collaboration, real-time feedback loops, automated regression test suites, and live self-healing pipelines. High velocity, zero bloat, pure vibes.

---

### 📦 Download Assets
- **`PerkLens.apk`** (12.4 MB) — Official production release signed with the permanent 30-year keystore.
- **`PerkLens-v1.0.0-debug.apk`** (18.3 MB) — Debug variant signed with the identical release key.
