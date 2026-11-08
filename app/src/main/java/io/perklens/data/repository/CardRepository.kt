package io.perklens.data.repository

import android.content.Context
import com.google.gson.Gson
import io.perklens.data.model.Card
import io.perklens.data.model.CardCatalogContainer
import io.perklens.data.model.LiveOffer
import io.perklens.data.sync.RemoteCatalogSyncManager
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader

class CardRepository(
    private val context: Context,
    private val syncManager: RemoteCatalogSyncManager
) {
    private val gson = Gson()
    private var cachedCatalog: CardCatalogContainer? = null

    fun reloadCatalog(): CardCatalogContainer {
        cachedCatalog = null
        return getCatalog()
    }

    fun getCatalog(): CardCatalogContainer {
        if (cachedCatalog != null) return cachedCatalog!!

        // Tier 1: Check if an updated catalog was dynamically downloaded
        val dynamicFile = syncManager.getCachedCatalogFile()
        if (dynamicFile != null) {
            try {
                FileReader(dynamicFile).use { reader ->
                    val data = gson.fromJson(reader, CardCatalogContainer::class.java)
                    if (data != null && (data.cards.isNotEmpty() || data.liveOffers.isNotEmpty())) {
                        cachedCatalog = data
                        return data
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Tier 2: Fallback to embedded catalog in assets
        return try {
            context.assets.open("cards_catalog.json").use { inputStream ->
                InputStreamReader(inputStream).use { reader ->
                    val data = gson.fromJson(reader, CardCatalogContainer::class.java)
                    cachedCatalog = data
                    data
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CardCatalogContainer()
        }
    }

    fun getAllCards(): List<Card> = getCatalog().cards

    fun getLiveOffers(): List<LiveOffer> = getCatalog().liveOffers

    fun getCardById(id: String): Card? = getAllCards().find { it.id == id }

    fun searchCards(query: String): List<Card> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return getAllCards()

        return getAllCards().filter { card ->
            card.name.lowercase().contains(q) ||
            card.issuer.lowercase().contains(q) ||
            card.bestForCategory.lowercase().contains(q) ||
            card.network.lowercase().contains(q)
        }
    }

    fun getCardsByFilter(filter: String): List<Card> {
        return when (filter) {
            "LTF" -> getAllCards().filter { it.isLifetimeFree }
            "BoB" -> getAllCards().filter { it.issuer.contains("Baroda", ignoreCase = true) || it.name.contains("Baroda", ignoreCase = true) }
            "HDFC" -> getAllCards().filter { it.issuer.contains("HDFC", ignoreCase = true) }
            "SBI" -> getAllCards().filter { it.issuer.contains("SBI", ignoreCase = true) || it.issuer.contains("State Bank", ignoreCase = true) }
            "ICICI" -> getAllCards().filter { it.issuer.contains("ICICI", ignoreCase = true) }
            "Axis" -> getAllCards().filter { it.issuer.contains("Axis", ignoreCase = true) }
            "Amex" -> getAllCards().filter { it.issuer.contains("American Express", ignoreCase = true) || it.network.contains("Amex", ignoreCase = true) }
            "IDFC" -> getAllCards().filter { it.issuer.contains("IDFC", ignoreCase = true) }
            "IndusInd" -> getAllCards().filter { it.issuer.contains("IndusInd", ignoreCase = true) }
            "Kotak" -> getAllCards().filter { it.issuer.contains("Kotak", ignoreCase = true) }
            "Debit" -> getAllCards().filter { it.cardType.equals("DEBIT", ignoreCase = true) || it.name.contains("Debit", ignoreCase = true) }
            "Cashback" -> getAllCards().filter { it.rewardType.contains("Cashback", ignoreCase = true) }
            "Travel" -> getAllCards().filter { it.bestForCategory.contains("Travel", ignoreCase = true) || it.forexMarkupPercent <= 2.0 || it.loungeAccess.contains("lounge", ignoreCase = true) }
            "RuPay" -> getAllCards().filter { it.network.contains("RuPay", ignoreCase = true) }
            "LowFee" -> getAllCards().filter { it.annualFee in 1..1000 }
            else -> getAllCards()
        }
    }
}
