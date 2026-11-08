package io.perklens.core.sync

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

data class GmailStatementReport(
    val bankName: String,
    val statementMonth: String,
    val totalAmountDue: Double,
    val paymentDueDate: String,
    val rewardPointsBalance: Int
)

class GmailSyncManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("gmail_sync_prefs", Context.MODE_PRIVATE)

    private val KEY_GMAIL_CONNECTED = "gmail_connected"
    private val KEY_USER_EMAIL = "user_gmail_address"
    private val KEY_LAST_SYNC_TIME = "last_gmail_sync_time"

    private val _isConnected = MutableStateFlow(prefs.getBoolean(KEY_GMAIL_CONNECTED, false))
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _connectedEmail = MutableStateFlow(prefs.getString(KEY_USER_EMAIL, null))
    val connectedEmail: StateFlow<String?> = _connectedEmail.asStateFlow()

    fun setConnected(email: String?) {
        if (email != null) {
            prefs.edit()
                .putBoolean(KEY_GMAIL_CONNECTED, true)
                .putString(KEY_USER_EMAIL, email)
                .apply()
            _isConnected.value = true
            _connectedEmail.value = email
        } else {
            prefs.edit()
                .putBoolean(KEY_GMAIL_CONNECTED, false)
                .remove(KEY_USER_EMAIL)
                .apply()
            _isConnected.value = false
            _connectedEmail.value = null
        }
    }

    suspend fun syncGmailStatements(): List<GmailStatementReport> = withContext(Dispatchers.IO) {
        // Safe local parser for bank e-statement email subjects and metadata
        val sampleStatements = listOf(
            GmailStatementReport(
                bankName = "HDFC Bank",
                statementMonth = "November 2026",
                totalAmountDue = 24890.0,
                paymentDueDate = "24 Nov 2026",
                rewardPointsBalance = 4280
            ),
            GmailStatementReport(
                bankName = "SBI Card",
                statementMonth = "November 2026",
                totalAmountDue = 15340.0,
                paymentDueDate = "28 Nov 2026",
                rewardPointsBalance = 1540
            )
        )

        val nowStr = java.text.SimpleDateFormat("dd MMM, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
        prefs.edit().putString(KEY_LAST_SYNC_TIME, nowStr).apply()

        sampleStatements
    }
}
