package io.perklens.core.diagnostics

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DiagnosticLogEntry(
    val id: String,
    val timestamp: String,
    val level: String, // "ERROR", "WARNING", "INFO"
    val tag: String,   // "CATALOG_SYNC", "AI_ENGINE", "SMS_PARSER", "SYSTEM"
    val message: String,
    val technicalDetails: String? = null
)

class AppDiagnosticsLogManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_diagnostics_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val KEY_LOG_ENTRIES = "diagnostic_log_entries_json"

    private val _logs = MutableStateFlow<List<DiagnosticLogEntry>>(loadLogs())
    val logs: StateFlow<List<DiagnosticLogEntry>> = _logs.asStateFlow()

    private fun loadLogs(): List<DiagnosticLogEntry> {
        val json = prefs.getString(KEY_LOG_ENTRIES, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<DiagnosticLogEntry>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun persistLogs(list: List<DiagnosticLogEntry>) {
        _logs.value = list
        prefs.edit().putString(KEY_LOG_ENTRIES, gson.toJson(list)).apply()
    }

    @Synchronized
    fun log(level: String, tag: String, message: String, throwable: Throwable? = null) {
        val now = SimpleDateFormat("dd MMM yyyy, HH:mm:ss", Locale.getDefault()).format(Date())
        val details = throwable?.let {
            val stack = it.stackTraceToString()
            if (stack.length > 1000) stack.take(1000) + "\n...[truncated]" else stack
        }

        val entry = DiagnosticLogEntry(
            id = "log_${System.currentTimeMillis()}_${(100..999).random()}",
            timestamp = now,
            level = level,
            tag = tag,
            message = message,
            technicalDetails = details
        )

        // Keep last 150 entries
        val current = _logs.value.toMutableList()
        current.add(0, entry)
        if (current.size > 150) {
            persistLogs(current.take(150))
        } else {
            persistLogs(current)
        }
    }

    fun logError(tag: String, message: String, throwable: Throwable? = null) {
        log("ERROR", tag, message, throwable)
    }

    fun logWarning(tag: String, message: String) {
        log("WARNING", tag, message, null)
    }

    fun logInfo(tag: String, message: String) {
        log("INFO", tag, message, null)
    }

    fun clearLogs() {
        persistLogs(emptyList())
    }

    fun generateDiagnosticsReport(totalCardsInDb: Int = 162): String {
        val sb = StringBuilder()
        sb.append("# 🛡️ PerkLens Diagnostic & Error Audit Report\n\n")
        sb.append("- **Generated At:** ").append(SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault()).format(Date())).append("\n")
        sb.append("- **App Version:** PerkLens v1.0.0 (Release Build)\n")
        sb.append("- **Device Model:** ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append(" (SDK ").append(Build.VERSION.SDK_INT).append(")\n")
        sb.append("- **Active DB Size:** ").append(totalCardsInDb).append(" cards cataloged\n")
        sb.append("- **Total Logged Events:** ").append(_logs.value.size).append("\n\n")
        sb.append("---\n\n")
        sb.append("## 📋 Recorded Incident Traces\n\n")

        val currentLogs = _logs.value
        if (currentLogs.isEmpty()) {
            sb.append("✅ **All Systems Operational.** Zero failed updates or runtime exceptions recorded.\n")
        } else {
            for (entry in currentLogs) {
                sb.append("### [").append(entry.level).append("] [").append(entry.tag).append("] ").append(entry.timestamp).append("\n")
                sb.append("**Message:** ").append(entry.message).append("\n")
                if (!entry.technicalDetails.isNullOrBlank()) {
                    sb.append("```\n").append(entry.technicalDetails).append("\n```\n")
                }
                sb.append("\n")
            }
        }
        return sb.toString()
    }

    fun exportAndShareReport(context: Context, totalCardsInDb: Int = 162) {
        val report = generateDiagnosticsReport(totalCardsInDb)

        // Copy to clipboard
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("PerkLens Diagnostics", report)
        clipboard?.setPrimaryClip(clip)

        // Open Share Sheet
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "PerkLens Diagnostic Error Log")
            putExtra(Intent.EXTRA_TEXT, report)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val chooser = Intent.createChooser(sendIntent, "Export PerkLens Error Log")
        chooser.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        try {
            context.startActivity(chooser)
            Toast.makeText(context, "Log copied to clipboard & sharing opened", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Diagnostic report copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }
}
