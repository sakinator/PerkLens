package io.perklens.data.repository

import io.perklens.data.model.Card
import io.perklens.data.model.CategoryPlatformOffer

class CategoryOffersRepository(private val cardRepo: CardRepository) {

    private val allPlatformOffers = listOf(
        // === MOVIES & ENTERTAINMENT ===
        CategoryPlatformOffer(
            cardId = "hdfc_millennia",
            category = "movies",
            platform = "BookMyShow & PVR",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/BookMyShow_logo.svg/512px-BookMyShow_logo.svg.png",
            offerTitle = "5% CashPoints via SmartBuy",
            discountValue = "5% back as CashPoints (1 CP = ₹1)",
            quotaTerms = "Max 1,000 CashPoints per month",
            redemptionMethod = "Book through HDFC SmartBuy portal or direct checkout"
        ),
        CategoryPlatformOffer(
            cardId = "sbi_cashback",
            category = "movies",
            platform = "BookMyShow / Paytm Movies",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/BookMyShow_logo.svg/512px-BookMyShow_logo.svg.png",
            offerTitle = "5% Direct Cashback",
            discountValue = "5% flat statement credit on tickets & F&B",
            quotaTerms = "Covered under ₹5,000/mo online cashback pool",
            redemptionMethod = "Direct online payment on BookMyShow / Paytm"
        ),
        CategoryPlatformOffer(
            cardId = "onecard_metal",
            category = "movies",
            platform = "BookMyShow & INOX",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/BookMyShow_logo.svg/512px-BookMyShow_logo.svg.png",
            offerTitle = "5X Reward Points Booster",
            discountValue = "Up to 5X rewards on monthly movie spending",
            quotaTerms = "Automatically activated if Movies is in your top 2 categories",
            redemptionMethod = "Pay using OneCard online or POS at ticket counter"
        ),
        CategoryPlatformOffer(
            cardId = "axis_airtel",
            category = "movies",
            platform = "BookMyShow",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/BookMyShow_logo.svg/512px-BookMyShow_logo.svg.png",
            offerTitle = "1% Base Cashback + Brand Sales",
            discountValue = "1% cashback + eligible for Axis bank promo days",
            quotaTerms = "No monthly cap on 1% base reward",
            redemptionMethod = "Select Axis Bank Offer on BookMyShow checkout"
        ),

        // === DINING & FOOD DELIVERY ===
        CategoryPlatformOffer(
            cardId = "axis_airtel",
            category = "dining",
            platform = "Swiggy & Zomato",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Swiggy_logo.svg/512px-Swiggy_logo.svg.png",
            offerTitle = "Flat 10% Instant Statement Cashback",
            discountValue = "10% cashback credited directly in next bill",
            quotaTerms = "Max ₹500 cashback per month combined",
            redemptionMethod = "Direct online payment on Swiggy or Zomato app"
        ),
        CategoryPlatformOffer(
            cardId = "hsbc_live_plus",
            category = "dining",
            platform = "Swiggy, Zomato & Dineout",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/75/Zomato_logo.png/512px-Zomato_logo.png",
            offerTitle = "10% Direct Statement Cashback",
            discountValue = "10% cashback on all food delivery & restaurant bills",
            quotaTerms = "Max ₹1,000 per calendar month",
            redemptionMethod = "Pay online or swipe at restaurant POS machine"
        ),
        CategoryPlatformOffer(
            cardId = "hdfc_millennia",
            category = "dining",
            platform = "Swiggy & Zomato",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Swiggy_logo.svg/512px-Swiggy_logo.svg.png",
            offerTitle = "5% CashPoints Accelerator",
            discountValue = "5% back on all Swiggy & Zomato orders",
            quotaTerms = "Combined ₹1,000 CashPoints cap per month",
            redemptionMethod = "Direct card payment on Swiggy or Zomato"
        ),
        CategoryPlatformOffer(
            cardId = "sbi_cashback",
            category = "dining",
            platform = "Swiggy, Zomato & EazyDiner",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/en/thumb/1/12/Swiggy_logo.svg/512px-Swiggy_logo.svg.png",
            offerTitle = "5% Flat Cashback",
            discountValue = "5% statement credit on online food delivery",
            quotaTerms = "Covered under ₹5,000/mo overall pool",
            redemptionMethod = "Direct online checkout on food apps"
        ),

        // === ONLINE SHOPPING ===
        CategoryPlatformOffer(
            cardId = "icici_amazon_pay",
            category = "shopping",
            platform = "Amazon India",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Amazon_logo.svg/512px-Amazon_logo.svg.png",
            offerTitle = "5% Unlimited Cashback (Prime)",
            discountValue = "5% auto-credited as Amazon Pay Balance",
            quotaTerms = "Zero monthly cap (Unlimited earning)",
            redemptionMethod = "Save card on Amazon India account"
        ),
        CategoryPlatformOffer(
            cardId = "sbi_cashback",
            category = "shopping",
            platform = "Amazon, Flipkart & Myntra",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/en/thumb/7/7a/Flipkart_logo.svg/512px-Flipkart_logo.svg.png",
            offerTitle = "5% Universal Online Cashback",
            discountValue = "5% flat statement credit on almost all online shopping",
            quotaTerms = "Max ₹5,000 cashback per billing cycle",
            redemptionMethod = "Direct online payment on any shopping portal"
        ),
        CategoryPlatformOffer(
            cardId = "tata_neu_infinity",
            category = "shopping",
            platform = "Tata Neu, Croma & Westside",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/HDFC_Bank_Logo.svg/512px-HDFC_Bank_Logo.svg.png",
            offerTitle = "10% NeuCoins (5% + 5% NeuPass)",
            discountValue = "10% return across all Tata brand stores",
            quotaTerms = "1 NeuCoin = ₹1 across Tata ecosystem",
            redemptionMethod = "Shop via Tata Neu App or Croma stores"
        ),

        // === QUICK GROCERIES ===
        CategoryPlatformOffer(
            cardId = "axis_airtel",
            category = "grocery",
            platform = "BigBasket",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Bigbasket_Logo.png/512px-Bigbasket_Logo.png",
            offerTitle = "10% Instant Statement Cashback",
            discountValue = "10% cashback on grocery purchases",
            quotaTerms = "Shared ₹500 monthly cap with dining",
            redemptionMethod = "Direct checkout on BigBasket app/website"
        ),
        CategoryPlatformOffer(
            cardId = "hsbc_live_plus",
            category = "grocery",
            platform = "Blinkit, Zepto & Instamart",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Blinkit-yellow-app-icon.svg/512px-Blinkit-yellow-app-icon.svg.png",
            offerTitle = "10% Direct Statement Cashback",
            discountValue = "10% on quick commerce & supermarket purchases",
            quotaTerms = "Up to ₹1,000 cashback per month",
            redemptionMethod = "Pay on Blinkit, Zepto, or Instamart apps"
        ),
        CategoryPlatformOffer(
            cardId = "tata_neu_infinity",
            category = "grocery",
            platform = "BigBasket & BB Now",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/Bigbasket_Logo.png/512px-Bigbasket_Logo.png",
            offerTitle = "10% NeuCoins on Groceries",
            discountValue = "5% NeuCoins on card + 5% NeuPass membership",
            quotaTerms = "Unlimited NeuCoins earning",
            redemptionMethod = "Order via BigBasket or Tata Neu"
        ),

        // === RECHARGE & UTILITIES ===
        CategoryPlatformOffer(
            cardId = "axis_airtel",
            category = "utility",
            platform = "Airtel Thanks App",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Axis_Bank_logo.svg/512px-Axis_Bank_logo.svg.png",
            offerTitle = "25% Cashback on Mobile/Broadband",
            discountValue = "Flat 25% cashback on Airtel bills",
            quotaTerms = "Max ₹250 per calendar month",
            redemptionMethod = "Pay via Airtel Thanks App using this card"
        ),
        CategoryPlatformOffer(
            cardId = "axis_airtel",
            category = "utility",
            platform = "Electricity, Water, Gas Bills",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Axis_Bank_logo.svg/512px-Axis_Bank_logo.svg.png",
            offerTitle = "10% Cashback on Utility Bills",
            discountValue = "10% cashback on BBPS utility payments",
            quotaTerms = "Max ₹250 per calendar month",
            redemptionMethod = "Pay utility bills via Airtel Thanks App BBPS"
        ),
        CategoryPlatformOffer(
            cardId = "icici_amazon_pay",
            category = "utility",
            platform = "Amazon Pay Recharges & Bills",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Amazon_logo.svg/512px-Amazon_logo.svg.png",
            offerTitle = "2% Unlimited Cashback",
            discountValue = "2% auto-credited Amazon Pay Balance on recharges",
            quotaTerms = "No monthly cap or limits",
            redemptionMethod = "Pay via Amazon Pay Recharges & Bill Payments"
        ),

        // === TRAVEL & FLIGHTS ===
        CategoryPlatformOffer(
            cardId = "scapia_federal",
            category = "travel",
            platform = "Scapia Travel App",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/Federal_Bank_Logo.svg/512px-Federal_Bank_Logo.svg.png",
            offerTitle = "10% Scapia Coins + 0% Forex",
            discountValue = "10% return on flights/hotels + Zero forex markup abroad",
            quotaTerms = "5 Scapia Coins = ₹1 on travel redemption",
            redemptionMethod = "Book through Scapia in-app travel portal"
        ),
        CategoryPlatformOffer(
            cardId = "axis_atlas",
            category = "travel",
            platform = "Direct Airlines & Hotels",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Axis_Bank_logo.svg/512px-Axis_Bank_logo.svg.png",
            offerTitle = "5 EDGE Miles per ₹100",
            discountValue = "Equivalent to 10 Airline/Hotel Miles (8%-10% value)",
            quotaTerms = "Transfer 1:2 to Accor, Singapore KrisFlyer, Qatar",
            redemptionMethod = "Book directly on airline/hotel official websites"
        ),
        CategoryPlatformOffer(
            cardId = "amex_platinum_travel",
            category = "travel",
            platform = "Taj, SeleQtions & Vivanta",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/American_Express_logo_%282018%29.svg/512px-American_Express_logo_%282018%29.svg.png",
            offerTitle = "₹10,000 Taj Stay Voucher",
            discountValue = "Complimentary Taj luxury hotel stay voucher on ₹4L annual spend",
            quotaTerms = "Delivered upon completing ₹4,00,000 milestone",
            redemptionMethod = "Redeem voucher at any Taj property across India"
        ),

        // === FUEL SURCHARGE WAIVER ===
        CategoryPlatformOffer(
            cardId = "hdfc_millennia",
            category = "fuel",
            platform = "All Petrol Pumps (BPCL, HPCL, IOCL)",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/HDFC_Bank_Logo.svg/512px-HDFC_Bank_Logo.svg.png",
            offerTitle = "1% Fuel Surcharge Waiver",
            discountValue = "1% surcharge waived on fuel purchases",
            quotaTerms = "Valid on ₹400 to ₹5,000 spend. Max ₹250/mo waiver",
            redemptionMethod = "Direct swipe at any fuel station POS machine"
        ),
        CategoryPlatformOffer(
            cardId = "axis_airtel",
            category = "fuel",
            platform = "All Fuel Outlets",
            platformLogoUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Axis_Bank_logo.svg/512px-Axis_Bank_logo.svg.png",
            offerTitle = "1% Fuel Surcharge Waiver",
            discountValue = "1% surcharge waived on fuel transactions",
            quotaTerms = "Valid on ₹400 to ₹4,000 spend. Max ₹400/mo waiver",
            redemptionMethod = "Swipe card at fuel pump POS"
        )
    )

    fun getOffersForCategory(category: String, userCardIds: Set<String>): List<Pair<Card, CategoryPlatformOffer>> {
        val matchingOffers = allPlatformOffers.filter { it.category.equals(category, ignoreCase = true) }

        return matchingOffers
            .filter { userCardIds.contains(it.cardId) }
            .mapNotNull { offer ->
                val card = cardRepo.getCardById(offer.cardId)
                if (card != null) Pair(card, offer) else null
            }
    }

    fun getAllOffersForCategory(category: String): List<Pair<Card, CategoryPlatformOffer>> {
        val matchingOffers = allPlatformOffers.filter { it.category.equals(category, ignoreCase = true) }

        return matchingOffers.mapNotNull { offer ->
            val card = cardRepo.getCardById(offer.cardId)
            if (card != null) Pair(card, offer) else null
        }
    }

    fun getOffersForCard(cardId: String): List<CategoryPlatformOffer> {
        return allPlatformOffers.filter { it.cardId == cardId }
    }
}
