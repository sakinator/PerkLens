package io.perklens.engine

import io.perklens.data.model.Card
import io.perklens.data.model.DecisionResult
import kotlin.math.min

object RewardDecisionEngine {

    /**
     * Evaluates all owned cards for a given transaction amount and merchant/category,
     * returning a sorted list from highest savings to lowest.
     */
    fun evaluateBestCard(
        ownedCards: List<Card>,
        amount: Double,
        categoryKey: String
    ): List<DecisionResult> {
        if (ownedCards.isEmpty() || amount <= 0) return emptyList()

        val results = ownedCards.map { card ->
            calculateCardSavings(card, amount, categoryKey)
        }

        // Sort descending by savingsInr, then by rate
        val sorted = results.sortedWith(
            compareByDescending<DecisionResult> { it.savingsInr }
                .thenByDescending { it.effectivePercent }
        )

        // Mark the top candidate as best pick
        return sorted.mapIndexed { index, item ->
            if (index == 0 && item.savingsInr > 0) {
                item.copy(isBestChoice = true)
            } else {
                item
            }
        }
    }

    private fun calculateCardSavings(
        card: Card,
        amount: Double,
        categoryKey: String
    ): DecisionResult {
        // Find exact merchant rule match
        val rule = card.merchantRules.find { it.category.equals(categoryKey, ignoreCase = true) }

        val (rate, cap, note) = when {
            rule != null -> {
                Triple(rule.cashbackPercent, rule.monthlyCapInr, null)
            }
            categoryKey.contains("offline", ignoreCase = true) -> {
                val offlineRule = card.merchantRules.find { it.category == "offline_general" }
                val rate = offlineRule?.cashbackPercent ?: card.baseCashbackPercent
                Triple(rate, offlineRule?.monthlyCapInr, "Offline base rate applied")
            }
            else -> {
                // Default to online general rule or base cashback
                val onlineRule = card.merchantRules.find { it.category == "online_general" }
                val rate = onlineRule?.cashbackPercent ?: card.baseCashbackPercent
                Triple(rate, onlineRule?.monthlyCapInr, "General rate applied")
            }
        }

        // Calculate uncapped savings
        val rawSavings = (amount * rate) / 100.0

        // Handle monthly cap
        var finalSavings = rawSavings
        var capReached = false
        var capNote: String? = note

        if (cap != null && cap > 0) {
            if (rawSavings > cap) {
                finalSavings = cap
                capReached = true
                capNote = "Capped at ₹${cap.toInt()}/month"
            }
        }

        return DecisionResult(
            card = card,
            spendingAmount = amount,
            effectivePercent = rate,
            savingsInr = finalSavings,
            capReached = capReached,
            capNote = capNote,
            isBestChoice = false
        )
    }
}
