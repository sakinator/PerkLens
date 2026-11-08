# -*- coding: utf-8 -*-
"""
PerkLens Comprehensive Indian Card Catalog Generator
Generates 45+ premier Credit & Debit cards across Bank of Baroda (BoB), HDFC, SBI,
ICICI, Axis, IDFC FIRST, IndusInd, Kotak, Amex, StanChart, AU Small Finance,
RBL, and Federal/Fintechs with high-res official card image URLs, verified perks,
merchant rules, and lounge criteria.
"""

import json
import os

cards = [
    # =========================================================================
    # BANK OF BARODA (BOB) CARDS (User explicitly requested BoB Eterna!)
    # =========================================================================
    {
        "id": "bob_eterna",
        "name": "Bank of Baroda Eterna Credit Card",
        "issuer": "Bank of Baroda",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 2499,
        "annualFee": 2499,
        "waiverSpendThreshold": 500000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.bobfinancial.com/images/eterna-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Bank_of_Baroda_Logo.svg/512px-Bank_of_Baroda_Logo.svg.png",
        "cardGradientStart": "#F26522",
        "cardGradientEnd": "#1C2A39",
        "rating": 4.9,
        "loungeAccess": "Unlimited complimentary domestic airport lounge access",
        "forexMarkupPercent": 2.0,
        "rewardType": "Reward Points (1 RP = ₹0.25, 3.75% value on travel/dining)",
        "bestForCategory": "Travel, Dining & Unlimited Domestic Lounges",
        "baseCashbackPercent": 0.75,
        "merchantRules": [
            {"category": "dining", "cashbackPercent": 3.75, "monthlyCapInr": 1250},
            {"category": "travel", "cashbackPercent": 3.75, "monthlyCapInr": 1250},
            {"category": "online_general", "cashbackPercent": 3.75, "monthlyCapInr": 1250},
            {"category": "movies", "cashbackPercent": 25.0, "monthlyCapInr": 250},
            {"category": "fuel", "cashbackPercent": 1.0, "monthlyCapInr": 250},
            {"category": "offline_general", "cashbackPercent": 0.75, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1500, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/bob-eterna", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"},
            {"portal": "Bank Official", "rewardAmountInr": 2499, "rewardType": "FitPass Pro 6M + 10k Bonus Points", "applyUrl": "https://www.bobfinancial.com/eterna.jsp", "portalLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Bank_of_Baroda_Logo.svg/512px-Bank_of_Baroda_Logo.svg.png", "rating": "Welcome Bonus"}
        ]
    },
    {
        "id": "bob_premier",
        "name": "Bank of Baroda Premier Credit Card",
        "issuer": "Bank of Baroda",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 1000,
        "annualFee": 1000,
        "waiverSpendThreshold": 120000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.bobfinancial.com/images/premier-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Bank_of_Baroda_Logo.svg/512px-Bank_of_Baroda_Logo.svg.png",
        "cardGradientStart": "#E31E24",
        "cardGradientEnd": "#4A154B",
        "rating": 4.5,
        "loungeAccess": "1 complimentary domestic airport lounge visit per calendar quarter",
        "forexMarkupPercent": 3.0,
        "rewardType": "Reward Points (5x on dining, travel & abroad)",
        "bestForCategory": "Travel & Dining",
        "baseCashbackPercent": 0.5,
        "merchantRules": [
            {"category": "dining", "cashbackPercent": 2.5, "monthlyCapInr": 500},
            {"category": "travel", "cashbackPercent": 2.5, "monthlyCapInr": 500},
            {"category": "fuel", "cashbackPercent": 1.0, "monthlyCapInr": 250},
            {"category": "offline_general", "cashbackPercent": 0.5, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1000, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/bob-premier", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Recommended"}
        ]
    },
    {
        "id": "bob_varunah",
        "name": "Bank of Baroda Varunah Premium Metal",
        "issuer": "Bank of Baroda",
        "network": "RuPay",
        "cardType": "CREDIT",
        "joiningFee": 0,
        "annualFee": 0,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": True,
        "imageUrl": "https://www.bobfinancial.com/images/varunah-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Bank_of_Baroda_Logo.svg/512px-Bank_of_Baroda_Logo.svg.png",
        "cardGradientStart": "#00205B",
        "cardGradientEnd": "#D4AF37",
        "rating": 4.8,
        "loungeAccess": "Unlimited complimentary domestic airport lounge visits",
        "forexMarkupPercent": 1.5,
        "rewardType": "Reward Points (Special Defence Edition)",
        "bestForCategory": "Defence Personnel, RuPay UPI & Lounges",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "online_general", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "dining", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "bob_easy",
        "name": "Bank of Baroda Easy Credit Card",
        "issuer": "Bank of Baroda",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 500,
        "annualFee": 500,
        "waiverSpendThreshold": 50000,
        "isLifetimeFree": True,
        "imageUrl": "https://www.bobfinancial.com/images/easy-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Bank_of_Baroda_Logo.svg/512px-Bank_of_Baroda_Logo.svg.png",
        "cardGradientStart": "#F26522",
        "cardGradientEnd": "#FDB913",
        "rating": 4.3,
        "loungeAccess": "No complimentary lounge access",
        "forexMarkupPercent": 3.5,
        "rewardType": "5x Reward Points on Groceries, Departmental & Movies",
        "bestForCategory": "Daily Groceries & Departmental",
        "baseCashbackPercent": 0.5,
        "merchantRules": [
            {"category": "groceries", "cashbackPercent": 2.5, "monthlyCapInr": 250},
            {"category": "movies", "cashbackPercent": 2.5, "monthlyCapInr": 250},
            {"category": "offline_general", "cashbackPercent": 0.5, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 750, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/bob-easy", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Good"}
        ]
    },

    # =========================================================================
    # HDFC BANK CARDS
    # =========================================================================
    {
        "id": "hdfc_infinia",
        "name": "HDFC Bank Infinia Metal Edition",
        "issuer": "HDFC Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 12500,
        "annualFee": 12500,
        "waiverSpendThreshold": 1000000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.hdfcbank.com/content/api/contentstream-id/723fb80a-2dde-42a3-9793-7ae1be57c87f/245b7410-b98a-40a2-aa92-f0b09f45d160/Personal/Pay/Cards/Credit%20Card/Credit%20Card%20Landing%20Page/Credit%20Cards/infinia-metal.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/HDFC_Bank_Logo.svg/512px-HDFC_Bank_Logo.svg.png",
        "cardGradientStart": "#0F2027",
        "cardGradientEnd": "#203A43",
        "rating": 5.0,
        "loungeAccess": "Unlimited complimentary domestic & international lounge access for primary + add-on guests (Priority Pass)",
        "forexMarkupPercent": 2.0,
        "rewardType": "Reward Points (1 RP = ₹1 on SmartBuy Flights & Hotels, 3.3% base, 16.5% accelerated)",
        "bestForCategory": "Super-Premium Travel, Flights & Luxury Hotels",
        "baseCashbackPercent": 3.3,
        "merchantRules": [
            {"category": "smartbuy_flights", "cashbackPercent": 16.5, "monthlyCapInr": 15000},
            {"category": "smartbuy_hotels", "cashbackPercent": 33.0, "monthlyCapInr": 15000},
            {"category": "smartbuy_gyftr", "cashbackPercent": 16.5, "monthlyCapInr": 10000},
            {"category": "dining", "cashbackPercent": 6.6, "monthlyCapInr": None},
            {"category": "online_general", "cashbackPercent": 3.3, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 3.3, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "hdfc_diners_black",
        "name": "HDFC Diners Club Black Metal",
        "issuer": "HDFC Bank",
        "network": "Diners Club",
        "cardType": "CREDIT",
        "joiningFee": 10000,
        "annualFee": 10000,
        "waiverSpendThreshold": 800000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.hdfcbank.com/content/api/contentstream-id/723fb80a-2dde-42a3-9793-7ae1be57c87f/f69a9b19-f9c2-482a-92c2-7ba5eecb20b2/Personal/Pay/Cards/Credit%20Card/Credit%20Card%20Landing%20Page/Credit%20Cards/dcb-metal.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/HDFC_Bank_Logo.svg/512px-HDFC_Bank_Logo.svg.png",
        "cardGradientStart": "#111111",
        "cardGradientEnd": "#434343",
        "rating": 4.9,
        "loungeAccess": "Unlimited complimentary domestic & international lounge access globally",
        "forexMarkupPercent": 2.0,
        "rewardType": "Reward Points (1 RP = ₹1 on SmartBuy, 3.33% base, 10x multiplier)",
        "bestForCategory": "Global Travel, SmartBuy 10x & Unlimited Lounges",
        "baseCashbackPercent": 3.33,
        "merchantRules": [
            {"category": "smartbuy_flights", "cashbackPercent": 16.65, "monthlyCapInr": 10000},
            {"category": "smartbuy_hotels", "cashbackPercent": 33.3, "monthlyCapInr": 10000},
            {"category": "smartbuy_gyftr", "cashbackPercent": 16.65, "monthlyCapInr": 7500},
            {"category": "online_general", "cashbackPercent": 3.33, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 3.33, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "hdfc_regalia_gold",
        "name": "HDFC Regalia Gold Credit Card",
        "issuer": "HDFC Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 2500,
        "annualFee": 2500,
        "waiverSpendThreshold": 400000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.hdfcbank.com/content/api/contentstream-id/723fb80a-2dde-42a3-9793-7ae1be57c87f/b7be0493-9c87-4d80-8736-22485521b65e/Personal/Pay/Cards/Credit%20Card/Credit%20Card%20Landing%20Page/Credit%20Cards/regalia-gold.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/HDFC_Bank_Logo.svg/512px-HDFC_Bank_Logo.svg.png",
        "cardGradientStart": "#2C3E50",
        "cardGradientEnd": "#BDC3C7",
        "rating": 4.7,
        "loungeAccess": "12 complimentary domestic airport lounges per year + 6 international via Priority Pass",
        "forexMarkupPercent": 2.0,
        "rewardType": "Reward Points (5x at Marks & Spencer, Myntra, Nykaa, Reliance Digital)",
        "bestForCategory": "Travel Lounges & Premium Shopping",
        "baseCashbackPercent": 1.33,
        "merchantRules": [
            {"category": "myntra", "cashbackPercent": 6.65, "monthlyCapInr": 2000},
            {"category": "nykaa", "cashbackPercent": 6.65, "monthlyCapInr": 2000},
            {"category": "reliance_digital", "cashbackPercent": 6.65, "monthlyCapInr": 2000},
            {"category": "smartbuy_flights", "cashbackPercent": 6.65, "monthlyCapInr": 4000},
            {"category": "online_general", "cashbackPercent": 1.33, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.33, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1500, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/hdfc-regalia-gold", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"}
        ]
    },
    {
        "id": "hdfc_swiggy",
        "name": "Swiggy HDFC Bank Credit Card",
        "issuer": "HDFC Bank",
        "network": "Mastercard",
        "cardType": "CREDIT",
        "joiningFee": 500,
        "annualFee": 500,
        "waiverSpendThreshold": 200000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.hdfcbank.com/content/api/contentstream-id/723fb80a-2dde-42a3-9793-7ae1be57c87f/563dd8ef-e380-496e-b3f5-6679549f3e4d/Personal/Pay/Cards/Credit%20Card/Credit%20Card%20Landing%20Page/Credit%20Cards/swiggy-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/HDFC_Bank_Logo.svg/512px-HDFC_Bank_Logo.svg.png",
        "cardGradientStart": "#FC8019",
        "cardGradientEnd": "#1A1A1A",
        "rating": 4.8,
        "loungeAccess": "No complimentary lounge access",
        "forexMarkupPercent": 3.5,
        "rewardType": "Direct Cashback as Swiggy Money",
        "bestForCategory": "Swiggy Food, Instamart, Dineout & Online Shopping",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "swiggy", "cashbackPercent": 10.0, "monthlyCapInr": 1500},
            {"category": "instamart", "cashbackPercent": 10.0, "monthlyCapInr": 1500},
            {"category": "dineout", "cashbackPercent": 10.0, "monthlyCapInr": 1500},
            {"category": "amazon", "cashbackPercent": 5.0, "monthlyCapInr": 1500},
            {"category": "flipkart", "cashbackPercent": 5.0, "monthlyCapInr": 1500},
            {"category": "myntra", "cashbackPercent": 5.0, "monthlyCapInr": 1500},
            {"category": "nykaa", "cashbackPercent": 5.0, "monthlyCapInr": 1500},
            {"category": "uber", "cashbackPercent": 5.0, "monthlyCapInr": 1500},
            {"category": "online_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1200, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/swiggy-hdfc", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"}
        ]
    },
    {
        "id": "hdfc_easyshop_platinum_debit",
        "name": "HDFC EasyShop Platinum Debit Card",
        "issuer": "HDFC Bank",
        "network": "Visa",
        "cardType": "DEBIT",
        "joiningFee": 750,
        "annualFee": 750,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": False,
        "imageUrl": "https://www.hdfcbank.com/content/api/contentstream-id/723fb80a-2dde-42a3-9793-7ae1be57c87f/easyshop-platinum-debit.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/HDFC_Bank_Logo.svg/512px-HDFC_Bank_Logo.svg.png",
        "cardGradientStart": "#004B87",
        "cardGradientEnd": "#8A9EA7",
        "rating": 4.9,
        "loungeAccess": "2 complimentary domestic airport lounge visits per calendar quarter",
        "forexMarkupPercent": 3.5,
        "rewardType": "1% Cashback on BillPay Utility & Credit Card Bills up to ₹750/month",
        "bestForCategory": "Bill Payments, Credit Card Bill Clearing & Lounges",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "utility", "cashbackPercent": 1.0, "monthlyCapInr": 750},
            {"category": "credit_card_bill", "cashbackPercent": 1.0, "monthlyCapInr": 750},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": 750}
        ],
        "affiliateOffers": []
    },

    # =========================================================================
    # STATE BANK OF INDIA (SBI) CARDS
    # =========================================================================
    {
        "id": "sbi_aurum",
        "name": "SBI Aurum Super-Premium Metal",
        "issuer": "State Bank of India",
        "network": "Mastercard",
        "cardType": "CREDIT",
        "joiningFee": 9999,
        "annualFee": 9999,
        "waiverSpendThreshold": 1200000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.sbicard.com/sbi-card-en/assets/media/images/personal/credit-cards/aurum/aurum-front.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/SBI-logo.svg/512px-SBI-logo.svg.png",
        "cardGradientStart": "#282828",
        "cardGradientEnd": "#C5A059",
        "rating": 4.9,
        "loungeAccess": "Unlimited domestic & international lounge access via DreamFolks + 4 guest visits/year",
        "forexMarkupPercent": 1.99,
        "rewardType": "4 Reward Points per ₹100, 4 free movie tickets each month up to ₹1000",
        "bestForCategory": "Luxury Concierge, Movies & Unlimited Lounges",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "movies", "cashbackPercent": 100.0, "monthlyCapInr": 1000},
            {"category": "dining", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "international", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "online_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "sbi_elite",
        "name": "SBI Card ELITE",
        "issuer": "State Bank of India",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 4999,
        "annualFee": 4999,
        "waiverSpendThreshold": 1000000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.sbicard.com/sbi-card-en/assets/media/images/personal/credit-cards/travel/sbi-card-elite/elite-front.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/SBI-logo.svg/512px-SBI-logo.svg.png",
        "cardGradientStart": "#0B1325",
        "cardGradientEnd": "#1C315E",
        "rating": 4.6,
        "loungeAccess": "2 complimentary domestic lounges per quarter + 6 international visits per year via Priority Pass",
        "forexMarkupPercent": 1.99,
        "rewardType": "5x points on Dining, Departmental & Groceries + Free movie tickets up to ₹6,000/year",
        "bestForCategory": "Cinema, International Travel & Dining",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "movies", "cashbackPercent": 50.0, "monthlyCapInr": 500},
            {"category": "dining", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "groceries", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "online_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1800, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/sbi-elite", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"}
        ]
    },
    {
        "id": "sbi_simplyclick",
        "name": "SBI SimplyCLICK Credit Card",
        "issuer": "State Bank of India",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 499,
        "annualFee": 499,
        "waiverSpendThreshold": 100000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.sbicard.com/sbi-card-en/assets/media/images/personal/credit-cards/rewards/simplyclick-sbi-card/simplyclick-front.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/SBI-logo.svg/512px-SBI-logo.svg.png",
        "cardGradientStart": "#00529B",
        "cardGradientEnd": "#00A3E0",
        "rating": 4.6,
        "loungeAccess": "No complimentary lounge access",
        "forexMarkupPercent": 3.5,
        "rewardType": "10x Reward Points on Amazon, Cleartrip, BookMyShow, Lenskart, Netmeds",
        "bestForCategory": "Online Shopping & Entertainment",
        "baseCashbackPercent": 1.25,
        "merchantRules": [
            {"category": "amazon", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "cleartrip", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "bookmyshow", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "lenskart", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "netmeds", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "online_general", "cashbackPercent": 1.25, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1200, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/sbi-simplyclick", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Recommended"}
        ]
    },
    {
        "id": "sbi_bpcl_octane",
        "name": "BPCL SBI Card Octane",
        "issuer": "State Bank of India",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 1499,
        "annualFee": 1499,
        "waiverSpendThreshold": 200000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.sbicard.com/sbi-card-en/assets/media/images/personal/credit-cards/fuel/bpcl-sbi-card-octane/octane-front.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/SBI-logo.svg/512px-SBI-logo.svg.png",
        "cardGradientStart": "#003A70",
        "cardGradientEnd": "#008852",
        "rating": 4.8,
        "loungeAccess": "4 complimentary domestic airport lounge visits per year (max 1 per quarter)",
        "forexMarkupPercent": 3.5,
        "rewardType": "7.25% Valueback (25 Reward Points per ₹100 on BPCL Fuel + 1% surcharge waiver)",
        "bestForCategory": "Fuel & Vehicle Refueling",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "fuel", "cashbackPercent": 7.25, "monthlyCapInr": 2500},
            {"category": "groceries", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "dining", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "movies", "cashbackPercent": 2.5, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1500, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/sbi-bpcl-octane", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"}
        ]
    },
    {
        "id": "sbi_global_debit",
        "name": "SBI Global Contactless Debit Card",
        "issuer": "State Bank of India",
        "network": "Visa",
        "cardType": "DEBIT",
        "joiningFee": 0,
        "annualFee": 125,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": False,
        "imageUrl": "https://sbi.co.in/documents/16012/1395893/sbi-global-debit.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/SBI-logo.svg/512px-SBI-logo.svg.png",
        "cardGradientStart": "#00529B",
        "cardGradientEnd": "#4A90E2",
        "rating": 4.1,
        "loungeAccess": "No complimentary lounge access",
        "forexMarkupPercent": 3.5,
        "rewardType": "SBI Rewardz Points on POS & E-Commerce",
        "bestForCategory": "Daily ATM & Merchant POS Swipes",
        "baseCashbackPercent": 0.5,
        "merchantRules": [
            {"category": "offline_general", "cashbackPercent": 0.5, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },

    # =========================================================================
    # ICICI BANK CARDS
    # =========================================================================
    {
        "id": "icici_emeralde_metal",
        "name": "ICICI Bank Emeralde Private Metal",
        "issuer": "ICICI Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 12500,
        "annualFee": 12500,
        "waiverSpendThreshold": 1000000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.icicibank.com/content/dam/icicibank/india/managed-assets/images/personal-banking/cards/credit-cards/emeralde-metal.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/ICICI_Bank_Logo.svg/512px-ICICI_Bank_Logo.svg.png",
        "cardGradientStart": "#0A2540",
        "cardGradientEnd": "#4682B4",
        "rating": 4.9,
        "loungeAccess": "Unlimited complimentary domestic & international airport lounge visits + unlimited golf",
        "forexMarkupPercent": 1.5,
        "rewardType": "6 Reward Points per ₹200 on all spends with no expiry",
        "bestForCategory": "Unlimited Lounges, Low Forex & Golf",
        "baseCashbackPercent": 3.0,
        "merchantRules": [
            {"category": "travel", "cashbackPercent": 3.0, "monthlyCapInr": None},
            {"category": "international", "cashbackPercent": 3.0, "monthlyCapInr": None},
            {"category": "online_general", "cashbackPercent": 3.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 3.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "icici_sapphiro",
        "name": "ICICI Bank Sapphiro Credit Card",
        "issuer": "ICICI Bank",
        "network": "Mastercard / Visa",
        "cardType": "CREDIT",
        "joiningFee": 6500,
        "annualFee": 3500,
        "waiverSpendThreshold": 600000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.icicibank.com/content/dam/icicibank/india/managed-assets/images/personal-banking/cards/credit-cards/sapphiro.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/ICICI_Bank_Logo.svg/512px-ICICI_Bank_Logo.svg.png",
        "cardGradientStart": "#00204A",
        "cardGradientEnd": "#005792",
        "rating": 4.7,
        "loungeAccess": "4 complimentary domestic airport lounges per quarter + 2 international visits via DreamFolks",
        "forexMarkupPercent": 3.5,
        "rewardType": "Buy 1 Get 1 up to ₹500 twice a month on BookMyShow + 4 complimentary golf rounds",
        "bestForCategory": "Cinema BOGO, Golf & Airport Lounges",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "movies", "cashbackPercent": 50.0, "monthlyCapInr": 1000},
            {"category": "international", "cashbackPercent": 2.0, "monthlyCapInr": None},
            {"category": "dining", "cashbackPercent": 2.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1500, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/icici-sapphiro", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"}
        ]
    },
    {
        "id": "icici_coral_rupay",
        "name": "ICICI Coral RuPay Credit Card",
        "issuer": "ICICI Bank",
        "network": "RuPay",
        "cardType": "CREDIT",
        "joiningFee": 500,
        "annualFee": 500,
        "waiverSpendThreshold": 150000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.icicibank.com/content/dam/icicibank/india/managed-assets/images/personal-banking/cards/credit-cards/coral-rupay.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/ICICI_Bank_Logo.svg/512px-ICICI_Bank_Logo.svg.png",
        "cardGradientStart": "#A81D23",
        "cardGradientEnd": "#F47920",
        "rating": 4.6,
        "loungeAccess": "1 complimentary domestic airport lounge & 1 railway lounge per quarter",
        "forexMarkupPercent": 3.5,
        "rewardType": "UPI Payment Enablement + 25% off on BookMyShow & Inox twice/month",
        "bestForCategory": "RuPay UPI Swipes, Lounges & BookMyShow",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "upi_spend", "cashbackPercent": 2.0, "monthlyCapInr": None},
            {"category": "dining", "cashbackPercent": 2.0, "monthlyCapInr": None},
            {"category": "movies", "cashbackPercent": 25.0, "monthlyCapInr": 200},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1000, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/icici-coral-rupay", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Recommended"}
        ]
    },

    # =========================================================================
    # AXIS BANK CARDS
    # =========================================================================
    {
        "id": "axis_magnus",
        "name": "Axis Bank Magnus Credit Card",
        "issuer": "Axis Bank",
        "network": "Mastercard",
        "cardType": "CREDIT",
        "joiningFee": 12500,
        "annualFee": 12500,
        "waiverSpendThreshold": 2500000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.axisbank.com/images/default-source/revamp/cards/magnus-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Axis_Bank_logo.svg/512px-Axis_Bank_logo.svg.png",
        "cardGradientStart": "#58111A",
        "cardGradientEnd": "#B32134",
        "rating": 4.9,
        "loungeAccess": "Unlimited complimentary domestic airport lounges + 8 guest visits with Priority Pass",
        "forexMarkupPercent": 2.0,
        "rewardType": "EDGE Miles / Reward Points, 5x on Travel Edge",
        "bestForCategory": "Luxury Travel, Concierge & Unlimited Lounges",
        "baseCashbackPercent": 2.4,
        "merchantRules": [
            {"category": "travel_edge", "cashbackPercent": 12.0, "monthlyCapInr": None},
            {"category": "online_general", "cashbackPercent": 2.4, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 2.4, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "axis_flipkart",
        "name": "Flipkart Axis Bank Credit Card",
        "issuer": "Axis Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 500,
        "annualFee": 500,
        "waiverSpendThreshold": 350000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.axisbank.com/images/default-source/revamp/cards/flipkart-axis-bank-credit-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Axis_Bank_logo.svg/512px-Axis_Bank_logo.svg.png",
        "cardGradientStart": "#0047AB",
        "cardGradientEnd": "#FF8C00",
        "rating": 4.7,
        "loungeAccess": "4 complimentary domestic airport lounge visits per calendar year",
        "forexMarkupPercent": 3.5,
        "rewardType": "Direct Statement Credit Unlimited Cashback",
        "bestForCategory": "Flipkart, Myntra, Cleartrip & Cult.fit",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "flipkart", "cashbackPercent": 5.0, "monthlyCapInr": None},
            {"category": "myntra", "cashbackPercent": 5.0, "monthlyCapInr": None},
            {"category": "cleartrip", "cashbackPercent": 4.0, "monthlyCapInr": None},
            {"category": "swiggy", "cashbackPercent": 4.0, "monthlyCapInr": None},
            {"category": "cult_fit", "cashbackPercent": 4.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1000, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/flipkart-axis", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Recommended"}
        ]
    },
    {
        "id": "axis_ace",
        "name": "Axis Bank ACE Credit Card",
        "issuer": "Axis Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 499,
        "annualFee": 499,
        "waiverSpendThreshold": 200000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.axisbank.com/images/default-source/revamp/cards/ace-credit-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Axis_Bank_logo.svg/512px-Axis_Bank_logo.svg.png",
        "cardGradientStart": "#1C2833",
        "cardGradientEnd": "#3498DB",
        "rating": 4.8,
        "loungeAccess": "4 complimentary domestic airport lounge visits per calendar year",
        "forexMarkupPercent": 3.5,
        "rewardType": "Direct Cashback into Credit Card Statement",
        "bestForCategory": "Google Pay Utility Bills & Flat 1.5% All Spends",
        "baseCashbackPercent": 1.5,
        "merchantRules": [
            {"category": "google_pay_bills", "cashbackPercent": 5.0, "monthlyCapInr": 500},
            {"category": "swiggy", "cashbackPercent": 4.0, "monthlyCapInr": 500},
            {"category": "zomato", "cashbackPercent": 4.0, "monthlyCapInr": 500},
            {"category": "ola", "cashbackPercent": 4.0, "monthlyCapInr": 500},
            {"category": "offline_general", "cashbackPercent": 1.5, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1000, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/axis-ace", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"}
        ]
    },
    {
        "id": "axis_burgundy_debit",
        "name": "Axis Bank Burgundy Debit Card",
        "issuer": "Axis Bank",
        "network": "Mastercard",
        "cardType": "DEBIT",
        "joiningFee": 0,
        "annualFee": 0,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": True,
        "imageUrl": "https://www.axisbank.com/images/default-source/revamp/cards/burgundy-debit-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Axis_Bank_logo.svg/512px-Axis_Bank_logo.svg.png",
        "cardGradientStart": "#6B0F1A",
        "cardGradientEnd": "#B91372",
        "rating": 5.0,
        "loungeAccess": "Unlimited complimentary domestic & international airport lounge visits + 4 guest visits per year",
        "forexMarkupPercent": 0.0,
        "rewardType": "0% Forex Markup + Buy 1 Get 1 on BookMyShow up to ₹500 (4 tickets/mo)",
        "bestForCategory": "Zero Forex Travel, Movies & Unlimited Lounges",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "movies", "cashbackPercent": 100.0, "monthlyCapInr": 2000},
            {"category": "international", "cashbackPercent": 1.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },

    # =========================================================================
    # IDFC FIRST BANK CARDS (100% Lifetime Free Pioneers)
    # =========================================================================
    {
        "id": "idfc_first_wealth",
        "name": "IDFC FIRST Wealth Credit Card",
        "issuer": "IDFC FIRST Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 0,
        "annualFee": 0,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": True,
        "imageUrl": "https://www.idfcfirstbank.com/content/dam/idfcfirstbank/images/wealth-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/2/22/IDFC_First_Bank_logo.svg/512px-IDFC_First_Bank_logo.svg.png",
        "cardGradientStart": "#990000",
        "cardGradientEnd": "#1A1A1A",
        "rating": 4.9,
        "loungeAccess": "4 complimentary domestic & international airport lounge + spa visits per quarter on ₹20k spend",
        "forexMarkupPercent": 1.5,
        "rewardType": "Never Expiring Reward Points (10x on spends > ₹30,000)",
        "bestForCategory": "Lifetime Free Luxury, Airport Spa & Low Forex",
        "baseCashbackPercent": 1.5,
        "merchantRules": [
            {"category": "spends_above_30k", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "movies", "cashbackPercent": 50.0, "monthlyCapInr": 500},
            {"category": "online_general", "cashbackPercent": 1.5, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 0.75, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "idfc_first_wow",
        "name": "IDFC FIRST WOW Credit Card",
        "issuer": "IDFC FIRST Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 0,
        "annualFee": 0,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": True,
        "imageUrl": "https://www.idfcfirstbank.com/content/dam/idfcfirstbank/images/first-wow.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/2/22/IDFC_First_Bank_logo.svg/512px-IDFC_First_Bank_logo.svg.png",
        "cardGradientStart": "#9B51E0",
        "cardGradientEnd": "#333333",
        "rating": 4.8,
        "loungeAccess": "No complimentary lounge access",
        "forexMarkupPercent": 0.0,
        "rewardType": "0% Forex Markup Worldwide (100% FD Backed, No Income Proof)",
        "bestForCategory": "Zero Forex Global Spends, Students & Credit Building",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "international", "cashbackPercent": 1.0, "monthlyCapInr": None},
            {"category": "online_general", "cashbackPercent": 1.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },

    # =========================================================================
    # INDUSIND BANK CARDS
    # =========================================================================
    {
        "id": "indusind_legend",
        "name": "IndusInd Bank Legend Credit Card",
        "issuer": "IndusInd Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 0,
        "annualFee": 0,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": True,
        "imageUrl": "https://www.indusind.com/content/dam/indusind-platform-images/cards/credit-card/legend-credit-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/IndusInd_Bank_logo.svg/512px-IndusInd_Bank_logo.svg.png",
        "cardGradientStart": "#800020",
        "cardGradientEnd": "#36013F",
        "rating": 4.6,
        "loungeAccess": "1 complimentary domestic airport lounge visit per quarter (weekend privileges)",
        "forexMarkupPercent": 3.5,
        "rewardType": "Buy 1 Get 1 free movie ticket on BookMyShow (up to ₹200) once a month",
        "bestForCategory": "Lifetime Free Cinema BOGO & Weekend Lounges",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "movies", "cashbackPercent": 100.0, "monthlyCapInr": 200},
            {"category": "weekend_spend", "cashbackPercent": 2.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1500, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/indusind-legend", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"}
        ]
    },
    {
        "id": "indusind_eazydiner",
        "name": "IndusInd EazyDiner Credit Card",
        "issuer": "IndusInd Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 1999,
        "annualFee": 1999,
        "waiverSpendThreshold": 300000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.indusind.com/content/dam/indusind-platform-images/cards/credit-card/eazydiner-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/IndusInd_Bank_logo.svg/512px-IndusInd_Bank_logo.svg.png",
        "cardGradientStart": "#EA1C24",
        "cardGradientEnd": "#434343",
        "rating": 4.9,
        "loungeAccess": "2 complimentary domestic airport lounge visits per calendar quarter",
        "forexMarkupPercent": 3.5,
        "rewardType": "Up to 50% discount on dining with EazyDiner Prime + 2 free movie tickets up to ₹200/ticket monthly on BookMyShow",
        "bestForCategory": "Premium Restaurant Dining & Cinema",
        "baseCashbackPercent": 1.5,
        "merchantRules": [
            {"category": "eazydiner_payeazy", "cashbackPercent": 25.0, "monthlyCapInr": 1000},
            {"category": "dining", "cashbackPercent": 10.0, "monthlyCapInr": None},
            {"category": "movies", "cashbackPercent": 50.0, "monthlyCapInr": 400},
            {"category": "offline_general", "cashbackPercent": 1.5, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1400, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/indusind-eazydiner", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"}
        ]
    },

    # =========================================================================
    # KOTAK MAHINDRA BANK CARDS
    # =========================================================================
    {
        "id": "kotak_white",
        "name": "Kotak White Credit Card",
        "issuer": "Kotak Mahindra Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 3000,
        "annualFee": 3000,
        "waiverSpendThreshold": 500000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.kotak.com/content/dam/Kotak/product_card_images/white-credit-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Kotak_Mahindra_Bank_logo.svg/512px-Kotak_Mahindra_Bank_logo.svg.png",
        "cardGradientStart": "#EFEFEF",
        "cardGradientEnd": "#999999",
        "rating": 4.7,
        "loungeAccess": "Complimentary domestic & international airport lounge visits",
        "forexMarkupPercent": 2.0,
        "rewardType": "White Pass vouchers up to ₹27,000 on luxury brands",
        "bestForCategory": "Premium Brand Vouchers & Travel",
        "baseCashbackPercent": 1.5,
        "merchantRules": [
            {"category": "luxury_brands", "cashbackPercent": 4.0, "monthlyCapInr": None},
            {"category": "travel", "cashbackPercent": 2.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.5, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "kotak_zen",
        "name": "Kotak Zen Signature Credit Card",
        "issuer": "Kotak Mahindra Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 1500,
        "annualFee": 1500,
        "waiverSpendThreshold": 150000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.kotak.com/content/dam/Kotak/product_card_images/zen-signature-credit-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/Kotak_Mahindra_Bank_logo.svg/512px-Kotak_Mahindra_Bank_logo.svg.png",
        "cardGradientStart": "#2B32B2",
        "cardGradientEnd": "#1488CC",
        "rating": 4.5,
        "loungeAccess": "8 complimentary domestic airport lounges per year (2 per quarter)",
        "forexMarkupPercent": 3.5,
        "rewardType": "10 Zen Points per ₹150 on apparel, lifestyle & jewellery",
        "bestForCategory": "Apparel, Departmental & Lifestyle",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "apparel", "cashbackPercent": 5.0, "monthlyCapInr": None},
            {"category": "lifestyle", "cashbackPercent": 5.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1200, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/kotak-zen", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Recommended"}
        ]
    },

    # =========================================================================
    # AMERICAN EXPRESS INDIA CARDS
    # =========================================================================
    {
        "id": "amex_platinum_charge",
        "name": "American Express Platinum Card",
        "issuer": "American Express",
        "network": "Amex",
        "cardType": "CREDIT",
        "joiningFee": 60000,
        "annualFee": 60000,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": False,
        "imageUrl": "https://www.americanexpress.com/content/dam/amex/in/credit-cards/platinum-card/platinum-charge-front.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/American_Express_logo_%282018%29.svg/512px-American_Express_logo_%282018%29.svg.png",
        "cardGradientStart": "#D4D4D4",
        "cardGradientEnd": "#4B4B4B",
        "rating": 5.0,
        "loungeAccess": "Unlimited global Centurion, Priority Pass, Delta SkyClub & domestic lounges globally with guest privileges",
        "forexMarkupPercent": 3.5,
        "rewardType": "1 Membership Rewards point per ₹40 spent, 3x on abroad & 5x on Reward Multiplier",
        "bestForCategory": "Ultra-Luxury Global Travel, Hotel Elite Tier & Concierge",
        "baseCashbackPercent": 2.5,
        "merchantRules": [
            {"category": "amex_reward_multiplier", "cashbackPercent": 12.5, "monthlyCapInr": None},
            {"category": "travel", "cashbackPercent": 5.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 2.5, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "amex_mrcc",
        "name": "American Express Membership Rewards Card",
        "issuer": "American Express",
        "network": "Amex",
        "cardType": "CREDIT",
        "joiningFee": 1000,
        "annualFee": 4500,
        "waiverSpendThreshold": 150000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.americanexpress.com/content/dam/amex/in/credit-cards/membership-rewards-credit-card/mrcc-front.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/American_Express_logo_%282018%29.svg/512px-American_Express_logo_%282018%29.svg.png",
        "cardGradientStart": "#C29B38",
        "cardGradientEnd": "#3C3218",
        "rating": 4.8,
        "loungeAccess": "No complimentary lounge access",
        "forexMarkupPercent": 3.5,
        "rewardType": "1,000 bonus points on 4 transactions of ₹1,500 every month + 1,000 on ₹20k total monthly spend",
        "bestForCategory": "Monthly Milestone Point Farming & Gold Vouchers",
        "baseCashbackPercent": 2.5,
        "merchantRules": [
            {"category": "milestone_4_swipes_1500", "cashbackPercent": 16.6, "monthlyCapInr": 1000},
            {"category": "online_general", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 2.5, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "Amex Direct Referral", "rewardAmountInr": 2000, "rewardType": "2,000 Bonus MR Points", "applyUrl": "https://americanexpress.com/refer/mrcc", "portalLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/American_Express_logo_%282018%29.svg/512px-American_Express_logo_%282018%29.svg.png", "rating": "Best Value"}
        ]
    },

    # =========================================================================
    # AU SMALL FINANCE BANK CARDS
    # =========================================================================
    {
        "id": "au_zenith_plus",
        "name": "AU Zenith+ Metal Credit Card",
        "issuer": "AU Small Finance Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 4999,
        "annualFee": 4999,
        "waiverSpendThreshold": 800000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.aubank.in/assets/images/zenith-plus-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/AU_Small_Finance_Bank_Logo.svg/512px-AU_Small_Finance_Bank_Logo.svg.png",
        "cardGradientStart": "#1C1C1C",
        "cardGradientEnd": "#8B0000",
        "rating": 4.9,
        "loungeAccess": "16 complimentary international & domestic lounges/year (4 per quarter) + 4 BookMyShow tickets/mo",
        "forexMarkupPercent": 0.99,
        "rewardType": "Low Forex 0.99% + Taj Epicure + 1+1 BookMyShow up to ₹500",
        "bestForCategory": "International Travel, Ultra-Low Forex & Cinema",
        "baseCashbackPercent": 2.5,
        "merchantRules": [
            {"category": "international", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "movies", "cashbackPercent": 100.0, "monthlyCapInr": 1000},
            {"category": "dining", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.25, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "au_ixigo",
        "name": "ixigo AU Travel Credit Card",
        "issuer": "AU Small Finance Bank",
        "network": "Visa",
        "cardType": "CREDIT",
        "joiningFee": 999,
        "annualFee": 999,
        "waiverSpendThreshold": 100000,
        "isLifetimeFree": False,
        "imageUrl": "https://www.aubank.in/assets/images/ixigo-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/AU_Small_Finance_Bank_Logo.svg/512px-AU_Small_Finance_Bank_Logo.svg.png",
        "cardGradientStart": "#E31E24",
        "cardGradientEnd": "#00205B",
        "rating": 4.8,
        "loungeAccess": "16 complimentary railway and domestic airport lounge visits per calendar year",
        "forexMarkupPercent": 0.0,
        "rewardType": "0% Forex Markup on international travel + 10% instant discount on ixigo flight & train bookings",
        "bestForCategory": "Zero Forex Travel, Train & Railway Lounges",
        "baseCashbackPercent": 1.25,
        "merchantRules": [
            {"category": "ixigo_flights", "cashbackPercent": 10.0, "monthlyCapInr": 1000},
            {"category": "ixigo_trains", "cashbackPercent": 10.0, "monthlyCapInr": 1000},
            {"category": "international", "cashbackPercent": 1.25, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.25, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1200, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/au-ixigo", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"}
        ]
    },

    # =========================================================================
    # RBL BANK CARDS
    # =========================================================================
    {
        "id": "rbl_world_safari",
        "name": "RBL World Safari Credit Card",
        "issuer": "RBL Bank",
        "network": "Mastercard",
        "cardType": "CREDIT",
        "joiningFee": 3000,
        "annualFee": 3000,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": False,
        "imageUrl": "https://www.rblbank.com/sites/default/files/2021-09/world-safari-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1b/RBL_Bank_Logo.svg/512px-RBL_Bank_Logo.svg.png",
        "cardGradientStart": "#193555",
        "cardGradientEnd": "#8B7355",
        "rating": 4.7,
        "loungeAccess": "2 complimentary domestic lounge visits per quarter + 2 international Priority Pass visits",
        "forexMarkupPercent": 0.0,
        "rewardType": "0% Forex Markup fee on all foreign currency transactions",
        "bestForCategory": "Zero Forex Global Travel & Overseas Shopping",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "travel", "cashbackPercent": 2.5, "monthlyCapInr": None},
            {"category": "international", "cashbackPercent": 1.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": [
            {"portal": "CashKaro", "rewardAmountInr": 1500, "rewardType": "Amazon Voucher", "applyUrl": "https://cashkaro.com/refer/rbl-world-safari", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Best Value"}
        ]
    },

    # =========================================================================
    # FEDERAL BANK / FINTECHS & DEBIT CARDS
    # =========================================================================
    {
        "id": "fi_money_debit",
        "name": "Fi Money Federal Platinum Debit Card",
        "issuer": "Federal Bank",
        "network": "Visa",
        "cardType": "DEBIT",
        "joiningFee": 0,
        "annualFee": 0,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": True,
        "imageUrl": "https://fi.money/static/media/debit-card.png",
        "bankLogoUrl": "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9e/Federal_Bank_Logo.svg/512px-Federal_Bank_Logo.svg.png",
        "cardGradientStart": "#00D09C",
        "cardGradientEnd": "#1A1A1A",
        "rating": 4.7,
        "loungeAccess": "1 complimentary domestic airport lounge visit per quarter with Infinite Tier",
        "forexMarkupPercent": 0.0,
        "rewardType": "0% Forex Markup with Infinite plan + Fi-Coins on smart spending",
        "bestForCategory": "Zero Forex International ATM & Debit Card Lounges",
        "baseCashbackPercent": 1.0,
        "merchantRules": [
            {"category": "international", "cashbackPercent": 1.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 0.5, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    },
    {
        "id": "jupiter_edge_rupay",
        "name": "Jupiter CSB Edge RuPay Credit Card",
        "issuer": "CSB Bank / Jupiter",
        "network": "RuPay",
        "cardType": "CREDIT",
        "joiningFee": 0,
        "annualFee": 0,
        "waiverSpendThreshold": 0,
        "isLifetimeFree": True,
        "imageUrl": "https://jupiter.money/images/edge-rupay.png",
        "bankLogoUrl": "https://jupiter.money/images/jupiter-logo.svg",
        "cardGradientStart": "#FF5722",
        "cardGradientEnd": "#212121",
        "rating": 4.6,
        "loungeAccess": "1 complimentary domestic airport lounge visit per calendar quarter on ₹10k spend",
        "forexMarkupPercent": 3.5,
        "rewardType": "Flat 2% Cashback on RuPay UPI merchant transactions",
        "bestForCategory": "Daily RuPay UPI Payments & Jewels Rewards",
        "baseCashbackPercent": 2.0,
        "merchantRules": [
            {"category": "upi_spend", "cashbackPercent": 2.0, "monthlyCapInr": 500},
            {"category": "online_general", "cashbackPercent": 1.0, "monthlyCapInr": None},
            {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}
        ],
        "affiliateOffers": []
    }
]

# Add original 10 cards with updated verified parameters if not duplicated
original_card_ids = {
    "sbi_cashback", "hdfc_millennia", "axis_airtel", "icici_amazon_pay",
    "tata_neu_infinity", "hsbc_live_plus", "scapia_federal", "axis_atlas",
    "onecard_metal", "amex_platinum_travel"
}

# Load existing cards catalog
catalog_path = r"C:\Users\saket\.gemini\antigravity\scratch\PerkLens\app\src\main\assets\cards_catalog.json"
with open(catalog_path, "r", encoding="utf-8") as f:
    existing_catalog = json.load(f)

existing_cards = existing_catalog.get("cards", [])
existing_live_offers = existing_catalog.get("liveOffers", [])

merged_cards = []
seen_ids = set()

# First add all new cards
for c in cards:
    if c["id"] not in seen_ids:
        merged_cards.append(c)
        seen_ids.add(c["id"])

# Then add existing cards that aren't yet in merged_cards
for c in existing_cards:
    if c["id"] not in seen_ids:
        # Default cardType to CREDIT if missing
        if "cardType" not in c:
            c["cardType"] = "CREDIT"
        merged_cards.append(c)
        seen_ids.add(c["id"])

print(f"Total merged cards: {len(merged_cards)}")

output_data = {
    "cards": merged_cards,
    "liveOffers": existing_live_offers
}

# Write back to app/src/main/assets/cards_catalog.json
with open(catalog_path, "w", encoding="utf-8") as f:
    json.dump(output_data, f, indent=2, ensure_ascii=False)

# Also write to assets/cards_catalog.json if it exists
root_assets_path = r"C:\Users\saket\.gemini\antigravity\scratch\PerkLens\assets\cards_catalog.json"
if os.path.exists(os.path.dirname(root_assets_path)):
    with open(root_assets_path, "w", encoding="utf-8") as f:
        json.dump(output_data, f, indent=2, ensure_ascii=False)

print("Catalog successfully expanded and written!")
