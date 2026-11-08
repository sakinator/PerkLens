package io.perklens.data.model

import com.google.gson.annotations.SerializedName

enum class LoungeType {
    AIRPORT_DOMESTIC,
    AIRPORT_INTERNATIONAL,
    RAILWAY
}

enum class LoungeAccessCriteria {
    UNCONDITIONAL,     // ₹2 swipe (e.g. Scapia, Axis Atlas, Tata Neu Infinity)
    SPEND_LINKED,      // Requires spending ₹10k, ₹35k, or ₹50k in previous calendar quarter
    PRIORITY_PASS,     // International / Priority Pass lounge
    PAID_ONLY
}

data class Lounge(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("city") val city: String,
    @SerializedName("airportOrStation") val airportOrStation: String,
    @SerializedName("terminal") val terminal: String,
    @SerializedName("type") val type: LoungeType,
    @SerializedName("criteria") val criteria: LoungeAccessCriteria,
    @SerializedName("spendRequirementNote") val spendRequirementNote: String,
    @SerializedName("eligibleCardIds") val eligibleCardIds: List<String> = emptyList(),
    @SerializedName("eligibleNetworks") val eligibleNetworks: List<String> = emptyList(),
    @SerializedName("timings") val timings: String = "24 Hours",
    @SerializedName("locationDetail") val locationDetail: String,
    @SerializedName("amenities") val amenities: List<String> = emptyList()
)

data class LoungeCatalogContainer(
    @SerializedName("lounges") val lounges: List<Lounge> = emptyList()
)

data class CardPerks(
    val movieBogoSummary: String? = null,
    val movieBogoQuota: String? = null,
    val diningDiscountSummary: String? = null,
    val fuelWaiverBand: String = "1% waiver on ₹400–₹4,000 (Max ₹250/mo)",
    val milestoneTargetInr: Int? = null,
    val milestoneRewardSummary: String? = null
)

data class DevaluationAlert(
    val id: String,
    val bank: String,
    val cardAffected: String,
    val title: String,
    val effectiveDate: String,
    val severity: String, // "HIGH", "MEDIUM", "LOW"
    val summary: String,
    val actionAdvice: String
)

data class BillPaymentPortal(
    val portalName: String,
    val paymentMethod: String,
    val returnRatePercent: Double,
    val monthlyCapInr: Int,
    val headline: String,
    val instructions: String,
    val appUrl: String
)
