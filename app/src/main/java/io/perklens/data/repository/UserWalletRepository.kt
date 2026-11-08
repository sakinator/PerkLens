package io.perklens.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

data class UserCardUsageData(
    val cardId: String,
    val billingCycleDay: Int = 15, // 1 to 31
    val availedMovieTicketsMonth: Int = 0,
    val availedLoungeVisitsQuarter: Int = 0,
    val availedCashbackMonthInr: Double = 0.0,
    val lastResetMonth: Int = -1
)

class UserWalletRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_wallet_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val KEY_SAVED_CARDS = "saved_card_ids"
    private val KEY_CARD_USAGES = "card_usages_json"

    private val _savedCardIds = MutableStateFlow<Set<String>>(emptySet())
    val savedCardIds: StateFlow<Set<String>> = _savedCardIds.asStateFlow()

    private val _cardUsages = MutableStateFlow<Map<String, UserCardUsageData>>(emptyMap())
    val cardUsages: StateFlow<Map<String, UserCardUsageData>> = _cardUsages.asStateFlow()

    init {
        loadCards()
        loadUsages()
    }

    private fun loadCards() {
        val stored = prefs.getStringSet(KEY_SAVED_CARDS, null)
        if (stored == null) {
            val defaultCards = setOf("sbi_cashback", "hdfc_millennia", "icici_amazon_pay")
            saveToPrefs(defaultCards)
            _savedCardIds.value = defaultCards
        } else {
            _savedCardIds.value = stored
        }
    }

    private fun loadUsages() {
        val json = prefs.getString(KEY_CARD_USAGES, null)
        if (!json.isNullOrBlank()) {
            try {
                val type = object : TypeToken<Map<String, UserCardUsageData>>() {}.type
                val map: Map<String, UserCardUsageData> = gson.fromJson(json, type)
                _cardUsages.value = map
                return
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        _cardUsages.value = emptyMap()
    }

    private fun saveUsages(map: Map<String, UserCardUsageData>) {
        _cardUsages.value = map
        prefs.edit().putString(KEY_CARD_USAGES, gson.toJson(map)).apply()
    }

    fun getUsage(cardId: String): UserCardUsageData {
        return _cardUsages.value[cardId] ?: UserCardUsageData(cardId = cardId)
    }

    fun setBillingCycleDay(cardId: String, day: Int) {
        val current = getUsage(cardId)
        val updatedMap = _cardUsages.value.toMutableMap()
        updatedMap[cardId] = current.copy(billingCycleDay = day.coerceIn(1, 31))
        saveUsages(updatedMap)
    }

    fun recordAvailedMovieTicket(cardId: String, delta: Int) {
        val current = getUsage(cardId)
        val updatedMap = _cardUsages.value.toMutableMap()
        val newCount = (current.availedMovieTicketsMonth + delta).coerceAtLeast(0)
        updatedMap[cardId] = current.copy(availedMovieTicketsMonth = newCount)
        saveUsages(updatedMap)
    }

    fun recordAvailedLoungeVisit(cardId: String, delta: Int) {
        val current = getUsage(cardId)
        val updatedMap = _cardUsages.value.toMutableMap()
        val newCount = (current.availedLoungeVisitsQuarter + delta).coerceAtLeast(0)
        updatedMap[cardId] = current.copy(availedLoungeVisitsQuarter = newCount)
        saveUsages(updatedMap)
    }

    fun recordAvailedCashback(cardId: String, amount: Double) {
        val current = getUsage(cardId)
        val updatedMap = _cardUsages.value.toMutableMap()
        val newAmount = (current.availedCashbackMonthInr + amount).coerceAtLeast(0.0)
        updatedMap[cardId] = current.copy(availedCashbackMonthInr = newAmount)
        saveUsages(updatedMap)
    }

    fun calculateGracePeriodDays(billingDay: Int): Int {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        // If today is before billing day: days until bill + 20 days grace
        // If today is on/after billing day: days until next month's bill + 20 days grace
        val daysUntilBill = if (today < billingDay) {
            billingDay - today
        } else {
            (30 - today) + billingDay
        }
        return daysUntilBill + 20 // Standard Indian bank ~20 days payment due grace
    }

    fun addCard(cardId: String) {
        val updated = _savedCardIds.value.toMutableSet().apply { add(cardId) }
        saveToPrefs(updated)
        _savedCardIds.value = updated
    }

    fun removeCard(cardId: String) {
        val updated = _savedCardIds.value.toMutableSet().apply { remove(cardId) }
        saveToPrefs(updated)
        _savedCardIds.value = updated
    }

    private fun saveToPrefs(cards: Set<String>) {
        prefs.edit().putStringSet(KEY_SAVED_CARDS, cards).apply()
    }
}
