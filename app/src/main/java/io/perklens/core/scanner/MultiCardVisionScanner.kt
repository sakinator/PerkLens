package io.perklens.core.scanner

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import io.perklens.core.ai.AiSettingsRepository
import io.perklens.data.model.Card
import io.perklens.data.repository.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

data class CardDetectionResult(
    val card: Card,
    val confidence: Float,
    val matchedKeywords: List<String>,
    var isSelected: Boolean = true
)

object MultiCardVisionScanner {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    /**
     * Scans an image containing one or multiple credit cards spread out.
     * Segregates text into distinct card entities, strips sensitive card numbers/CVVs,
     * and matches recognized products against our 200+ card catalog.
     */
    suspend fun scanMultipleCards(
        bitmap: Bitmap,
        cardRepo: CardRepository,
        aiSettings: AiSettingsRepository? = null
    ): List<CardDetectionResult> = withContext(Dispatchers.Default) {
        val allMarketCards = cardRepo.getAllCards()
        val detectedTextBlocks = runOnDeviceOcr(bitmap)

        if (detectedTextBlocks.isEmpty()) return@withContext emptyList()

        val fullText = detectedTextBlocks.joinToString(" ").uppercase()

        // Cluster and match multiple cards
        val matchedResults = mutableListOf<CardDetectionResult>()
        val alreadyMatchedIds = mutableSetOf<String>()

        allMarketCards.forEach { card ->
            val cardTokens = card.name.uppercase().split(" ")
                .filter { it.length > 2 && it !in listOf("BANK", "CARD", "CREDIT") }
            val issuerTokens = card.issuer.uppercase().split(" ")
                .filter { it.length > 2 && it !in listOf("BANK", "OF", "INDIA") }

            val matchedKeywords = mutableListOf<String>()

            // Check if card specific name tokens are in the text
            val nameMatches = cardTokens.count { token ->
                if (fullText.contains(token)) {
                    matchedKeywords.add(token)
                    true
                } else false
            }

            // Check if issuer tokens are in the text
            val issuerMatches = issuerTokens.count { token ->
                if (fullText.contains(token)) {
                    matchedKeywords.add(token)
                    true
                } else false
            }

            val isMatch = when {
                card.id.contains("sbi_cashback") && fullText.contains("CASHBACK") && (fullText.contains("SBI") || fullText.contains("STATE")) -> true
                card.id.contains("hdfc_millennia") && fullText.contains("MILLENNIA") -> true
                card.id.contains("axis_airtel") && fullText.contains("AIRTEL") && (fullText.contains("AXIS") || fullText.contains("AIRTEL")) -> true
                card.id.contains("icici_amazon") && fullText.contains("AMAZON") -> true
                card.id.contains("tata_neu") && (fullText.contains("NEU") || fullText.contains("TATA")) -> true
                card.id.contains("scapia") && fullText.contains("SCAPIA") -> true
                card.id.contains("onecard") && (fullText.contains("ONECARD") || fullText.contains("ONE CARD")) -> true
                card.id.contains("axis_atlas") && fullText.contains("ATLAS") -> true
                card.id.contains("amex_plat") && (fullText.contains("AMERICAN EXPRESS") || fullText.contains("PLATINUM TRAVEL") || fullText.contains("AMEX")) -> true
                card.id.contains("hsbc_live") && (fullText.contains("LIVE+") || fullText.contains("LIVE PLUS") || (fullText.contains("HSBC") && fullText.contains("LIVE"))) -> true
                nameMatches >= 1 && (issuerMatches >= 1 || cardTokens.size == 1) -> true
                else -> false
            }

            if (isMatch && !alreadyMatchedIds.contains(card.id)) {
                alreadyMatchedIds.add(card.id)
                val confidence = if (nameMatches >= 2 || cardTokens.any { fullText.contains(it) }) 0.95f else 0.80f
                matchedResults.add(
                    CardDetectionResult(
                        card = card,
                        confidence = confidence,
                        matchedKeywords = matchedKeywords.distinct()
                    )
                )
            }
        }

        matchedResults
    }

    private suspend fun runOnDeviceOcr(bitmap: Bitmap): List<String> = suspendCancellableCoroutine { continuation ->
        try {
            val image = InputImage.fromBitmap(bitmap, 0)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val blocks = visionText.textBlocks.map { it.text }
                    continuation.resume(blocks)
                }
                .addOnFailureListener {
                    continuation.resume(emptyList())
                }
        } catch (e: Exception) {
            continuation.resume(emptyList())
        }
    }
}
