package io.perklens.core.ai

import io.perklens.data.model.Card

data class PortfolioCoverage(
    val hasRupayUpi: Boolean,
    val hasLoungeAccess: Boolean,
    val hasOnlineCashback: Boolean,
    val hasDiningAccelerator: Boolean,
    val hasZeroForex: Boolean,
    val healthScorePercent: Int
)

data class CardRecommendation(
    val card: Card,
    val priorityRank: Int,
    val headlineReason: String,
    val solvedCategoryGap: String,
    val estimatedAnnualBenefitInr: Int,
    val isLtf: Boolean
)

object PortfolioRecommendationEngine {

    fun analyzeCoverage(userCards: List<Card>): PortfolioCoverage {
        if (userCards.isEmpty()) {
            return PortfolioCoverage(
                hasRupayUpi = false,
                hasLoungeAccess = false,
                hasOnlineCashback = false,
                hasDiningAccelerator = false,
                hasZeroForex = false,
                healthScorePercent = 0
            )
        }

        val hasRupay = userCards.any { it.network.contains("RuPay", ignoreCase = true) }
        val hasLounge = userCards.any {
            !it.loungeAccess.contains("No ", ignoreCase = true) && !it.loungeAccess.equals("None", ignoreCase = true)
        }
        val hasOnline = userCards.any {
            it.merchantRules.any { r -> r.cashbackPercent >= 5.0 && (r.category.contains("online") || r.category.contains("amazon")) }
        }
        val hasDining = userCards.any {
            it.merchantRules.any { r -> r.cashbackPercent >= 10.0 && (r.category.contains("swiggy") || r.category.contains("zomato") || r.category.contains("dining")) }
        }
        val hasForex = userCards.any { it.forexMarkupPercent <= 2.0 }

        var score = 0
        if (hasRupay) score += 20
        if (hasLounge) score += 20
        if (hasOnline) score += 25
        if (hasDining) score += 20
        if (hasForex) score += 15

        return PortfolioCoverage(
            hasRupayUpi = hasRupay,
            hasLoungeAccess = hasLounge,
            hasOnlineCashback = hasOnline,
            hasDiningAccelerator = hasDining,
            hasZeroForex = hasForex,
            healthScorePercent = score.coerceIn(0, 100)
        )
    }

    fun recommendNewCards(
        userCards: List<Card>,
        allMarketCards: List<Card>,
        suggestLtfOnly: Boolean
    ): List<CardRecommendation> {
        val coverage = analyzeCoverage(userCards)
        val ownedIds = userCards.map { it.id }.toSet()

        // Filter out cards already owned
        var candidates = allMarketCards.filter { !ownedIds.contains(it.id) }

        // Apply strict LTF filter if user turned on the setting
        if (suggestLtfOnly) {
            candidates = candidates.filter { it.isLifetimeFree }
        }

        val recommendations = mutableListOf<CardRecommendation>()

        // 1. Missing RuPay UPI gap
        if (!coverage.hasRupayUpi) {
            val rupayCard = candidates.find { it.network.contains("RuPay", ignoreCase = true) }
            if (rupayCard != null) {
                recommendations.add(
                    CardRecommendation(
                        card = rupayCard,
                        priorityRank = 1,
                        headlineReason = "Fills your missing UPI Credit gap — earn rewards on all daily QR scan-and-pay transactions.",
                        solvedCategoryGap = "RuPay UPI On Credit",
                        estimatedAnnualBenefitInr = 4500,
                        isLtf = rupayCard.isLifetimeFree
                    )
                )
            }
        }

        // 2. Missing Airport Lounge Access
        if (!coverage.hasLoungeAccess) {
            val loungeCard = candidates.find {
                !it.loungeAccess.contains("No ", ignoreCase = true) && !it.loungeAccess.equals("None", ignoreCase = true)
            }
            if (loungeCard != null && recommendations.none { it.card.id == loungeCard.id }) {
                recommendations.add(
                    CardRecommendation(
                        card = loungeCard,
                        priorityRank = 2,
                        headlineReason = "Fills your airport lounge gap: complimentary domestic airport lounge access with easy spend criteria.",
                        solvedCategoryGap = "Airport Lounge Access",
                        estimatedAnnualBenefitInr = 6000,
                        isLtf = loungeCard.isLifetimeFree
                    )
                )
            }
        }

        // 3. Missing Food Delivery & Dining Accelerator
        if (!coverage.hasDiningAccelerator) {
            val diningCard = candidates.find {
                it.merchantRules.any { r -> r.cashbackPercent >= 10.0 && (r.category.contains("swiggy") || r.category.contains("zomato") || r.category.contains("dining")) }
            }
            if (diningCard != null && recommendations.none { it.card.id == diningCard.id }) {
                recommendations.add(
                    CardRecommendation(
                        card = diningCard,
                        priorityRank = 3,
                        headlineReason = "Accelerate dining & food delivery: gives 10% direct cashback on Swiggy, Zomato, and dining bills.",
                        solvedCategoryGap = "Dining & Food Delivery (10%)",
                        estimatedAnnualBenefitInr = 6000,
                        isLtf = diningCard.isLifetimeFree
                    )
                )
            }
        }

        // 4. Missing Zero/Low Forex
        if (!coverage.hasZeroForex) {
            val forexCard = candidates.find { it.forexMarkupPercent <= 2.0 }
            if (forexCard != null && recommendations.none { it.card.id == forexCard.id }) {
                recommendations.add(
                    CardRecommendation(
                        card = forexCard,
                        priorityRank = 4,
                        headlineReason = "Zero/Low international transaction fee: saves 3.5% on foreign websites, subscriptions, and overseas trips.",
                        solvedCategoryGap = "Zero/Low Forex Markup",
                        estimatedAnnualBenefitInr = 3500,
                        isLtf = forexCard.isLifetimeFree
                    )
                )
            }
        }

        // 5. Missing 5% Online Shopping
        if (!coverage.hasOnlineCashback) {
            val onlineCard = candidates.find {
                it.merchantRules.any { r -> r.cashbackPercent >= 5.0 && r.category.contains("online") }
            }
            if (onlineCard != null && recommendations.none { it.card.id == onlineCard.id }) {
                recommendations.add(
                    CardRecommendation(
                        card = onlineCard,
                        priorityRank = 5,
                        headlineReason = "Master online shopping: 5% flat statement cashback across all major e-commerce platforms.",
                        solvedCategoryGap = "5% Online Shopping",
                        estimatedAnnualBenefitInr = 8000,
                        isLtf = onlineCard.isLifetimeFree
                    )
                )
            }
        }

        // If all gaps covered or user wants more, add remaining top candidates
        if (recommendations.size < 3) {
            candidates.filter { c -> recommendations.none { it.card.id == c.id } }
                .take(3 - recommendations.size)
                .forEach { remainingCard ->
                    recommendations.add(
                        CardRecommendation(
                            card = remainingCard,
                            priorityRank = 6,
                            headlineReason = "Outstanding companion card: ${remainingCard.bestForCategory}.",
                            solvedCategoryGap = remainingCard.bestForCategory,
                            estimatedAnnualBenefitInr = 3000,
                            isLtf = remainingCard.isLifetimeFree
                        )
                    )
                }
        }

        return recommendations.sortedBy { it.priorityRank }
    }
}
