package io.perklens.core.parser

import io.perklens.data.model.Card
import java.util.Calendar

enum class QuotaWindow(val label: String) {
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    SEMI_ANNUAL("6-Months"),
    ANNUAL("Yearly / Milestone")
}

data class ExhaustedOfferStatus(
    val cardId: String,
    val cardName: String,
    val perkTitle: String,
    val category: String, // "movies", "lounge", "dining", "utility", "milestone"
    val window: QuotaWindow,
    val totalQuotaLimit: Double,
    val consumedAmount: Double,
    val isFullyExhausted: Boolean,
    val remainingValue: Double,
    val unit: String, // "Tickets", "Visits", "INR", "Cashback"
    val resetDateNotice: String,
    val sourceTrigger: String
)

object IntelligentOfferExhaustionEngine {

    fun analyzeExhaustion(
        transactions: List<ParsedBankTransaction>,
        userCards: List<Card>,
        billingDays: Map<String, Int>
    ): List<ExhaustedOfferStatus> {
        val results = mutableListOf<ExhaustedOfferStatus>()
        val cal = Calendar.getInstance()
        val currentMonth = cal.get(Calendar.MONTH)
        val currentYear = cal.get(Calendar.YEAR)
        val currentQuarter = (currentMonth / 3) + 1

        userCards.forEach { card ->
            val billingDay = billingDays[card.id] ?: 15
            val daysUntilReset = if (cal.get(Calendar.DAY_OF_MONTH) < billingDay) {
                billingDay - cal.get(Calendar.DAY_OF_MONTH)
            } else {
                (30 - cal.get(Calendar.DAY_OF_MONTH)) + billingDay
            }

            // Filter transactions for this bank / card
            val cardTxns = transactions.filter { txn ->
                txn.bank.contains(card.issuer, ignoreCase = true) || card.issuer.contains(txn.bank, ignoreCase = true)
            }

            // 1. MONTHLY: Movie Tickets (BookMyShow / PVR / Inox)
            val movieTxns = cardTxns.filter {
                it.merchant.contains("BookMyShow", ignoreCase = true) ||
                it.merchant.contains("PVR", ignoreCase = true) ||
                it.merchant.contains("INOX", ignoreCase = true) ||
                it.merchant.contains("Cinema", ignoreCase = true)
            }
            val movieQuotaMax = if (card.id.contains("hdfc") || card.id.contains("indusind") || card.id.contains("icici")) 2.0 else 1.0
            val moviesConsumed = movieTxns.size.toDouble().coerceAtMost(movieQuotaMax)
            val isMovieExhausted = moviesConsumed >= movieQuotaMax

            results.add(
                ExhaustedOfferStatus(
                    cardId = card.id,
                    cardName = card.name,
                    perkTitle = "Cinema BOGO / Ticket Privilege",
                    category = "movies",
                    window = QuotaWindow.MONTHLY,
                    totalQuotaLimit = movieQuotaMax,
                    consumedAmount = moviesConsumed,
                    isFullyExhausted = isMovieExhausted,
                    remainingValue = (movieQuotaMax - moviesConsumed).coerceAtLeast(0.0),
                    unit = "Tickets",
                    resetDateNotice = "Resets in $daysUntilReset days (Day $billingDay)",
                    sourceTrigger = if (movieTxns.isNotEmpty()) "Detected ${movieTxns.size} ticket bookings via SMS/Email" else "No bookings detected this cycle"
                )
            )

            // 2. QUARTERLY: Airport Lounge Swipes
            val loungeTxns = cardTxns.filter {
                it.merchant.contains("Lounge", ignoreCase = true) ||
                it.merchant.contains("DreamFolks", ignoreCase = true) ||
                it.merchant.contains("Encalm", ignoreCase = true) ||
                it.merchant.contains("080", ignoreCase = true) ||
                (it.amountInr in 2.0..25.0) // Standard ₹2 / ₹25 Indian airport lounge auth charge
            }
            val loungeQuarterMax = 2.0 // Typically 2 visits per quarter
            val loungeConsumed = loungeTxns.size.toDouble().coerceAtMost(loungeQuarterMax)
            val isLoungeExhausted = loungeConsumed >= loungeQuarterMax

            val daysUntilQuarterEnd = 30 * (3 - (currentMonth % 3))
            results.add(
                ExhaustedOfferStatus(
                    cardId = card.id,
                    cardName = card.name,
                    perkTitle = "Airport Lounge Access",
                    category = "lounge",
                    window = QuotaWindow.QUARTERLY,
                    totalQuotaLimit = loungeQuarterMax,
                    consumedAmount = loungeConsumed,
                    isFullyExhausted = isLoungeExhausted,
                    remainingValue = (loungeQuarterMax - loungeConsumed).coerceAtLeast(0.0),
                    unit = "Visits",
                    resetDateNotice = "Resets in ~${daysUntilQuarterEnd} days (Q${currentQuarter + 1})",
                    sourceTrigger = if (loungeTxns.isNotEmpty()) "Detected ${loungeTxns.size} lounge entries via SMS auth" else "Full quarterly quota available"
                )
            )

            // 3. MONTHLY: Dining & Food Delivery Cashback Cap
            val diningTxns = cardTxns.filter {
                it.merchant.contains("Swiggy", ignoreCase = true) ||
                it.merchant.contains("Zomato", ignoreCase = true) ||
                it.merchant.contains("EazyDiner", ignoreCase = true)
            }
            val totalDiningSpend = diningTxns.sumOf { it.amountInr }
            val diningCapRule = card.merchantRules.find { it.category == "swiggy" || it.category == "dining" }
            val diningCapInr = diningCapRule?.monthlyCapInr ?: 500.0
            val diningCashbackEarned = (totalDiningSpend * (diningCapRule?.cashbackPercent ?: 5.0) / 100.0).coerceAtMost(diningCapInr)
            val isDiningCapExhausted = diningCashbackEarned >= diningCapInr

            results.add(
                ExhaustedOfferStatus(
                    cardId = card.id,
                    cardName = card.name,
                    perkTitle = "Food Delivery Cashback Cap",
                    category = "dining",
                    window = QuotaWindow.MONTHLY,
                    totalQuotaLimit = diningCapInr,
                    consumedAmount = diningCashbackEarned,
                    isFullyExhausted = isDiningCapExhausted,
                    remainingValue = (diningCapInr - diningCashbackEarned).coerceAtLeast(0.0),
                    unit = "INR Cashback",
                    resetDateNotice = "Resets in $daysUntilReset days (Day $billingDay)",
                    sourceTrigger = "Spent ₹${totalDiningSpend.toInt()} on Swiggy/Zomato this month"
                )
            )

            // 4. ANNUAL: Annual Fee Waiver & Milestone Tracker
            val totalYearSpend = cardTxns.sumOf { it.amountInr }
            val waiverGoal = card.waiverSpendThreshold.toDouble()
            val isWaiverAchieved = card.isLifetimeFree || (totalYearSpend >= waiverGoal)

            results.add(
                ExhaustedOfferStatus(
                    cardId = card.id,
                    cardName = card.name,
                    perkTitle = if (card.isLifetimeFree) "Lifetime Free Status" else "Annual Fee Waiver Goal",
                    category = "milestone",
                    window = QuotaWindow.ANNUAL,
                    totalQuotaLimit = waiverGoal,
                    consumedAmount = totalYearSpend,
                    isFullyExhausted = isWaiverAchieved,
                    remainingValue = if (card.isLifetimeFree) 0.0 else (waiverGoal - totalYearSpend).coerceAtLeast(0.0),
                    unit = "INR Spend",
                    resetDateNotice = "Annual renewal cycle",
                    sourceTrigger = if (card.isLifetimeFree) "Zero fee forever" else "Logged ₹${totalYearSpend.toInt()} toward ₹${waiverGoal.toInt()} waiver goal"
                )
            )
        }

        return results
    }
}
