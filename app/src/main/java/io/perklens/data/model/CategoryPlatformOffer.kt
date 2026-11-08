package io.perklens.data.model

import com.google.gson.annotations.SerializedName

data class CategoryPlatformOffer(
    val cardId: String,
    val category: String, // "movies", "dining", "shopping", "grocery", "travel", "fuel", "utility"
    val platform: String, // "BookMyShow", "PVR INOX", "Swiggy", "Zomato", "Amazon", "MakeMyTrip"
    val platformLogoUrl: String? = null,
    val offerTitle: String,
    val discountValue: String,
    val quotaTerms: String,
    val redemptionMethod: String
)

data class CardMilestoneInfo(
    val cardId: String,
    val annualFeeWaiverSpendInr: Int,
    val bonusMilestoneTargetInr: Int? = null,
    val bonusMilestoneReward: String? = null,
    val currentSpendLogged: Double = 0.0
)
