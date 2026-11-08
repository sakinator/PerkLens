package io.perklens.data.repository

import android.content.Context
import com.google.gson.Gson
import io.perklens.data.model.Lounge
import io.perklens.data.model.LoungeCatalogContainer
import io.perklens.data.model.LoungeType
import java.io.InputStreamReader

class LoungeRepository(private val context: Context) {
    private val gson = Gson()
    private var cachedLounges: List<Lounge>? = null

    fun getAllLounges(): List<Lounge> {
        if (cachedLounges != null) return cachedLounges!!

        return try {
            context.assets.open("lounges_catalog.json").use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val data = gson.fromJson(reader, LoungeCatalogContainer::class.java)
                    cachedLounges = data.lounges
                    data.lounges
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun searchLounges(query: String): List<Lounge> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return getAllLounges()

        return getAllLounges().filter { lounge ->
            lounge.city.lowercase().contains(q) ||
            lounge.airportOrStation.lowercase().contains(q) ||
            lounge.name.lowercase().contains(q) ||
            lounge.terminal.lowercase().contains(q)
        }
    }

    fun getLoungesByType(type: LoungeType?): List<Lounge> {
        if (type == null) return getAllLounges()
        return getAllLounges().filter { it.type == type }
    }

    fun getLoungesEligibleForUser(userCardIds: Set<String>): List<Lounge> {
        return getAllLounges().filter { lounge ->
            lounge.eligibleCardIds.any { cardId -> userCardIds.contains(cardId) }
        }
    }
}
