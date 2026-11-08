package io.perklens.data.model

import com.google.gson.annotations.SerializedName

data class CardCatalogContainer(
    @SerializedName("cards") val cards: List<Card> = emptyList(),
    @SerializedName("liveOffers") val liveOffers: List<LiveOffer> = emptyList()
)

data class Card(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("issuer") val issuer: String,
    @SerializedName("network") val network: String,
    @SerializedName("joiningFee") val joiningFee: Int,
    @SerializedName("annualFee") val annualFee: Int,
    @SerializedName("waiverSpendThreshold") val waiverSpendThreshold: Int,
    @SerializedName("isLifetimeFree") val isLifetimeFree: Boolean,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("bankLogoUrl") val bankLogoUrl: String? = null,
    @SerializedName("cardGradientStart") val cardGradientStart: String = "#1A1A1A",
    @SerializedName("cardGradientEnd") val cardGradientEnd: String = "#4A4A4A",
    @SerializedName("rating") val rating: Double = 4.5,
    @SerializedName("loungeAccess") val loungeAccess: String = "None",
    @SerializedName("forexMarkupPercent") val forexMarkupPercent: Double = 3.5,
    @SerializedName("rewardType") val rewardType: String = "Cashback",
    @SerializedName("bestForCategory") val bestForCategory: String = "General Shopping",
    @SerializedName("baseCashbackPercent") val baseCashbackPercent: Double = 1.0,
    @SerializedName("merchantRules") val merchantRules: List<MerchantRule> = emptyList(),
    @SerializedName("affiliateOffers") val affiliateOffers: List<AffiliateOffer> = emptyList()
)

data class MerchantRule(
    @SerializedName("category") val category: String,
    @SerializedName("cashbackPercent") val cashbackPercent: Double,
    @SerializedName("monthlyCapInr") val monthlyCapInr: Double? = null
)

data class AffiliateOffer(
    @SerializedName("portal") val portal: String, // "CashKaro", "EarnKaro", "GoPaisa"
    @SerializedName("rewardAmountInr") val rewardAmountInr: Int,
    @SerializedName("rewardType") val rewardType: String, // "Amazon Voucher", "Direct Bank", "Cashback"
    @SerializedName("applyUrl") val applyUrl: String,
    @SerializedName("portalLogoUrl") val portalLogoUrl: String? = null,
    @SerializedName("rating") val rating: String = "Recommended"
)

data class LiveOffer(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("bank") val bank: String,
    @SerializedName("discountSummary") val discountSummary: String,
    @SerializedName("couponCode") val couponCode: String,
    @SerializedName("category") val category: String,
    @SerializedName("validTill") val validTill: String,
    @SerializedName("terms") val terms: String,
    @SerializedName("brandLogoUrl") val brandLogoUrl: String? = null,
    @SerializedName("bankLogoUrl") val bankLogoUrl: String? = null
)

data class UserCard(
    val cardId: String,
    val billingCycleDay: Int = 1,
    val customNickname: String? = null,
    val currentMonthSpend: Double = 0.0
)

data class DecisionResult(
    val card: Card,
    val spendingAmount: Double,
    val effectivePercent: Double,
    val savingsInr: Double,
    val capReached: Boolean = false,
    val capNote: String? = null,
    val isBestChoice: Boolean = false
)
