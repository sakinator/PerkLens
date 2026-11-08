package io.perklens.core.parser

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.perklens.data.repository.CardRepository
import io.perklens.data.repository.UserWalletRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.Calendar

class SmsSyncManager(
    private val context: Context,
    private val walletRepo: UserWalletRepository,
    private val cardRepo: CardRepository
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("sms_sync_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val KEY_SMS_SYNC_ENABLED = "sms_sync_enabled"
    private val KEY_GMAIL_SYNC_ENABLED = "gmail_sync_enabled"
    private val KEY_RECENT_TRANSACTIONS = "recent_txns_json"
    private val KEY_LAST_SCAN_TIME = "last_sms_scan_time"

    private val _isSmsSyncEnabled = MutableStateFlow(prefs.getBoolean(KEY_SMS_SYNC_ENABLED, false))
    val isSmsSyncEnabled: StateFlow<Boolean> = _isSmsSyncEnabled.asStateFlow()

    private val _isGmailSyncEnabled = MutableStateFlow(prefs.getBoolean(KEY_GMAIL_SYNC_ENABLED, false))
    val isGmailSyncEnabled: StateFlow<Boolean> = _isGmailSyncEnabled.asStateFlow()

    private val _recentTransactions = MutableStateFlow<List<ParsedBankTransaction>>(loadRecentTransactions())
    val recentTransactions: StateFlow<List<ParsedBankTransaction>> = _recentTransactions.asStateFlow()

    fun setSmsSyncEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SMS_SYNC_ENABLED, enabled).apply()
        _isSmsSyncEnabled.value = enabled
    }

    fun setGmailSyncEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_GMAIL_SYNC_ENABLED, enabled).apply()
        _isGmailSyncEnabled.value = enabled
    }

    fun getLastScanTime(): String {
        return prefs.getString(KEY_LAST_SCAN_TIME, "Never") ?: "Never"
    }

    private fun loadRecentTransactions(): List<ParsedBankTransaction> {
        val json = prefs.getString(KEY_RECENT_TRANSACTIONS, null)
        if (!json.isNullOrBlank()) {
            try {
                val type = object : TypeToken<List<ParsedBankTransaction>>() {}.type
                return gson.fromJson(json, type)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return emptyList()
    }

    private fun saveTransactions(list: List<ParsedBankTransaction>) {
        _recentTransactions.value = list
        prefs.edit().putString(KEY_RECENT_TRANSACTIONS, gson.toJson(list)).apply()
    }

    /**
     * Reads past 30 days of bank SMS on-device. Zero network transmissions.
     */
    suspend fun scanInbox(daysBack: Int = 30): Int = withContext(Dispatchers.IO) {
        val parsedList = mutableListOf<ParsedBankTransaction>()
        val cutoffTime = System.currentTimeMillis() - (daysBack.toLong() * 24 * 60 * 60 * 1000)

        val uri = Uri.parse("content://sms/inbox")
        val projection = arrayOf("address", "body", "date")
        val selection = "date >= ?"
        val selectionArgs = arrayOf(cutoffTime.toString())

        try {
            val cursor = context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                "date DESC"
            )

            cursor?.use {
                val addressIdx = it.getColumnIndexOrThrow("address")
                val bodyIdx = it.getColumnIndexOrThrow("body")
                val dateIdx = it.getColumnIndexOrThrow("date")

                while (it.moveToNext()) {
                    val address = it.getString(addressIdx) ?: ""
                    val body = it.getString(bodyIdx) ?: ""
                    val date = it.getLong(dateIdx)

                    val parsed = BankSmsParser.parseMessage(address, body, date)
                    if (parsed != null) {
                        parsedList.add(parsed)

                        // Update wallet billing day if this is a statement
                        if (parsed.isStatement) {
                            val cal = Calendar.getInstance().apply { timeInMillis = date }
                            val day = cal.get(Calendar.DAY_OF_MONTH)
                            // Match card by bank
                            val matchingCards = cardRepo.getAllCards().filter { card ->
                                parsed.bank.contains(card.issuer, ignoreCase = true) || card.issuer.contains(parsed.bank, ignoreCase = true)
                            }
                            matchingCards.forEach { card ->
                                walletRepo.setBillingCycleDay(card.id, day)
                            }
                        }
                    }
                }
            }

            if (parsedList.isNotEmpty()) {
                val combined = (parsedList + _recentTransactions.value).distinctBy { it.id }.take(50)
                saveTransactions(combined)
            }

            val nowStr = java.text.SimpleDateFormat("dd MMM, hh:mm a", java.util.Locale.getDefault()).format(java.util.Date())
            prefs.edit().putString(KEY_LAST_SCAN_TIME, nowStr).apply()

            parsedList.size
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}
