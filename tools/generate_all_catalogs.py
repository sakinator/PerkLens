# -*- coding: utf-8 -*-
"""
PerkLens Comprehensive Indian Banking Catalog Generator (162 Cards & 35 Lounges)
Exhaustively covers every Credit & Debit Card across:
- Bank of Baroda (BoB) - 14 cards (Eterna, Premier, Select, Easy, Varunah RuPay, ConQR, Swavlamban, IRCTC, HPCL, Snapdeal, Vikram, Yoddha, Platinum Debit)
- State Bank of India (SBI) - 17 cards (Aurum, Elite, Prime, Pulse, Cashback, SimplyCLICK, SimplySAVE, BPCL, BPCL Octane, Air India, Vistara, IRCTC, Paytm, Reliance, Global Debit, Platinum Debit)
- HDFC Bank - 17 cards (Infinia, Diners Black, Diners Privilege, Regalia Gold, Millennia, Tata Neu Infinity/Plus, Swiggy, Marriott Bonvoy, Shoppers Stop, IOCL, Freedom, MoneyBack+, Pixel Play, EasyShop Platinum Debit, Millennia Debit, Imperia Debit)
- ICICI Bank - 13 cards (Emeralde Private, Emeralde, Sapphiro, Rubyx, Coral, Coral RuPay, Amazon Pay ICICI, MakeMyTrip, HPCL Super Saver, Platinum Chip, Expressions Debit, Sapphiro Debit, Coral Debit)
- Axis Bank - 17 cards (Burgundy Private, Magnus, Atlas, Olympus, Select, Privilege, Airtel Axis, Flipkart Axis, Ace, IndianOil, Neo, My Zone, Rewards, Samsung Infinite, Burgundy Debit, Priority Debit, Liberty Debit)
- Kotak Mahindra Bank - 12 cards (White Reserve, White, Privy League, Zen, Mojo, League, 811 Dream, IOCL, PVR INOX, Myntra, 811 Debit, Privy Debit)
- IndusInd Bank - 11 cards (Pioneer Heritage, Pioneer Legacy, Celesta, Pinnacle, Legend, Nexxt, Platinum Aura Edge, Tiger, EazyDiner, Club Vistara Explorer, Exclusive Debit)
- IDFC FIRST Bank - 11 cards (FIRST Private, FIRST Wealth, FIRST Select, FIRST Classic, FIRST Millennia, FIRST WOW, FIRST SWYP, FIRST Power+, Club Vistara, Mayura, Signature Debit)
- Public Sector Banks - 6 cards (PNB Rakshak, PNB Select, PNB Platinum, Canara Select, Union Select, BOI Select)
- AU Small Finance Bank - 8 cards (Zenith+, Zenith, Vetta, Altura+, Altura, LIT, Ixigo AU, Royale Debit)
- RBL Bank - 8 cards (World Safari, Insignia, Icon, ShopRite, Play BMS, Cookies, SuperCard Platinum Choice, SuperCard Binge)
- Federal & Yes Bank - 8 cards (Federal Celesta, Imperio, Signet, Scapia Federal, Yes Marquee, Yes Reserv, Yes Elite, Kiwi Yes Bank RuPay)
- Standard Chartered, HSBC & American Express - 12 cards (SC Ultimate, SC EaseMyTrip, SC Smart, HSBC Premier, HSBC Live+, HSBC Platinum, Amex Centurion, Amex Plat Charge, Amex Gold, Amex Plat Travel, Amex MRCC, Amex SmartEarn)
- Fintech & Neo-Banks - 8 cards (OneCard Metal, Uni NX Wave, Jupiter Edge RuPay, Fi Amplifi, Fi Infinite Debit, Jupiter Debit, Niyo Global Debit, Slice Spark)
"""

import json
import os
import sys
if sys.stdout.encoding and sys.stdout.encoding.lower() != 'utf-8':
    try:
        sys.stdout.reconfigure(encoding='utf-8')
    except Exception:
        pass

def build_catalogs():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    project_dir = os.path.abspath(os.path.join(script_dir, ".."))
    app_assets_dir = os.path.join(project_dir, "app", "src", "main", "assets")
    root_assets_dir = os.path.join(project_dir, "assets")
    
    os.makedirs(app_assets_dir, exist_ok=True)
    os.makedirs(root_assets_dir, exist_ok=True)

    # -------------------------------------------------------------------------
    # 1. GENERATE 162 CREDIT & DEBIT CARDS
    # -------------------------------------------------------------------------
    raw_cards_data = []

    def c(cid, name, issuer, network, ctype, fee, waiver, ltf, img, logo, gstart, gend, rating,
          lounge, forex, rtype, bestfor, basecb, rules=None, aff=None):
        if rules is None:
            rules = [
                {"category": "online_general", "cashbackPercent": basecb * 2.0, "monthlyCapInr": 1000},
                {"category": "dining", "cashbackPercent": basecb * 2.5, "monthlyCapInr": 500},
                {"category": "fuel", "cashbackPercent": 1.0, "monthlyCapInr": 250},
                {"category": "offline_general", "cashbackPercent": basecb, "monthlyCapInr": None}
            ]
        if aff is None:
            aff = [
                {"portal": "CashKaro", "rewardAmountInr": 1000, "rewardType": "Amazon Voucher", "applyUrl": f"https://cashkaro.com/refer/{cid}", "portalLogoUrl": "https://logo.clearbit.com/cashkaro.com", "rating": "Fast Track"},
                {"portal": "Bank Direct", "rewardAmountInr": fee if fee > 0 else 500, "rewardType": "Welcome Gift Voucher", "applyUrl": f"https://www.google.com/search?q={cid}+apply+online", "portalLogoUrl": logo, "rating": "Official"}
            ]
        return {
            "id": cid,
            "name": name,
            "issuer": issuer,
            "network": network,
            "cardType": ctype,
            "joiningFee": fee,
            "annualFee": fee,
            "waiverSpendThreshold": waiver,
            "isLifetimeFree": ltf,
            "imageUrl": img,
            "bankLogoUrl": logo,
            "cardGradientStart": gstart,
            "cardGradientEnd": gend,
            "rating": rating,
            "loungeAccess": lounge,
            "forexMarkupPercent": forex,
            "rewardType": rtype,
            "bestForCategory": bestfor,
            "baseCashbackPercent": basecb,
            "merchantRules": rules,
            "affiliateOffers": aff
        }

    bob_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ca/Bank_of_Baroda_Logo.svg/512px-Bank_of_Baroda_Logo.svg.png"
    sbi_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/cc/SBI-Logo.svg/512px-SBI-Logo.svg.png"
    hdfc_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/28/HDFC_Bank_Logo.svg/512px-HDFC_Bank_Logo.svg.png"
    icici_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/ICICI_Bank_Logo.svg/512px-ICICI_Bank_Logo.svg.png"
    axis_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/Axis_Bank_logo.svg/512px-Axis_Bank_logo.svg.png"
    kotak_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7b/Kotak_Mahindra_Bank_logo.svg/512px-Kotak_Mahindra_Bank_logo.svg.png"
    indus_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/87/IndusInd_Bank_Logo.svg/512px-IndusInd_Bank_Logo.svg.png"
    idfc_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8e/IDFC_First_Bank_logo.svg/512px-IDFC_First_Bank_logo.svg.png"
    au_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/AU_Small_Finance_Bank_Logo.svg/512px-AU_Small_Finance_Bank_Logo.svg.png"
    rbl_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/52/RBL_Bank_logo.svg/512px-RBL_Bank_logo.svg.png"
    fed_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Federal_Bank_Logo.svg/512px-Federal_Bank_Logo.svg.png"
    yes_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/Yes_Bank_Logo.svg/512px-Yes_Bank_Logo.svg.png"
    amex_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/American_Express_logo_%282018%29.svg/512px-American_Express_logo_%282018%29.svg.png"
    sc_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Standard_Chartered_%282021%29.svg/512px-Standard_Chartered_%282021%29.svg.png"
    hsbc_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/HSBC_logo_%282018%29.svg/512px-HSBC_logo_%282018%29.svg.png"
    pnb_logo = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Punjab_National_Bank_Logo.svg/512px-Punjab_National_Bank_Logo.svg.png"

    # --- 1. BANK OF BARODA (14 cards) ---
    raw_cards_data.append(c("bob_eterna", "Bank of Baroda Eterna Credit Card", "Bank of Baroda", "Visa", "CREDIT", 2499, 500000, False,
        "https://www.bobfinancial.com/images/eterna-card.png", bob_logo, "#F26522", "#1C2A39", 4.9,
        "Unlimited complimentary domestic airport lounge access", 2.0, "Reward Points (3.75% value on travel/dining)", "Travel, Dining & Unlimited Domestic Lounges", 0.75,
        [{"category": "dining", "cashbackPercent": 3.75, "monthlyCapInr": 1250}, {"category": "travel", "cashbackPercent": 3.75, "monthlyCapInr": 1250}, {"category": "movies", "cashbackPercent": 25.0, "monthlyCapInr": 250}, {"category": "fuel", "cashbackPercent": 1.0, "monthlyCapInr": 250}, {"category": "offline_general", "cashbackPercent": 0.75, "monthlyCapInr": None}]))
    raw_cards_data.append(c("bob_premier", "Bank of Baroda Premier Credit Card", "Bank of Baroda", "Visa", "CREDIT", 1000, 120000, False,
        "https://www.bobfinancial.com/images/premier-card.png", bob_logo, "#1A237E", "#F26522", 4.3,
        "1 complimentary domestic lounge visit per calendar quarter", 2.5, "Reward Points (2.5% travel & dining)", "Travel & Dining", 0.5))
    raw_cards_data.append(c("bob_select", "Bank of Baroda Select Credit Card", "Bank of Baroda", "Visa", "CREDIT", 750, 70000, False,
        "https://www.bobfinancial.com/images/select-card.png", bob_logo, "#004D40", "#F26522", 4.1,
        "1 complimentary domestic lounge per calendar quarter", 3.0, "Reward Points (5X on dining & utility)", "Dining & Utilities", 0.5))
    raw_cards_data.append(c("bob_easy", "Bank of Baroda Easy Credit Card", "Bank of Baroda", "Visa", "CREDIT", 500, 35000, False,
        "https://www.bobfinancial.com/images/easy-card.png", bob_logo, "#37474F", "#00E676", 4.0,
        "No complimentary airport lounge access", 3.5, "Reward Points (5X grocery & departmental)", "Grocery & Everyday Spends", 0.5))
    raw_cards_data.append(c("bob_varunah", "Bank of Baroda Varunah RuPay Credit Card", "Bank of Baroda", "RuPay", "CREDIT", 2499, 500000, False,
        "https://www.bobfinancial.com/images/varunah-card.png", bob_logo, "#0D47A1", "#FF6F00", 4.8,
        "Unlimited domestic lounges + 4 international lounges per year via RuPay network", 2.0, "Reward Points (3.5% on UPI & dining)", "RuPay UPI & Premium Airport Lounges", 0.75))
    raw_cards_data.append(c("bob_varunah_premium", "Bank of Baroda Varunah Premium RuPay Credit Card", "Bank of Baroda", "RuPay", "CREDIT", 1499, 300000, False,
        "https://www.bobfinancial.com/images/varunah-card.png", bob_logo, "#1565C0", "#FFA000", 4.5,
        "8 complimentary domestic airport lounges per year (2 per quarter)", 2.5, "Reward Points (5X rewards on dining & travel)", "RuPay UPI & Travel", 0.6))
    raw_cards_data.append(c("bob_conqr", "Bank of Baroda ConQR RuPay Credit Card", "Bank of Baroda", "RuPay", "CREDIT", 0, 0, True,
        "https://www.bobfinancial.com/images/conqr-card.png", bob_logo, "#263238", "#00B0FF", 4.1,
        "No complimentary airport lounge access", 3.5, "Reward Points (UPI Merchant Cashpoints)", "Merchant Payments & Small Businesses", 0.5))
    raw_cards_data.append(c("bob_swavlamban", "Bank of Baroda Swavlamban RuPay Credit Card", "Bank of Baroda", "RuPay", "CREDIT", 0, 0, True,
        "https://www.bobfinancial.com/images/swavlamban-card.png", bob_logo, "#3E2723", "#8D6E63", 3.9,
        "No complimentary airport lounge access", 3.5, "Reward Points (Basic)", "Everyday Needs & Financial Inclusion", 0.4))
    raw_cards_data.append(c("bob_irctc", "IRCTC BoB RuPay Credit Card", "Bank of Baroda", "RuPay", "CREDIT", 500, 50000, False,
        "https://www.bobfinancial.com/images/irctc-card.png", bob_logo, "#BF360C", "#FF6F00", 4.4,
        "4 complimentary railway executive lounges per year", 3.5, "IRCTC Reward Points (Up to 40 points per ₹100 on train bookings)", "Train Travel & Railway Lounges", 0.5))
    raw_cards_data.append(c("bob_hpcl_energie", "HPCL BoB ENERGIE RuPay Credit Card", "Bank of Baroda", "RuPay", "CREDIT", 499, 50000, False,
        "https://www.bobfinancial.com/images/hpcl-card.png", bob_logo, "#B71C1C", "#D32F2F", 4.5,
        "4 complimentary domestic airport lounges per year (1 per quarter)", 3.5, "HP Pay Fuel Points (5.0% savings on HPCL fuel)", "HPCL Fuel & LPG Gas Bookings", 0.5))
    raw_cards_data.append(c("bob_snapdeal", "Snapdeal BoB Credit Card", "Bank of Baroda", "RuPay", "CREDIT", 249, 25000, False,
        "https://www.bobfinancial.com/images/snapdeal-card.png", bob_logo, "#880E4F", "#E91E63", 4.0,
        "No complimentary airport lounge access", 3.5, "Snapdeal Reward Points (5% on Snapdeal shopping)", "Snapdeal E-Commerce", 0.5))
    raw_cards_data.append(c("bob_vikram", "Bank of Baroda Vikram Defense RuPay Credit Card", "Bank of Baroda", "RuPay", "CREDIT", 0, 0, True,
        "https://www.bobfinancial.com/images/vikram-card.png", bob_logo, "#1B5E20", "#2E7D32", 4.9,
        "4 complimentary domestic airport lounges per year + ₹20 Lakhs accidental death insurance", 3.0, "Reward Points (5X on grocery, departmental & fuel)", "Indian Defense & Paramilitary Forces", 0.75))
    raw_cards_data.append(c("bob_yoddha", "Bank of Baroda Yoddha RuPay Credit Card", "Bank of Baroda", "RuPay", "CREDIT", 0, 0, True,
        "https://www.bobfinancial.com/images/yoddha-card.png", bob_logo, "#33691E", "#558B2F", 4.8,
        "8 complimentary domestic airport lounges per year (2 per quarter)", 2.5, "Reward Points (10X on utility, travel & grocery)", "Armed Forces & Paramilitary Officers", 0.8))
    raw_cards_data.append(c("bob_visa_platinum_debit", "Bank of Baroda Visa Platinum Debit Card", "Bank of Baroda", "Visa", "DEBIT", 250, 0, False,
        "https://www.bobfinancial.com/images/debit-card.png", bob_logo, "#006064", "#0097A7", 4.2,
        "2 complimentary domestic airport lounges per quarter on active account", 3.5, "Baroda Reward Points (₹1 Lakh daily ATM limit)", "Debit Card Privileges & Domestic Lounges", 0.5))

    # --- 2. STATE BANK OF INDIA (17 cards) ---
    raw_cards_data.append(c("sbi_aurum", "SBI Card Aurum Metal Credit Card", "State Bank of India", "Mastercard", "CREDIT", 9999, 1200000, False,
        "https://www.sbicard.com/aurum.png", sbi_logo, "#1A1608", "#FFD700", 4.9,
        "Unlimited domestic lounges + 4 international lounges per quarter via DreamFolks", 1.99, "Aurum Reward Points (4 points per ₹100, 1 pt = ₹0.25)", "Super Premium Luxury & Concierge", 1.0))
    raw_cards_data.append(c("sbi_elite", "SBI Card Elite", "State Bank of India", "Visa", "CREDIT", 4999, 1000000, False,
        "https://www.sbicard.com/elite.png", sbi_logo, "#102030", "#4A90E2", 4.6,
        "2 complimentary domestic airport lounges per quarter + 6 international Priority Pass visits/yr", 1.99, "Reward Points (5X dining, grocery & departmental)", "Movie BOGO & International Travel", 0.5))
    raw_cards_data.append(c("sbi_prime", "SBI Card Prime", "State Bank of India", "Visa", "CREDIT", 2999, 300000, False,
        "https://www.sbicard.com/prime.png", sbi_logo, "#1A237E", "#3949AB", 4.4,
        "2 complimentary domestic lounges per quarter + 4 international Priority Pass visits/yr", 3.5, "Reward Points (20 points/₹100 on utility bill pay)", "Utilities, Dining & Trident Privileges", 0.5))
    raw_cards_data.append(c("sbi_pulse", "SBI Card Pulse", "State Bank of India", "Visa", "CREDIT", 1499, 200000, False,
        "https://www.sbicard.com/pulse.png", sbi_logo, "#880E4F", "#D81B60", 4.4,
        "8 complimentary domestic airport lounges per year (2 per quarter)", 3.5, "Pulse Points (Noise Smartwatch + 1-yr Fitpass Pro)", "Fitness, Gyms & Health Memberships", 0.5))
    raw_cards_data.append(c("sbi_cashback", "SBI Cashback Credit Card", "State Bank of India", "Visa", "CREDIT", 999, 200000, False,
        "https://www.sbicard.com/cashback.png", sbi_logo, "#004D40", "#00B0FF", 4.9,
        "No complimentary airport lounge access", 3.5, "Direct Cashback (5% on all online spends up to ₹5000/mo)", "Online Shopping (Amazon, Flipkart, Any Website)", 5.0,
        [{"category": "online_general", "cashbackPercent": 5.0, "monthlyCapInr": 5000}, {"category": "dining", "cashbackPercent": 5.0, "monthlyCapInr": 5000}, {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}]))
    raw_cards_data.append(c("sbi_simplyclick", "SimplyCLICK SBI Card", "State Bank of India", "Visa", "CREDIT", 499, 100000, False,
        "https://www.sbicard.com/simplyclick.png", sbi_logo, "#0D47A1", "#00E5FF", 4.5,
        "No complimentary airport lounge access", 3.5, "Reward Points (10X on Amazon, Cleartrip, BookMyShow, Lenskart, Netmeds)", "Online Shopping & Entertainment", 0.25))
    raw_cards_data.append(c("sbi_simplysave", "SimplySAVE SBI Card", "State Bank of India", "Visa", "CREDIT", 499, 100000, False,
        "https://www.sbicard.com/simplysave.png", sbi_logo, "#B71C1C", "#FF5252", 4.2,
        "No complimentary airport lounge access", 3.5, "Reward Points (10X on dining, grocery, departmental & movies)", "Dining, Groceries & Everyday Offline Spends", 0.25))
    raw_cards_data.append(c("sbi_simplysave_rupay", "SimplySAVE RuPay SBI Card", "State Bank of India", "RuPay", "CREDIT", 499, 100000, False,
        "https://www.sbicard.com/simplysave-rupay.png", sbi_logo, "#BF360C", "#FF6D00", 4.4,
        "No complimentary airport lounge access", 3.5, "Reward Points (10X points on dining, grocery & RuPay UPI)", "RuPay UPI Scan & Pay + Groceries", 0.25))
    raw_cards_data.append(c("sbi_bpcl", "BPCL SBI Card", "State Bank of India", "Visa", "CREDIT", 499, 50000, False,
        "https://www.sbicard.com/bpcl.png", sbi_logo, "#FF6F00", "#FFD54F", 4.3,
        "No complimentary airport lounge access", 3.5, "Reward Points (4.25% valueback = 13X points on BPCL fuel)", "BPCL Fuel & Lubricants", 0.25))
    raw_cards_data.append(c("sbi_bpcl_octane", "BPCL SBI Card Octane", "State Bank of India", "Visa", "CREDIT", 1499, 200000, False,
        "https://www.sbicard.com/bpcl-octane.png", sbi_logo, "#E65100", "#FF9800", 4.7,
        "4 complimentary domestic airport lounges per year (1 per quarter)", 3.5, "Reward Points (7.25% valueback = 25X points on BPCL petrol & diesel)", "High Fuel Spends & BPCL Surcharge Waiver", 0.25))
    raw_cards_data.append(c("sbi_air_india_signature", "Air India SBI Signature Credit Card", "State Bank of India", "Visa", "CREDIT", 4999, 300000, False,
        "https://www.sbicard.com/air-india.png", sbi_logo, "#B71C1C", "#FFD700", 4.5,
        "8 complimentary domestic airport lounges per year (2 per quarter)", 3.5, "Flying Returns Points (Up to 30 points per ₹100 on Air India tickets)", "Air India Flights & Flying Returns Miles", 0.5))
    raw_cards_data.append(c("sbi_vistara_prime", "Club Vistara SBI Card Prime", "State Bank of India", "Visa", "CREDIT", 2999, 300000, False,
        "https://www.sbicard.com/vistara-prime.png", sbi_logo, "#4A148C", "#8E24AA", 4.6,
        "8 complimentary domestic airport lounges per year (2 per quarter) + 4 international lounges via Priority Pass", 3.5, "CV Points (Complimentary Premium Economy Ticket Voucher on joining & renewal)", "Vistara Flights & Airline Miles", 0.5))
    raw_cards_data.append(c("sbi_irctc_premier", "IRCTC SBI Card Premier", "State Bank of India", "RuPay", "CREDIT", 1499, 200000, False,
        "https://www.sbicard.com/irctc-premier.png", sbi_logo, "#1A237E", "#303F9F", 4.5,
        "8 complimentary railway executive lounges per year (2 per quarter)", 3.5, "IRCTC Reward Points (Up to 10% valueback on train tickets booked on irctc.co.in)", "Indian Railways Train Tickets & Executive Lounges", 0.5))
    raw_cards_data.append(c("sbi_paytm_select", "Paytm SBI Card Select", "State Bank of India", "Visa", "CREDIT", 1499, 200000, False,
        "https://www.sbicard.com/paytm-select.png", sbi_logo, "#002E6E", "#00B9F5", 4.3,
        "4 complimentary domestic airport lounges per year (1 per quarter)", 3.5, "Paytm Cashback (5% on Paytm Mall, Movies & Travel)", "Paytm App Ecosystem & Travel", 0.5))
    raw_cards_data.append(c("sbi_reliance", "Reliance SBI Card", "State Bank of India", "RuPay", "CREDIT", 499, 100000, False,
        "https://www.sbicard.com/reliance.png", sbi_logo, "#1A237E", "#C2185B", 4.2,
        "No complimentary airport lounge access", 3.5, "Reliance Reward Points (5 points per ₹100 at Smart Bazaar, Trends, Digital)", "Reliance Retail, Trends & JioMart", 0.25))
    raw_cards_data.append(c("sbi_global_debit", "SBI Global International Debit Card", "State Bank of India", "Visa", "DEBIT", 125, 0, False,
        "https://www.sbicard.com/global-debit.png", sbi_logo, "#0D47A1", "#1976D2", 4.1,
        "No complimentary airport lounge access", 3.5, "SBI Rewardz Points (₹40,000 daily ATM cash withdrawal)", "Everyday Banking & High ATM Limit", 0.25))
    raw_cards_data.append(c("sbi_platinum_debit", "SBI Platinum International Debit Card", "State Bank of India", "Visa", "DEBIT", 250, 0, False,
        "https://www.sbicard.com/platinum-debit.png", sbi_logo, "#263238", "#455A64", 4.4,
        "2 complimentary domestic airport lounge visits per calendar quarter", 3.0, "SBI Rewardz Points (2X points on international spends)", "Debit Card Domestic Airport Lounges", 0.5))

    # --- 3. HDFC BANK (17 cards) ---
    raw_cards_data.append(c("hdfc_infinia", "HDFC Bank Infinia Metal Credit Card", "HDFC Bank", "Visa", "CREDIT", 12500, 1000000, False,
        "https://www.hdfcbank.com/infinia.png", hdfc_logo, "#0A0A0A", "#2E2E2E", 5.0,
        "Unlimited domestic & international airport lounge access with complimentary guest visits", 2.0, "Reward Points (33.3% return on SmartBuy flights & hotels)", "Super Premium Travel, SmartBuy & Dining", 3.3,
        [{"category": "travel", "cashbackPercent": 33.3, "monthlyCapInr": 15000}, {"category": "dining", "cashbackPercent": 15.0, "monthlyCapInr": 5000}, {"category": "online_general", "cashbackPercent": 3.3, "monthlyCapInr": None}]))
    raw_cards_data.append(c("hdfc_diners_black", "HDFC Diners Club Black Metal", "HDFC Bank", "Diners", "CREDIT", 10000, 800000, False,
        "https://www.hdfcbank.com/dcb.png", hdfc_logo, "#1A1A1A", "#3A3A3A", 4.9,
        "Unlimited domestic & international airport lounges worldwide + 6 complimentary golf games/qtr", 2.0, "Reward Points (33.3% SmartBuy reward rate)", "Luxury Travel, Golf & Fine Dining", 3.3))
    raw_cards_data.append(c("hdfc_diners_privilege", "HDFC Diners Club Privilege", "HDFC Bank", "Diners", "CREDIT", 2500, 300000, False,
        "https://www.hdfcbank.com/dcp.png", hdfc_logo, "#102030", "#304050", 4.4,
        "2 complimentary domestic & international lounge visits per quarter", 3.5, "Reward Points (Buy 1 Get 1 on BookMyShow twice a month)", "Entertainment & Dining BOGO", 0.75))
    raw_cards_data.append(c("hdfc_regalia_gold", "HDFC Bank Regalia Gold Credit Card", "HDFC Bank", "Visa", "CREDIT", 2500, 400000, False,
        "https://www.hdfcbank.com/regalia-gold.png", hdfc_logo, "#1C1408", "#C5A059", 4.6,
        "12 complimentary domestic airport lounges per year + 6 international Priority Pass visits", 2.0, "Reward Points (Marks & Spencer, Myntra, Nykaa milestone vouchers)", "Lifestyle, Fashion & Travel", 1.33))
    raw_cards_data.append(c("hdfc_millennia", "HDFC Bank Millennia Credit Card", "HDFC Bank", "Visa", "CREDIT", 1000, 100000, False,
        "https://www.hdfcbank.com/millennia.png", hdfc_logo, "#002040", "#008080", 4.8,
        "1 complimentary domestic airport lounge per quarter on spending ₹1 Lakh in previous quarter", 3.5, "CashPoints (5% cashback on Amazon, Flipkart, Swiggy, Zomato, Uber, BookMyShow)", "Online Shopping & Food Delivery", 5.0,
        [{"category": "online_general", "cashbackPercent": 5.0, "monthlyCapInr": 1000}, {"category": "dining", "cashbackPercent": 5.0, "monthlyCapInr": 1000}, {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}]))
    raw_cards_data.append(c("tata_neu_infinity", "Tata Neu Infinity HDFC Credit Card", "HDFC Bank", "RuPay", "CREDIT", 1499, 300000, False,
        "https://www.hdfcbank.com/tata-neu-infinity.png", hdfc_logo, "#2A0845", "#6441A5", 4.8,
        "8 complimentary domestic airport lounges per year + 4 international lounges via Priority Pass", 2.0, "NeuCoins (10% on Tata Neu / 1.5% on UPI RuPay transactions)", "Tata Neu Ecosystem & RuPay UPI", 1.5))
    raw_cards_data.append(c("tata_neu_plus", "Tata Neu Plus HDFC Credit Card", "HDFC Bank", "RuPay", "CREDIT", 499, 100000, False,
        "https://www.hdfcbank.com/tata-neu-plus.png", hdfc_logo, "#3B145E", "#8A54C9", 4.3,
        "4 complimentary domestic airport lounges per year (1 per quarter)", 3.5, "NeuCoins (7% on Tata brands / 1% on UPI RuPay)", "Tata Brands (BigBasket, Croma, 1mg, Air India)", 1.0))
    raw_cards_data.append(c("hdfc_swiggy", "HDFC Swiggy Credit Card", "HDFC Bank", "Mastercard", "CREDIT", 500, 200000, False,
        "https://www.hdfcbank.com/swiggy.png", hdfc_logo, "#FC8019", "#333333", 4.8,
        "No complimentary airport lounge access", 3.5, "Swiggy Money Cashback (10% on Swiggy Food, Instamart, Dineout, Genie up to ₹1500/mo)", "Swiggy Food Orders, Instamart & Dineout", 10.0,
        [{"category": "dining", "cashbackPercent": 10.0, "monthlyCapInr": 1500}, {"category": "online_general", "cashbackPercent": 5.0, "monthlyCapInr": 1500}, {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}]))
    raw_cards_data.append(c("hdfc_marriott_bonvoy", "Marriott Bonvoy HDFC Credit Card", "HDFC Bank", "Diners", "CREDIT", 3000, 0, False,
        "https://www.hdfcbank.com/marriott.png", hdfc_logo, "#101820", "#AA7C11", 4.7,
        "12 complimentary domestic airport lounges + 12 international lounges per year", 3.5, "Marriott Bonvoy Points (1 Free Night Award up to 15,000 points + 10 Elite Night Credits)", "Marriott Hotels, Resorts & Luxury Stays", 1.0))
    raw_cards_data.append(c("hdfc_shoppers_stop", "Shoppers Stop HDFC Bank Credit Card", "HDFC Bank", "Visa", "CREDIT", 500, 50000, False,
        "https://www.hdfcbank.com/shoppers-stop.png", hdfc_logo, "#1A1A1A", "#E50914", 4.1,
        "No complimentary airport lounge access", 3.5, "Shoppers Stop Points (6 points per ₹150 spent at Shoppers Stop)", "Fashion Apparel & Departmental Shopping", 0.5))
    raw_cards_data.append(c("hdfc_indianoil", "IndianOil HDFC Bank Credit Card", "HDFC Bank", "RuPay", "CREDIT", 500, 50000, False,
        "https://www.hdfcbank.com/indianoil.png", hdfc_logo, "#BF360C", "#FF6F00", 4.3,
        "No complimentary airport lounge access", 3.5, "Fuel Points (5% on fuel spends at IndianOil outlets, 50 liters free fuel/yr)", "IndianOil Petrol & Diesel", 0.5))
    raw_cards_data.append(c("hdfc_freedom", "HDFC Freedom Credit Card", "HDFC Bank", "Visa", "CREDIT", 500, 50000, False,
        "https://www.hdfcbank.com/freedom.png", hdfc_logo, "#004D40", "#00B4D8", 4.0,
        "No complimentary airport lounge access", 3.5, "CashPoints (10X CashPoints on BigBasket, BookMyShow, OYO, Swiggy, Uber)", "Entry-level Lifestyle & Entertainment", 0.25))
    raw_cards_data.append(c("hdfc_moneyback_plus", "HDFC MoneyBack+ Credit Card", "HDFC Bank", "Visa", "CREDIT", 500, 50000, False,
        "https://www.hdfcbank.com/moneyback-plus.png", hdfc_logo, "#1A237E", "#283593", 4.2,
        "No complimentary airport lounge access", 3.5, "CashPoints (10X CashPoints on Amazon, Flipkart, BigBasket, Reliance Smart, Swiggy)", "Everyday Online Shopping & Departmental", 0.25))
    raw_cards_data.append(c("hdfc_pixel_play", "HDFC Pixel Play Credit Card", "HDFC Bank", "Visa", "CREDIT", 500, 100000, False,
        "https://www.hdfcbank.com/pixel-play.png", hdfc_logo, "#6200EA", "#B388FF", 4.4,
        "No complimentary airport lounge access", 3.5, "Cashback (Customizable 5% cashback on choice of 2 merchants)", "Gen Z Digital Customization", 1.0))
    raw_cards_data.append(c("hdfc_easyshop_platinum_debit", "HDFC EasyShop Platinum Debit Card", "HDFC Bank", "Visa", "DEBIT", 750, 0, False,
        "https://www.hdfcbank.com/easyshop-platinum.png", hdfc_logo, "#102040", "#00A86B", 4.7,
        "2 complimentary domestic airport lounges per quarter on spending ₹5000 in previous quarter", 3.5, "Cashback Points (1% cashback on telecom, utilities, grocery & bill pay via NetBanking/SmartPay up to ₹750/mo)", "Debit Card Bill Pay Cashback & Domestic Lounges", 1.0,
        [{"category": "utilities", "cashbackPercent": 1.0, "monthlyCapInr": 750}, {"category": "online_general", "cashbackPercent": 1.0, "monthlyCapInr": 750}]))
    raw_cards_data.append(c("hdfc_millennia_debit", "HDFC Millennia Debit Card", "HDFC Bank", "Mastercard", "DEBIT", 500, 0, False,
        "https://www.hdfcbank.com/millennia-debit.png", hdfc_logo, "#003366", "#009999", 4.5,
        "1 complimentary domestic airport lounge per quarter on minimum ₹5k quarterly spend", 3.5, "Cashback Points (5% cashback on PayZapp & SmartBuy, 2.5% on online shopping up to ₹400/mo)", "Debit Card PayZapp & Online Shopping", 2.5))
    raw_cards_data.append(c("hdfc_imperia_debit", "HDFC Imperia Platinum Debit Card", "HDFC Bank", "Visa", "DEBIT", 0, 0, True,
        "https://www.hdfcbank.com/imperia-debit.png", hdfc_logo, "#0B132B", "#FFD700", 4.9,
        "Unlimited complimentary domestic airport lounge access for primary cardholder", 2.0, "Imperia Reward Points (₹10 Lakhs daily purchase limit, zero ATM charges globally)", "High Net-Worth Imperia Banking & Free Lounges", 1.0))

    # --- 4. ICICI BANK (13 cards) ---
    raw_cards_data.append(c("icici_emeralde_private", "ICICI Emeralde Private Metal Credit Card", "ICICI Bank", "Visa", "CREDIT", 12499, 1000000, False,
        "https://www.icicibank.com/emeralde-private.png", icici_logo, "#0A0A0A", "#8D6E63", 4.9,
        "Unlimited domestic & international airport lounge access + unlimited spa sessions with DreamFolks", 1.99, "Reward Points (6 points per ₹200 on all retail)", "Ultra Luxury Travel, Taj Epicure & Secretarial Concierge", 1.5))
    raw_cards_data.append(c("icici_emeralde", "ICICI Emeralde Credit Card", "ICICI Bank", "Mastercard", "CREDIT", 12000, 1000000, False,
        "https://www.icicibank.com/emeralde.png", icici_logo, "#101820", "#107C41", 4.7,
        "Unlimited domestic airport lounges + Buy 1 Get 1 on BookMyShow up to ₹750 twice a month", 1.5, "Reward Points (4 points per ₹100 on all spends)", "Unlimited Lounges & Cinema BOGO", 1.0))
    raw_cards_data.append(c("icici_sapphiro", "ICICI Sapphiro Credit Card", "ICICI Bank", "Visa", "CREDIT", 6500, 600000, False,
        "https://www.icicibank.com/sapphiro.png", icici_logo, "#0A192F", "#00B4D8", 4.7,
        "4 complimentary domestic lounges per quarter on ₹35k spend + 2 international lounges via DreamFolks", 3.5, "Reward Points (Buy 1 Get 1 on BookMyShow up to ₹500 twice a month)", "Entertainment Cinema BOGO & Domestic Lounges", 0.5))
    raw_cards_data.append(c("icici_rubyx", "ICICI Rubyx Credit Card", "ICICI Bank", "Mastercard", "CREDIT", 3000, 300000, False,
        "https://www.icicibank.com/rubyx.png", icici_logo, "#800020", "#FF4081", 4.3,
        "2 complimentary domestic airport lounges per quarter on ₹35k spend", 3.5, "Reward Points (25% discount on BookMyShow up to ₹150 twice a month)", "Shopping Privileges & Cinema", 0.5))
    raw_cards_data.append(c("icici_coral", "ICICI Coral Credit Card", "ICICI Bank", "Visa", "CREDIT", 500, 150000, False,
        "https://www.icicibank.com/coral.png", icici_logo, "#D84315", "#FF8A65", 4.2,
        "1 complimentary domestic airport lounge per quarter on ₹35k spend", 3.5, "Reward Points (25% off BookMyShow twice a month up to ₹100)", "Entry-level Dining & Entertainment", 0.5))
    raw_cards_data.append(c("icici_coral_rupay", "ICICI Coral RuPay Credit Card", "ICICI Bank", "RuPay", "CREDIT", 500, 150000, False,
        "https://www.icicibank.com/coral-rupay.png", icici_logo, "#E64A19", "#FF5722", 4.4,
        "1 complimentary domestic airport lounge per quarter on ₹35k spend", 3.5, "Reward Points (2X points on dining, groceries & RuPay UPI)", "RuPay UPI Payments + Dining", 0.5))
    raw_cards_data.append(c("amazon_pay_icici", "Amazon Pay ICICI Credit Card", "ICICI Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.icicibank.com/amazon-pay.png", icici_logo, "#131921", "#FF9900", 4.9,
        "No complimentary airport lounge access", 3.5, "Amazon Pay Balance (5% unlimited for Prime / 3% for non-Prime / 2% on bills)", "Amazon India Shopping, Recharges & Bill Payments", 5.0,
        [{"category": "online_general", "cashbackPercent": 5.0, "monthlyCapInr": None}, {"category": "utilities", "cashbackPercent": 2.0, "monthlyCapInr": None}, {"category": "offline_general", "cashbackPercent": 1.0, "monthlyCapInr": None}]))
    raw_cards_data.append(c("icici_makemytrip_signature", "MakeMyTrip ICICI Bank Signature Credit Card", "ICICI Bank", "Visa", "CREDIT", 2500, 250000, False,
        "https://www.icicibank.com/makemytrip.png", icici_logo, "#B71C1C", "#FFD700", 4.5,
        "1 complimentary domestic airport lounge per quarter + 1 international lounge per year", 3.5, "My Cash (₹1500 MMT voucher + 4 MMT black points per ₹200 on flights/hotels)", "MakeMyTrip Flights, Hotels & Holiday Packages", 1.0))
    raw_cards_data.append(c("icici_hpcl_super_saver", "HPCL Super Saver ICICI Credit Card", "ICICI Bank", "Visa", "CREDIT", 500, 150000, False,
        "https://www.icicibank.com/hpcl.png", icici_logo, "#0D47A1", "#1E88E5", 4.3,
        "No complimentary airport lounge access", 3.5, "Fuel Cashback (5% cashback on HPCL petrol/diesel via HP Pay + 5% on utility)", "HPCL Fuel & Utility Spends", 0.5))
    raw_cards_data.append(c("icici_platinum_chip", "ICICI Platinum Chip Credit Card", "ICICI Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.icicibank.com/platinum-chip.png", icici_logo, "#263238", "#78909C", 4.0,
        "No complimentary airport lounge access", 3.5, "ICICI Reward Points (2 points per ₹100 on retail)", "Zero Maintenance Fee Credit Card", 0.5))
    raw_cards_data.append(c("icici_expressions_debit", "ICICI Expressions Debit Card", "ICICI Bank", "Visa", "DEBIT", 499, 0, False,
        "https://www.icicibank.com/expressions-debit.png", icici_logo, "#311B92", "#7E57C2", 4.3,
        "1 complimentary domestic airport lounge per quarter on active account", 3.5, "Reward Points (Personalized custom card design, ₹5 Lakhs daily POS limit)", "Personalized Custom Debit Card Design", 0.5))
    raw_cards_data.append(c("icici_sapphiro_debit", "ICICI Sapphiro Debit Card", "ICICI Bank", "Mastercard", "DEBIT", 1999, 0, False,
        "https://www.icicibank.com/sapphiro-debit.png", icici_logo, "#0A192F", "#00B4D8", 4.6,
        "4 complimentary domestic airport lounges per quarter on active account", 3.5, "Reward Points (Buy 1 Get 1 on BookMyShow up to ₹250, personal accident cover ₹20 Lakhs)", "Debit Card Airport Lounges & Cinema BOGO", 0.75))
    raw_cards_data.append(c("icici_coral_debit", "ICICI Coral Debit Card", "ICICI Bank", "Visa", "DEBIT", 599, 0, False,
        "https://www.icicibank.com/coral-debit.png", icici_logo, "#BF360C", "#FF7043", 4.2,
        "1 complimentary domestic airport lounge per quarter on minimum ₹3000 spend", 3.5, "Reward Points (25% discount on BookMyShow movies, dining delights)", "Entry-level Debit Card Lounges", 0.5))

    # --- 5. AXIS BANK (17 cards) ---
    raw_cards_data.append(c("axis_burgundy_private", "Axis Burgundy Private Metal Credit Card", "Axis Bank", "Visa", "CREDIT", 50000, 0, False,
        "https://www.axisbank.com/burgundy-private.png", axis_logo, "#800020", "#FFD700", 5.0,
        "Unlimited domestic & international airport lounges worldwide with complimentary guest visits", 0.0, "Edge Reward Points (30 points per ₹200 on all retail, 100% zero forex markup)", "Ultra Luxury Wealth Banking, Zero Forex & Private Concierge", 3.0))
    raw_cards_data.append(c("axis_magnus", "Axis Bank Magnus Credit Card", "Axis Bank", "Visa", "CREDIT", 12500, 2500000, False,
        "https://www.axisbank.com/magnus.png", axis_logo, "#800020", "#B71C1C", 4.8,
        "Unlimited domestic airport lounges + 8 complimentary guest visits per year", 2.0, "Edge Reward Points (5:4 Air Miles transfer ratio to Singapore Airlines/Accor/Qatar)", "Premium Air Miles Transfers & Luxury Lounges", 1.2))
    raw_cards_data.append(c("axis_atlas", "Axis Bank Atlas Credit Card", "Axis Bank", "Visa", "CREDIT", 5000, 0, False,
        "https://www.axisbank.com/atlas.png", axis_logo, "#102030", "#00A86B", 4.9,
        "Tier-based domestic & international lounges (Up to 18 domestic & 12 international visits per year)", 3.5, "Edge Miles (5 Edge Miles per ₹100 on airlines & hotels, 1:2 transfer ratio)", "Frequent Flying, Hotel Bookings & Airline Miles", 2.0))
    raw_cards_data.append(c("axis_olympus", "Axis Bank Olympus Credit Card", "Axis Bank", "Visa", "CREDIT", 20000, 0, False,
        "https://www.axisbank.com/olympus.png", axis_logo, "#1F1F1F", "#D4AF37", 4.8,
        "Unlimited domestic airport lounges + 8 international visits via Priority Pass", 1.8, "Edge Miles (4 Edge Miles per ₹100, luxury transfer partners)", "Super Premium Travel & Low Forex", 2.0))
    raw_cards_data.append(c("axis_select", "Axis Bank Select Credit Card", "Axis Bank", "Visa", "CREDIT", 3000, 800000, False,
        "https://www.axisbank.com/select.png", axis_logo, "#800020", "#304050", 4.5,
        "6 complimentary domestic airport lounges per quarter on spending ₹50k in previous 3 months", 3.5, "Edge Points (20% off BigBasket up to ₹500/mo, ₹200 off Swiggy twice/mo)", "BigBasket Groceries, Swiggy Dining & Priority Pass", 1.0))
    raw_cards_data.append(c("axis_privilege", "Axis Bank Privilege Credit Card", "Axis Bank", "Visa", "CREDIT", 1500, 250000, False,
        "https://www.axisbank.com/privilege.png", axis_logo, "#800020", "#5C6BC0", 4.2,
        "2 complimentary domestic airport lounges per quarter on spending ₹50k in previous quarter", 3.5, "Edge Points (Shopping vouchers worth ₹5000 on activation)", "Multi-brand Shopping Vouchers", 0.5))
    raw_cards_data.append(c("airtel_axis", "Airtel Axis Bank Credit Card", "Axis Bank", "Mastercard", "CREDIT", 500, 200000, False,
        "https://www.axisbank.com/airtel-axis.png", axis_logo, "#E50914", "#800020", 4.9,
        "No complimentary airport lounge access", 3.5, "Direct Cashback (25% on Airtel bills, 10% on Swiggy/Zomato/BigBasket, 10% on utilities)", "Airtel Mobile/Wi-Fi/DTH Bills & Utilities", 10.0,
        [{"category": "utilities", "cashbackPercent": 10.0, "monthlyCapInr": 250}, {"category": "dining", "cashbackPercent": 10.0, "monthlyCapInr": 500}, {"category": "online_general", "cashbackPercent": 1.0, "monthlyCapInr": None}]))
    raw_cards_data.append(c("flipkart_axis", "Flipkart Axis Bank Credit Card", "Axis Bank", "Visa", "CREDIT", 500, 350000, False,
        "https://www.axisbank.com/flipkart-axis.png", axis_logo, "#2874F0", "#800020", 4.8,
        "No complimentary airport lounge access", 3.5, "Unlimited Cashback (5% on Flipkart & Cleartrip, 4% on Swiggy, Uber, PVR, Tata 1mg)", "Flipkart Online Shopping & Cleartrip", 5.0,
        [{"category": "online_general", "cashbackPercent": 5.0, "monthlyCapInr": None}, {"category": "travel", "cashbackPercent": 5.0, "monthlyCapInr": None}, {"category": "dining", "cashbackPercent": 4.0, "monthlyCapInr": None}]))
    raw_cards_data.append(c("axis_ace", "Axis Bank ACE Credit Card", "Axis Bank", "Visa", "CREDIT", 499, 200000, False,
        "https://www.axisbank.com/ace.png", axis_logo, "#00A86B", "#800020", 4.8,
        "4 complimentary domestic airport lounges per year (1 per quarter)", 3.5, "Direct Cashback (5% on Google Pay bill payments, 4% on Swiggy/Zomato/Ola, 2% offline)", "Google Pay Utility Bills & Flat 2% Offline Spends", 2.0,
        [{"category": "utilities", "cashbackPercent": 5.0, "monthlyCapInr": 500}, {"category": "dining", "cashbackPercent": 4.0, "monthlyCapInr": 500}, {"category": "offline_general", "cashbackPercent": 2.0, "monthlyCapInr": None}]))
    raw_cards_data.append(c("axis_indianoil", "IndianOil Axis Bank Credit Card", "Axis Bank", "RuPay", "CREDIT", 500, 50000, False,
        "https://www.axisbank.com/indianoil-axis.png", axis_logo, "#FF6F00", "#800020", 4.3,
        "No complimentary airport lounge access", 3.5, "Edge Points (4% valueback on IndianOil fuel, 1% fuel surcharge waiver)", "IndianOil Petrol & RuPay UPI", 0.5))
    raw_cards_data.append(c("axis_neo", "Axis Bank Neo Credit Card", "Axis Bank", "Visa", "CREDIT", 250, 100000, False,
        "https://www.axisbank.com/neo.png", axis_logo, "#800020", "#00E5FF", 4.2,
        "No complimentary airport lounge access", 3.5, "Edge Points (10% off Blinkit up to ₹250/mo, 10% off BookMyShow up to ₹100/mo)", "Blinkit Quick Groceries & BookMyShow", 0.5))
    raw_cards_data.append(c("axis_my_zone", "Axis Bank MY ZONE Credit Card", "Axis Bank", "RuPay", "CREDIT", 500, 200000, False,
        "https://www.axisbank.com/my-zone.png", axis_logo, "#800020", "#FF4081", 4.4,
        "4 complimentary domestic airport lounges per year (1 per quarter)", 3.5, "Edge Points (Buy 1 Get 1 on Paytm Movies up to ₹200/mo, SonyLIV annual membership)", "Paytm Movies BOGO & SonyLIV OTT", 0.5))
    raw_cards_data.append(c("axis_rewards", "Axis Bank Rewards Credit Card", "Axis Bank", "Visa", "CREDIT", 1000, 200000, False,
        "https://www.axisbank.com/rewards.png", axis_logo, "#800020", "#FFD700", 4.3,
        "2 complimentary domestic airport lounges per quarter on spending ₹50k in previous 3 months", 3.5, "Edge Reward Points (10X on apparel & departmental stores)", "Apparel & Departmental Shopping", 0.5))
    raw_cards_data.append(c("axis_samsung_infinite", "Samsung Axis Bank Infinite Credit Card", "Axis Bank", "Visa", "CREDIT", 5000, 700000, False,
        "https://www.axisbank.com/samsung-infinite.png", axis_logo, "#034EA2", "#800020", 4.7,
        "8 complimentary domestic airport lounges + 6 international Priority Pass visits/yr", 3.5, "Edge Points (10% cashback year-round on Samsung smartphones, TVs, appliances)", "Samsung Electronics & Domestic Lounges", 1.0))
    raw_cards_data.append(c("axis_burgundy_debit", "Axis Burgundy Debit Card", "Axis Bank", "Mastercard", "DEBIT", 0, 0, True,
        "https://www.axisbank.com/burgundy-debit.png", axis_logo, "#800020", "#1A1A1A", 4.9,
        "Unlimited complimentary domestic & international airport lounges for primary cardholder and guests", 2.0, "Edge Points (3X reward points, 96 BookMyShow movie tickets per year)", "Wealth Banking Debit Card & Free Movie Tickets", 1.5))
    raw_cards_data.append(c("axis_priority_debit", "Axis Priority Debit Card", "Axis Bank", "Visa", "DEBIT", 750, 0, False,
        "https://www.axisbank.com/priority-debit.png", axis_logo, "#800020", "#4A148C", 4.4,
        "1 complimentary domestic airport lounge per quarter on active account", 3.5, "Edge Points (25% off BookMyShow movies, ₹5 Lakhs daily purchase limit)", "Priority Banking Debit Card & Lounges", 0.5))
    raw_cards_data.append(c("axis_liberty_debit", "Axis Liberty Debit Card", "Axis Bank", "Mastercard", "DEBIT", 500, 0, False,
        "https://www.axisbank.com/liberty-debit.png", axis_logo, "#800020", "#00B4D8", 4.3,
        "No complimentary airport lounge access", 3.5, "Cashback (Flat 5% cashback on weekend shopping & food delivery, personal accident cover ₹5 Lakhs)", "Weekend Shopping & Dining Cashback", 0.5))

    # --- 6. KOTAK MAHINDRA BANK (12 cards) ---
    raw_cards_data.append(c("kotak_white_reserve", "Kotak White Reserve Credit Card", "Kotak Mahindra Bank", "Visa", "CREDIT", 12500, 0, False,
        "https://www.kotak.com/white-reserve.png", kotak_logo, "#1A1A1A", "#FFFFFF", 4.9,
        "Unlimited domestic & international airport lounges worldwide with complimentary guest passes", 1.5, "White Pass Vouchers (Luxury shopping vouchers worth up to ₹2.5 Lakhs on luxury brands)", "Luxury Retail Vouchers & VIP Lounges", 1.5))
    raw_cards_data.append(c("kotak_white", "Kotak White Credit Card", "Kotak Mahindra Bank", "Visa", "CREDIT", 3000, 500000, False,
        "https://www.kotak.com/white.png", kotak_logo, "#2C3E50", "#ECF0F1", 4.5,
        "4 complimentary domestic airport lounges per quarter", 3.5, "White Pass Vouchers (Up to ₹27,000 value vouchers on annual spend milestones)", "Milestone Vouchers & Domestic Lounges", 1.0))
    raw_cards_data.append(c("kotak_privy_league", "Kotak Privy League Black Signature", "Kotak Mahindra Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.kotak.com/privy-black.png", kotak_logo, "#0A0A0A", "#D4AF37", 4.8,
        "Unlimited complimentary domestic airport lounge access + free golf games", 2.0, "Privy Reward Points (5X rewards on apparel, travel & international)", "Wealth Banking Privileges & Golf", 1.0))
    raw_cards_data.append(c("kotak_zen", "Kotak Zen Signature Credit Card", "Kotak Mahindra Bank", "Visa", "CREDIT", 1500, 150000, False,
        "https://www.kotak.com/zen.png", kotak_logo, "#1B2A4A", "#9C27B0", 4.4,
        "2 complimentary domestic airport lounges per quarter", 3.5, "Zen Points (10 Zen points per ₹100 on apparel, lifestyle & jewelry)", "Fashion Lifestyle, Apparel & Jewelry", 0.75))
    raw_cards_data.append(c("kotak_mojo", "Kotak Mojo Platinum Credit Card", "Kotak Mahindra Bank", "Visa", "CREDIT", 1000, 100000, False,
        "https://www.kotak.com/mojo.png", kotak_logo, "#311B92", "#673AB7", 4.3,
        "8 complimentary domestic airport lounges per year (2 per quarter)", 3.5, "Mojo Points (2.5 Mojo points per ₹100 online, 1 Mojo pt = ₹0.40)", "Online Shopping & Domestic Lounges", 1.0))
    raw_cards_data.append(c("kotak_league", "Kotak League Platinum Credit Card", "Kotak Mahindra Bank", "Visa", "CREDIT", 500, 50000, False,
        "https://www.kotak.com/league.png", kotak_logo, "#B71C1C", "#D32F2F", 4.1,
        "No complimentary airport lounge access", 3.5, "Reward Points (4X points on special categories, annual fee waiver on ₹50k spend)", "Entry-level Everyday Spends", 0.5))
    raw_cards_data.append(c("kotak_811_dream", "Kotak 811 #DreamDifferent Credit Card", "Kotak Mahindra Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.kotak.com/811-dream.png", kotak_logo, "#D32F2F", "#1976D2", 4.3,
        "No complimentary airport lounge access", 3.5, "Reward Points (2 points per ₹100 online, 100% Lifetime Free secured against FD)", "First Time Cardholders & Zero CIBIL Check", 0.5))
    raw_cards_data.append(c("kotak_indianoil", "IndianOil Kotak Credit Card", "Kotak Mahindra Bank", "RuPay", "CREDIT", 449, 50000, False,
        "https://www.kotak.com/indianoil-kotak.png", kotak_logo, "#E65100", "#D32F2F", 4.3,
        "No complimentary airport lounge access", 3.5, "Reward Points (4% back on IndianOil fuel, 2% on grocery & dining, RuPay UPI)", "IndianOil Petrol & RuPay UPI", 0.5))
    raw_cards_data.append(c("kotak_pvr_inox_platinum", "PVR INOX Kotak Platinum Credit Card", "Kotak Mahindra Bank", "Visa", "CREDIT", 999, 0, False,
        "https://www.kotak.com/pvr-kotak.png", kotak_logo, "#000000", "#FFD700", 4.7,
        "No complimentary airport lounge access", 3.5, "PVR Movie Tickets (2 free PVR movie tickets every month on spending ₹10,000/mo)", "PVR INOX Free Cinema Tickets", 2.0))
    raw_cards_data.append(c("kotak_myntra", "Myntra Kotak Credit Card", "Kotak Mahindra Bank", "RuPay", "CREDIT", 500, 200000, False,
        "https://www.kotak.com/myntra-kotak.png", kotak_logo, "#E91E63", "#FF6F00", 4.6,
        "2 complimentary domestic airport lounges per year", 3.5, "Myntra Discount (Instant 7.5% discount on Myntra up to ₹750/txn)", "Myntra Fashion Shopping & RuPay UPI", 1.5))
    raw_cards_data.append(c("kotak_811_debit", "Kotak 811 Debit Card", "Kotak Mahindra Bank", "Visa", "DEBIT", 199, 0, False,
        "https://www.kotak.com/811-debit.png", kotak_logo, "#D32F2F", "#1976D2", 4.1,
        "No complimentary airport lounge access", 3.5, "Kotak Rewards (Virtual instant card in app, zero minimum balance account)", "Digital Zero Balance Account", 0.25))
    raw_cards_data.append(c("kotak_privy_debit", "Kotak Privy League Platinum Debit Card", "Kotak Mahindra Bank", "Visa", "DEBIT", 0, 0, True,
        "https://www.kotak.com/privy-debit.png", kotak_logo, "#0A0A0A", "#D4AF37", 4.6,
        "Complimentary domestic airport lounge access on active account", 3.5, "Reward Points (₹3 Lakhs daily ATM and POS transaction limit)", "Privy Wealth Banking Debit Lounges", 0.5))

    # --- 7. INDUSIND BANK (11 cards) ---
    raw_cards_data.append(c("indusind_pioneer_heritage", "IndusInd Pioneer Heritage Metal Credit Card", "IndusInd Bank", "Visa", "CREDIT", 90000, 0, False,
        "https://www.indusind.com/pioneer-heritage.png", indus_logo, "#0A0A0A", "#8D6E63", 4.9,
        "Unlimited domestic & international airport lounges worldwide with complimentary guest passes", 0.0, "Pioneer Reward Points (2.5 points per ₹100, 100% zero forex markup)", "Pioneer Wealth Banking, Zero Forex & Heritage Concierge", 2.5))
    raw_cards_data.append(c("indusind_pioneer_legacy", "IndusInd Pioneer Legacy Credit Card", "IndusInd Bank", "Visa", "CREDIT", 9999, 0, False,
        "https://www.indusind.com/pioneer-legacy.png", indus_logo, "#1A1A1A", "#B8860B", 4.6,
        "1 complimentary domestic lounge per quarter + Oberoi hotel stay vouchers", 2.0, "Reward Points (Buy 1 Get 1 on BookMyShow up to ₹200)", "Oberoi Luxury Stays & Lounges", 1.0))
    raw_cards_data.append(c("indusind_celesta", "IndusInd Celesta Credit Card", "IndusInd Bank", "Mastercard", "CREDIT", 50000, 0, False,
        "https://www.indusind.com/celesta.png", indus_logo, "#102030", "#C5A059", 4.8,
        "Unlimited domestic airport lounges + 3 international lounges per quarter via Priority Pass", 1.8, "Celesta Points (3 free movie tickets per month up to ₹350/ticket on BMS)", "Cinema BOGO & Unlimited Lounges", 1.5))
    raw_cards_data.append(c("indusind_pinnacle", "IndusInd Pinnacle Credit Card", "IndusInd Bank", "Visa", "CREDIT", 12999, 0, False,
        "https://www.indusind.com/pinnacle.png", indus_logo, "#263238", "#455A64", 4.5,
        "1 complimentary domestic lounge per quarter + golf games", 2.5, "Pinnacle Points (2.5% rewards on e-commerce, 1 free BMS ticket/mo)", "E-Commerce Shopping & Golf", 1.2))
    raw_cards_data.append(c("indusind_legend", "IndusInd Legend Credit Card", "IndusInd Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.indusind.com/legend.png", indus_logo, "#8B0000", "#FFD700", 4.5,
        "1 complimentary domestic airport lounge per quarter", 3.5, "Reward Points (1+1 movie tickets on BookMyShow up to ₹200/mo, 100% Lifetime Free)", "Lifetime Free Privileges & Movie BOGO", 0.5))
    raw_cards_data.append(c("indusind_nexxt", "IndusInd Nexxt Credit Card", "IndusInd Bank", "Mastercard", "CREDIT", 1499, 0, False,
        "https://www.indusind.com/nexxt.png", indus_logo, "#0D47A1", "#00E5FF", 4.6,
        "No complimentary airport lounge access", 3.5, "Push-button LED Card (Interactive 3 buttons on card: Pay with Rewards, Credit, or EMI)", "Interactive Card Technology & EMI", 0.5))
    raw_cards_data.append(c("indusind_platinum_aura_edge", "IndusInd Platinum Aura Edge Credit Card", "IndusInd Bank", "Visa", "CREDIT", 500, 0, True,
        "https://www.indusind.com/aura.png", indus_logo, "#37474F", "#78909C", 4.2,
        "No complimentary airport lounge access", 3.5, "Customizable Rewards (Up to 4X points on party, shop, travel, or home plans)", "Customizable Spend Plans", 0.5))
    raw_cards_data.append(c("indusind_tiger", "IndusInd Tiger Credit Card", "IndusInd Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.indusind.com/tiger.png", indus_logo, "#E65100", "#FFB74D", 4.5,
        "2 complimentary domestic airport lounges per quarter", 1.5, "Accelerated Rewards (Up to 6 points per ₹100, low 1.5% forex markup, Lifetime Free)", "Low Forex Markup & Domestic Lounges", 0.75))
    raw_cards_data.append(c("indusind_eazydiner", "EazyDiner IndusInd Bank Signature Credit Card", "IndusInd Bank", "Visa", "CREDIT", 1999, 0, False,
        "https://www.indusind.com/eazydiner.png", indus_logo, "#FF1744", "#D50000", 4.8,
        "2 complimentary domestic airport lounges per quarter", 3.5, "EazyPoints (Instant 25% discount on dining via EazyDiner Pay, free 1-yr EazyDiner Prime)", "Fine Dining Discounts & Restaurant Reservations", 2.0))
    raw_cards_data.append(c("indusind_vistara_explorer", "Club Vistara IndusInd Explorer Credit Card", "IndusInd Bank", "Visa", "CREDIT", 40000, 0, False,
        "https://www.indusind.com/vistara-explorer.png", indus_logo, "#4A148C", "#FFD700", 4.9,
        "16 complimentary domestic lounges + 16 international lounges per year via Priority Pass", 0.0, "CV Points (Complimentary Business Class Flight Ticket, 100% zero forex markup)", "Business Class Flights, Zero Forex & Luxury Travel", 2.0))
    raw_cards_data.append(c("indusind_exclusive_debit", "IndusInd Exclusive Debit Card", "IndusInd Bank", "Visa", "DEBIT", 0, 0, True,
        "https://www.indusind.com/exclusive-debit.png", indus_logo, "#8B0000", "#FFD700", 4.6,
        "2 complimentary domestic airport lounges per quarter", 3.5, "Reward Points (BookMyShow 1+1 ticket free, zero liability protection)", "Exclusive Debit Lounges & Cinema BOGO", 0.5))

    # --- 8. IDFC FIRST BANK (11 cards) ---
    raw_cards_data.append(c("idfc_first_private", "IDFC FIRST Private Metal Credit Card", "IDFC FIRST Bank", "Visa", "CREDIT", 49999, 0, False,
        "https://www.idfcfirstbank.com/private.png", idfc_logo, "#0A0A0A", "#8B0000", 4.9,
        "Unlimited domestic & international airport lounge & spa visits with complimentary guests", 0.0, "Reward Points (10X points, never expiring, 100% zero forex markup)", "Private Banking Metal, Zero Forex & Spa", 2.5))
    raw_cards_data.append(c("idfc_first_wealth", "IDFC FIRST Wealth Credit Card", "IDFC FIRST Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.idfcfirstbank.com/wealth.png", idfc_logo, "#8B0000", "#FFD700", 4.9,
        "Unlimited complimentary domestic airport lounge & spa visits + 4 international lounges via DreamFolks", 1.5, "Reward Points (10X points above ₹20k spend, Buy 1 Get 1 on BookMyShow up to ₹500 twice/mo, 100% Lifetime Free)", "Lifetime Free Luxury, Airport Spas & Cinema BOGO", 0.75))
    raw_cards_data.append(c("idfc_first_select", "IDFC FIRST Select Credit Card", "IDFC FIRST Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.idfcfirstbank.com/select.png", idfc_logo, "#1A237E", "#8B0000", 4.6,
        "4 complimentary domestic airport lounges per quarter", 1.99, "Reward Points (10X on spends above ₹20k, Buy 1 Get 1 on BookMyShow up to ₹250 twice/mo, Lifetime Free)", "Lifetime Free Domestic Lounges & Cinema", 0.5))
    raw_cards_data.append(c("idfc_first_classic", "IDFC FIRST Classic Credit Card", "IDFC FIRST Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.idfcfirstbank.com/classic.png", idfc_logo, "#37474F", "#8B0000", 4.3,
        "No complimentary airport lounge access", 3.5, "Reward Points (10X reward points on birthday spends, 100% Lifetime Free, dynamic APR from 9%)", "Lifetime Free Low Interest Rate", 0.5))
    raw_cards_data.append(c("idfc_first_millennia", "IDFC FIRST Millennia Credit Card", "IDFC FIRST Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.idfcfirstbank.com/millennia.png", idfc_logo, "#004D40", "#8B0000", 4.4,
        "No complimentary airport lounge access", 3.5, "Reward Points (10X points above ₹20k spend, 25% off cinema tickets up to ₹100, Lifetime Free)", "Lifetime Free Millennial Shopping", 0.5))
    raw_cards_data.append(c("idfc_first_wow", "IDFC FIRST WOW Credit Card", "IDFC FIRST Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.idfcfirstbank.com/wow.png", idfc_logo, "#FFD700", "#8B0000", 4.8,
        "No complimentary airport lounge access", 0.0, "Reward Points (100% ZERO Forex Markup everywhere worldwide, 100% Lifetime Free secured against FD, zero credit score required)", "Zero Forex Markup & Secured Card", 0.5))
    raw_cards_data.append(c("idfc_first_swyp", "IDFC FIRST SWYP Credit Card", "IDFC FIRST Bank", "Mastercard", "CREDIT", 499, 100000, False,
        "https://www.idfcfirstbank.com/swyp.png", idfc_logo, "#FF007F", "#8B0000", 4.4,
        "No complimentary airport lounge access", 3.5, "Reward Points (Curated subscription benefits on Lenskart, Gaana, Dominos, 10X points on milestone)", "Gen Z Youth Subscriptions", 0.5))
    raw_cards_data.append(c("idfc_first_power_plus", "IDFC FIRST Power+ Credit Card", "IDFC FIRST Bank", "RuPay", "CREDIT", 499, 150000, False,
        "https://www.idfcfirstbank.com/power-plus.png", idfc_logo, "#B71C1C", "#8B0000", 4.5,
        "No complimentary airport lounge access", 3.5, "Fuel Points (Up to 6.5% savings on HPCL fuel via HP Pay, 5% on groceries & utilities, RuPay UPI)", "HPCL Fuel & RuPay UPI", 0.5))
    raw_cards_data.append(c("idfc_club_vistara", "Club Vistara IDFC FIRST Credit Card", "IDFC FIRST Bank", "Mastercard", "CREDIT", 4999, 0, False,
        "https://www.idfcfirstbank.com/vistara.png", idfc_logo, "#4A148C", "#8B0000", 4.7,
        "2 complimentary domestic airport lounges + 1 international lounge per quarter", 3.5, "CV Points (Complimentary Premium Economy Flight Ticket, 6 CV points per ₹200)", "Vistara Flights & Airline Privileges", 1.0))
    raw_cards_data.append(c("idfc_first_mayura", "IDFC FIRST Mayura Metal Credit Card", "IDFC FIRST Bank", "Visa", "CREDIT", 5999, 0, False,
        "https://www.idfcfirstbank.com/mayura.png", idfc_logo, "#005F73", "#8B0000", 4.9,
        "4 complimentary domestic airport lounges & spa visits + 4 international lounges per quarter", 0.0, "Reward Points (100% ZERO Forex Markup, Buy 1 Get 1 on BookMyShow up to ₹500, metal card)", "Metal Travel Card, Zero Forex & Airport Spas", 1.5))
    raw_cards_data.append(c("idfc_first_signature_debit", "IDFC FIRST Visa Signature Debit Card", "IDFC FIRST Bank", "Visa", "DEBIT", 0, 0, True,
        "https://www.idfcfirstbank.com/signature-debit.png", idfc_logo, "#8B0000", "#D4AF37", 4.7,
        "Complimentary domestic airport lounge access on active account", 3.5, "Reward Points (2X reward points on online spends, daily limit ₹6 Lakhs)", "Lifetime Free Debit Card Lounges", 0.5))

    # --- 9. PUBLIC SECTOR BANKS (6 cards) ---
    raw_cards_data.append(c("pnb_rakshak_rupay", "PNB Rakshak RuPay Select Credit Card", "Punjab National Bank", "RuPay", "CREDIT", 0, 0, True,
        "https://www.pnbindia.in/rakshak.png", pnb_logo, "#8B0000", "#FFD700", 4.8,
        "Complimentary domestic airport lounge access + personal accidental insurance ₹50 Lakhs", 3.5, "Reward Points (Dedicated to Armed & Paramilitary Forces, zero joining & renewal fee)", "Defense & Paramilitary Forces", 0.5))
    raw_cards_data.append(c("pnb_rupay_select", "PNB RuPay Select Credit Card", "Punjab National Bank", "RuPay", "CREDIT", 500, 50000, False,
        "https://www.pnbindia.in/rupay-select.png", pnb_logo, "#8B0000", "#1A237E", 4.4,
        "2 complimentary domestic airport lounges per quarter + free spa session via RuPay", 3.5, "Reward Points (2X rewards on retail, spa sessions & gym access via RuPay)", "RuPay Select Wellness, Spas & Lounges", 0.5))
    raw_cards_data.append(c("pnb_rupay_platinum", "PNB RuPay Platinum Credit Card", "Punjab National Bank", "RuPay", "CREDIT", 250, 25000, False,
        "https://www.pnbindia.in/rupay-platinum.png", pnb_logo, "#8B0000", "#78909C", 4.1,
        "1 complimentary domestic airport lounge per quarter", 3.5, "Reward Points (Cashback on utility bill payments)", "Entry-level PSU Card & Bill Payments", 0.25))
    raw_cards_data.append(c("canara_rupay_select", "Canara RuPay Select Credit Card", "Canara Bank", "RuPay", "CREDIT", 1000, 100000, False,
        "https://www.canarabank.com/rupay-select.png", pnb_logo, "#005691", "#FFD700", 4.5,
        "2 complimentary domestic airport lounge visits per quarter + golf privileges", 3.5, "Reward Points (Free golf games, health checkups & ₹10 Lakhs accident cover)", "Canara Bank PSU Lounges & Wellness", 0.5))
    raw_cards_data.append(c("union_rupay_select", "Union Bank RuPay Select Credit Card", "Union Bank of India", "RuPay", "CREDIT", 499, 50000, False,
        "https://www.unionbankofindia.co.in/rupay.png", pnb_logo, "#C8102E", "#00205B", 4.4,
        "2 complimentary domestic airport lounges per quarter + wellness benefits", 3.5, "Reward Points (1% fuel waiver, free OTT subscriptions via RuPay)", "Union Bank PSU Privileges & Lounges", 0.5))
    raw_cards_data.append(c("boi_rupay_select", "Bank of India RuPay Select Credit Card", "Bank of India", "RuPay", "CREDIT", 750, 75000, False,
        "https://www.bankofindia.co.in/rupay.png", pnb_logo, "#E31837", "#002F6C", 4.3,
        "2 domestic airport lounges per quarter + health checkup vouchers", 3.5, "Star Rewardz (RuPay UPI scan & pay + domestic lounges)", "Bank of India PSU Card & UPI", 0.5))

    # --- 10. AU SMALL FINANCE BANK (8 cards) ---
    raw_cards_data.append(c("au_zenith_plus", "AU Zenith+ Super Premium Metal Credit Card", "AU Small Finance Bank", "Visa", "CREDIT", 4999, 800000, False,
        "https://www.aubank.in/zenith-plus.png", au_logo, "#101820", "#D4AF37", 4.9,
        "16 complimentary domestic airport & railway lounges + 16 international Priority Pass visits per year", 0.99, "Reward Points (Buy 1 Get 1 on BookMyShow up to ₹500, low 0.99% forex markup)", "Super Premium Metal, Low 0.99% Forex & Lounges", 1.25))
    raw_cards_data.append(c("au_zenith", "AU Zenith Credit Card", "AU Small Finance Bank", "Visa", "CREDIT", 7999, 500000, False,
        "https://www.aubank.in/zenith.png", au_logo, "#1F1F1F", "#FFD700", 4.7,
        "4 domestic lounges per quarter + 2 international lounges per quarter via Priority Pass", 1.99, "Reward Points (10,000 bonus reward points on milestone spends)", "Luxury Travel & Lifestyle", 1.0))
    raw_cards_data.append(c("au_vetta", "AU Vetta Credit Card", "AU Small Finance Bank", "Visa", "CREDIT", 2999, 150000, False,
        "https://www.aubank.in/vetta.png", au_logo, "#263238", "#43A047", 4.5,
        "2 complimentary domestic airport lounges per quarter", 3.5, "Reward Points (4 reward points per ₹100 on grocery/departmental, ₹1000 milestone vouchers)", "Grocery, Departmental & Milestones", 0.5))
    raw_cards_data.append(c("au_altura_plus", "AU Altura+ Credit Card", "AU Small Finance Bank", "Visa", "CREDIT", 499, 80000, False,
        "https://www.aubank.in/altura-plus.png", au_logo, "#0D47A1", "#00B0FF", 4.4,
        "2 complimentary domestic airport lounges per quarter", 3.5, "Reward Points (1.5% cashback on online spends, 100 reward points on activation)", "Online Shopping & Domestic Lounges", 0.5))
    raw_cards_data.append(c("au_altura", "AU Altura Credit Card", "AU Small Finance Bank", "Visa", "CREDIT", 199, 40000, False,
        "https://www.aubank.in/altura.png", au_logo, "#37474F", "#00E5FF", 4.1,
        "No complimentary airport lounge access", 3.5, "Reward Points (2 reward points per ₹100 on retail, annual fee waiver on ₹40k spend)", "Entry-level Retail Shopping", 0.25))
    raw_cards_data.append(c("au_lit", "AU LIT Credit Card", "AU Small Finance Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.aubank.in/lit.png", au_logo, "#FF007F", "#7928CA", 4.8,
        "Customizable on-demand: Toggle airport lounge access on/off when traveling", 3.5, "Dynamic Customizable Cashbacks (India's 1st customizable credit card: toggle 5% cashback, lounge, or fuel waiver on-demand in app)", "100% Dynamic On-Demand Feature Toggling", 1.0))
    raw_cards_data.append(c("ixigo_au", "Ixigo AU Credit Card", "AU Small Finance Bank", "Visa", "CREDIT", 999, 100000, False,
        "https://www.aubank.in/ixigo.png", au_logo, "#00A86B", "#FF6F00", 4.9,
        "16 complimentary airport & railway lounges per year (Domestic & Railway)", 0.0, "Reward Points (100% ZERO Forex Markup on all international spends, 10% instant discount on train & flight bookings on Ixigo)", "Zero Forex Markup, Train & Flight Bookings", 1.0))
    raw_cards_data.append(c("au_royale_debit", "AU Royale Debit Card", "AU Small Finance Bank", "Visa", "DEBIT", 0, 0, True,
        "https://www.aubank.in/royale-debit.png", au_logo, "#101820", "#D4AF37", 4.6,
        "Complimentary airport lounge access on active account", 2.0, "Cashback (1% cashback on all international spends, ₹2 Lakhs daily ATM limit)", "Royale Wealth Debit Card & Free Lounges", 0.5))

    # --- 11. RBL BANK (8 cards) ---
    raw_cards_data.append(c("rbl_world_safari", "RBL World Safari Credit Card", "RBL Bank", "Mastercard", "CREDIT", 3000, 0, False,
        "https://www.rblbank.com/world-safari.png", rbl_logo, "#0D47A1", "#00A86B", 4.9,
        "2 complimentary domestic airport lounges per quarter + Priority Pass membership", 0.0, "Travel Points (100% ZERO Forex Markup on all foreign currency transactions worldwide)", "Zero Forex Travel Card & Domestic Lounges", 1.0))
    raw_cards_data.append(c("rbl_insignia", "RBL Insignia Preferred Banking Credit Card", "RBL Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.rblbank.com/insignia.png", rbl_logo, "#0A0A0A", "#FFD700", 4.8,
        "Unlimited complimentary domestic airport lounge access for primary cardholder", 1.5, "Reward Points (2 free movie tickets every month on BookMyShow up to ₹500/ticket, Lifetime Free)", "Insignia Wealth Banking & BMS Free Tickets", 1.25))
    raw_cards_data.append(c("rbl_icon", "RBL Icon Credit Card", "RBL Bank", "Mastercard", "CREDIT", 5000, 300000, False,
        "https://www.rblbank.com/icon.png", rbl_logo, "#1C2A39", "#C5A059", 4.6,
        "2 complimentary domestic airport lounges per quarter + complimentary golf games", 3.5, "Icon Reward Points (Buy 1 Get 1 on BookMyShow up to ₹200 twice a month)", "Luxury Dining, Golf & Cinema BOGO", 1.0))
    raw_cards_data.append(c("rbl_shoprite", "RBL ShopRite Credit Card", "RBL Bank", "Mastercard", "CREDIT", 500, 150000, False,
        "https://www.rblbank.com/shoprite.png", rbl_logo, "#880E4F", "#FF4081", 4.2,
        "No complimentary airport lounge access", 3.5, "Reward Points (5% valueback on groceries up to 20 reward points per ₹100, 2.5% on fuel)", "Grocery Spends & Fuel Surcharge Waiver", 0.5))
    raw_cards_data.append(c("rbl_play", "RBL Play Credit Card", "RBL Bank", "Mastercard", "CREDIT", 500, 0, False,
        "https://www.rblbank.com/play.png", rbl_logo, "#C2185B", "#E91E63", 4.7,
        "No complimentary airport lounge access", 3.5, "BookMyShow Movie Tickets (2 free BookMyShow movie tickets worth ₹250 each every month on spending ₹5000)", "BookMyShow 2 Free Movie Tickets Every Month", 2.0))
    raw_cards_data.append(c("rbl_cookies", "RBL Cookies Credit Card", "RBL Bank", "Mastercard", "CREDIT", 100, 150000, False,
        "https://www.rblbank.com/cookies.png", rbl_logo, "#4A148C", "#AB47BC", 4.3,
        "No complimentary airport lounge access", 3.5, "Reward Points (10% discount on Zomato, BookMyShow, Myntra up to ₹300/mo)", "Gen Z Food & Entertainment Discounts", 0.5))
    raw_cards_data.append(c("rbl_supercard_platinum_choice", "Bajaj Finserv RBL SuperCard Platinum Choice", "RBL Bank", "Mastercard", "CREDIT", 499, 50000, False,
        "https://www.rblbank.com/supercard-platinum.png", rbl_logo, "#003366", "#FF6F00", 4.4,
        "No complimentary airport lounge access", 3.5, "Reward Points (4-in-1 card: Credit, emergency cash, EMI card, personal loan, 1 free BMS ticket/mo)", "4-in-1 SuperCard (Credit + Cash + EMI + Loan)", 0.5))
    raw_cards_data.append(c("rbl_supercard_binge", "Bajaj Finserv RBL Binge SuperCard", "RBL Bank", "Mastercard", "CREDIT", 999, 100000, False,
        "https://www.rblbank.com/supercard-binge.png", rbl_logo, "#002040", "#00C853", 4.5,
        "1 complimentary domestic airport lounge per quarter", 3.5, "Reward Points (12 free BookMyShow movie tickets per year, 12X points on online shopping)", "Cinema BOGO & 4-in-1 SuperCard", 0.75))

    # --- 12. FEDERAL BANK & YES BANK (8 cards) ---
    raw_cards_data.append(c("federal_celesta", "Federal Bank Celesta Credit Card", "Federal Bank", "Visa", "CREDIT", 3000, 300000, False,
        "https://www.federalbank.co.in/celesta.png", fed_logo, "#0D47A1", "#FFD700", 4.6,
        "2 complimentary domestic & 1 international airport lounge per quarter", 2.0, "Reward Points (Buy 1 Get 1 on BookMyShow up to ₹200, 3X on dining & travel)", "Premium Travel, Dining & Cinema", 0.75))
    raw_cards_data.append(c("federal_imperio", "Federal Bank Imperio Credit Card", "Federal Bank", "Mastercard", "CREDIT", 1500, 150000, False,
        "https://www.federalbank.co.in/imperio.png", fed_logo, "#1A237E", "#3949AB", 4.3,
        "2 complimentary domestic airport lounges per quarter on spending ₹20k in previous quarter", 3.5, "Reward Points (3X points on grocery & health, ₹100 off BookMyShow)", "Groceries, Healthcare & Cinema", 0.5))
    raw_cards_data.append(c("federal_signet", "Federal Bank Signet Credit Card", "Federal Bank", "Visa", "CREDIT", 750, 75000, False,
        "https://www.federalbank.co.in/signet.png", fed_logo, "#283593", "#5C6BC0", 4.2,
        "1 complimentary domestic airport lounge per quarter on spending ₹20k in previous quarter", 3.5, "Reward Points (Buy 1 Get 1 on BookMyShow once a month, 3X on electronics)", "Electronics, Cinema BOGO & Lounges", 0.5))
    raw_cards_data.append(c("scapia_federal", "Scapia Federal Credit Card", "Federal Bank", "Visa", "CREDIT", 0, 0, True,
        "https://www.scapia.cards/scapia.png", fed_logo, "#0B0C10", "#00FF87", 4.9,
        "Unlimited complimentary domestic airport lounge access on minimum ₹5000 monthly spend", 0.0, "Scapia Coins (100% ZERO Forex Markup worldwide, 10% Scapia Coins on flight/hotel bookings)", "Zero Forex International Travel & Airport Lounges", 2.0,
        [{"category": "travel", "cashbackPercent": 10.0, "monthlyCapInr": None}, {"category": "online_general", "cashbackPercent": 2.0, "monthlyCapInr": None}, {"category": "offline_general", "cashbackPercent": 2.0, "monthlyCapInr": None}]))
    raw_cards_data.append(c("yes_marquee", "Yes Bank Marquee Credit Card", "Yes Bank", "Visa", "CREDIT", 9999, 1000000, False,
        "https://www.yesbank.in/marquee.png", yes_logo, "#0A0A0A", "#002F6C", 4.9,
        "Unlimited domestic airport lounges for primary cardholder and guests + 6 international lounges per quarter", 1.0, "Reward Points (Buy 1 Get 1 on BookMyShow up to ₹800, low 1.0% forex markup)", "Ultra Luxury Wealth Banking, BMS BOGO & Lounges", 2.25))
    raw_cards_data.append(c("yes_reserv", "Yes Bank Reserv Credit Card", "Yes Bank", "Visa", "CREDIT", 1999, 300000, False,
        "https://www.yesbank.in/reserv.png", yes_logo, "#002F6C", "#00B4D8", 4.5,
        "3 complimentary domestic airport lounges per quarter + 3 international lounges per year via Priority Pass", 2.5, "Reward Points (3X points on subscription apps, Buy 1 Get 1 on BookMyShow)", "OTT Subscriptions & Domestic Lounges", 0.75))
    raw_cards_data.append(c("yes_elite", "Yes Bank Elite Credit Card", "Yes Bank", "Visa", "CREDIT", 999, 150000, False,
        "https://www.yesbank.in/elite.png", yes_logo, "#003366", "#29B6F6", 4.3,
        "2 complimentary domestic airport lounges per quarter on spending ₹50k in previous quarter", 3.0, "Reward Points (12 reward points per ₹200 on online retail)", "Retail Online Shopping & Lounges", 0.5))
    raw_cards_data.append(c("kiwi_yes_bank", "Kiwi Yes Bank RuPay Virtual Credit Card", "Yes Bank", "RuPay", "CREDIT", 0, 0, True,
        "https://www.gokiwi.in/kiwi.png", yes_logo, "#7C3AED", "#10B981", 4.9,
        "No complimentary airport lounge access", 3.5, "Kiwi Kiwis (Flat 2% cashback on UPI Scan & Pay via Kiwi app, 100% Lifetime Free virtual card)", "UPI Scan & Pay Flat 2% Cashback Everywhere", 2.0,
        [{"category": "online_general", "cashbackPercent": 2.0, "monthlyCapInr": 1000}, {"category": "offline_general", "cashbackPercent": 2.0, "monthlyCapInr": 1000}]))

    # --- 13. STANDARD CHARTERED, HSBC & AMERICAN EXPRESS (12 cards) ---
    raw_cards_data.append(c("sc_ultimate", "Standard Chartered Ultimate Credit Card", "Standard Chartered", "Visa", "CREDIT", 5000, 0, False,
        "https://www.sc.com/ultimate.png", sc_logo, "#003865", "#00A859", 4.8,
        "1 complimentary domestic airport lounge per month + 1 international lounge per month via Priority Pass", 2.0, "Reward Points (Flat 3.33% reward rate on all domestic & international spends, golf rounds)", "Flat 3.33% Reward Rate on All Spends & Golf", 3.33))
    raw_cards_data.append(c("sc_easemytrip", "Standard Chartered EaseMyTrip Credit Card", "Standard Chartered", "Visa", "CREDIT", 350, 50000, False,
        "https://www.sc.com/easemytrip.png", sc_logo, "#0088CC", "#00A859", 4.5,
        "1 complimentary domestic airport lounge per quarter + 1 international lounge per quarter", 3.5, "EaseMyTrip Instant Discount (Flat 20% off hotel bookings, 10% off flights on EaseMyTrip)", "EaseMyTrip Flight & Hotel Booking Discounts", 1.0))
    raw_cards_data.append(c("sc_smart", "Standard Chartered Smart Credit Card", "Standard Chartered", "Visa", "CREDIT", 499, 120000, False,
        "https://www.sc.com/smart.png", sc_logo, "#003865", "#4CAF50", 4.4,
        "No complimentary airport lounge access", 3.5, "Direct Cashback (2% flat cashback on all online shopping up to ₹1000/mo, 1% on offline)", "Online Shopping Flat 2% Cashback & Low APR", 2.0))
    raw_cards_data.append(c("hsbc_premier", "HSBC Premier Credit Card", "HSBC India", "Mastercard", "CREDIT", 0, 0, True,
        "https://www.hsbc.co.in/premier.png", hsbc_logo, "#DB0011", "#1A1A1A", 4.9,
        "Unlimited domestic & international airport lounges worldwide with complimentary guest passes", 1.99, "Reward Points (Complimentary golf games, dining airport privileges, Lifetime Free for Premier clients)", "HSBC Premier Wealth Banking & Free Lounges", 1.5))
    raw_cards_data.append(c("hsbc_live_plus", "HSBC Live+ Credit Card", "HSBC India", "Visa", "CREDIT", 999, 200000, False,
        "https://www.hsbc.co.in/live-plus.png", hsbc_logo, "#DB0011", "#4A90E2", 4.8,
        "1 complimentary domestic airport lounge per quarter on spending ₹20k in previous 3 months", 3.5, "Direct Cashback (10% accelerated cashback on dining, food delivery & groceries up to ₹1000/mo, 1.5% unlimited elsewhere)", "Dining, Zomato/Swiggy Delivery & Groceries", 10.0,
        [{"category": "dining", "cashbackPercent": 10.0, "monthlyCapInr": 1000}, {"category": "online_general", "cashbackPercent": 1.5, "monthlyCapInr": None}, {"category": "offline_general", "cashbackPercent": 1.5, "monthlyCapInr": None}]))
    raw_cards_data.append(c("hsbc_platinum", "HSBC Platinum Credit Card", "HSBC India", "Visa", "CREDIT", 0, 0, True,
        "https://www.hsbc.co.in/platinum.png", hsbc_logo, "#DB0011", "#78909C", 4.2,
        "No complimentary airport lounge access", 3.5, "Reward Points (2 reward points per ₹100, ₹2000 Amazon voucher on activation, Lifetime Free)", "Lifetime Free Introductory Card", 0.5))
    raw_cards_data.append(c("amex_centurion", "American Express Centurion Card", "American Express", "Amex", "CREDIT", 275000, 0, False,
        "https://www.americanexpress.com/centurion.png", amex_logo, "#000000", "#1A1A1A", 5.0,
        "Unlimited Centurion, Delta SkyClub, Priority Pass lounges worldwide with unlimited guest access", 3.5, "Centurion Membership Rewards (Black titanium card, invite only, 24/7 dedicated personal concierge)", "Invite-Only Black Titanium Luxury & Concierge", 2.5))
    raw_cards_data.append(c("amex_platinum_charge", "American Express Platinum Charge Metal Card", "American Express", "Amex", "CREDIT", 60000, 0, False,
        "https://www.americanexpress.com/plat-charge.png", amex_logo, "#71797E", "#B0C4DE", 4.9,
        "Unlimited Centurion Lounges, Priority Pass lounges, Delta SkyClubs worldwide for primary & supplementary", 3.5, "Membership Rewards (Metal charge card, Marriott Gold, Hilton Gold, Radisson Gold, Taj ₹45,000 vouchers)", "Luxury Metal Card, Hotel Elite Status & VIP Lounges", 1.5))
    raw_cards_data.append(c("amex_gold", "American Express Gold Card", "American Express", "Amex", "CREDIT", 4500, 0, False,
        "https://www.americanexpress.com/gold.png", amex_logo, "#FFD700", "#B8860B", 4.6,
        "No complimentary airport lounge access", 3.5, "Membership Rewards (1,000 bonus MR points on completing 6 transactions of ₹1000+ per month)", "Monthly Transaction Milestone Multipliers", 1.0))
    raw_cards_data.append(c("amex_platinum_travel", "American Express Platinum Travel Credit Card", "American Express", "Amex", "CREDIT", 5000, 0, False,
        "https://www.americanexpress.com/plat-travel.png", amex_logo, "#2B4C7E", "#607D8B", 4.8,
        "8 complimentary domestic airport lounges per year (2 per quarter)", 3.5, "Membership Rewards (48,000 bonus MR points + ₹10,000 Taj Hotel stay voucher upon ₹4 Lakhs annual spend)", "Milestone Rewards & ₹10,000 Taj Hotel Stay Voucher", 1.25))
    raw_cards_data.append(c("amex_mrcc", "American Express Membership Rewards Credit Card", "American Express", "Amex", "CREDIT", 4500, 150000, False,
        "https://www.americanexpress.com/mrcc.png", amex_logo, "#006FCF", "#002244", 4.7,
        "No complimentary airport lounge access", 3.5, "Membership Rewards (1,000 bonus points on 4 swipes of ₹1500 + 1,000 bonus points on ₹20k monthly spend)", "18k & 24k Karat Gold Collection Points Multiplier", 1.0))
    raw_cards_data.append(c("amex_smartearn", "American Express SmartEarn Credit Card", "American Express", "Amex", "CREDIT", 495, 40000, False,
        "https://www.americanexpress.com/smartearn.png", amex_logo, "#006FCF", "#4A90E2", 4.4,
        "No complimentary airport lounge access", 3.5, "Membership Rewards (10X MR points on Flipkart, Amazon, Uber up to 1000 pts/mo, 5X on Paytm, Swiggy)", "Entry-level Amex Card & 10X Points", 0.5))

    # --- 14. FINTECH & NEO-BANKS (8 cards) ---
    raw_cards_data.append(c("onecard_metal", "OneCard Metal Credit Card", "OneCard", "Visa", "CREDIT", 0, 0, True,
        "https://getonecard.app/images/metal.png", fed_logo, "#1A1A1A", "#333333", 4.7,
        "Domestic airport lounges available on premium qualifying tiers", 1.0, "OneCard Points (Precision metal card, 5X rewards on top 2 spend categories every month, Lifetime Free)", "Precision Metal Card & 5X Category Rewards", 1.0))
    raw_cards_data.append(c("uni_nx_wave", "Uni NX Wave Credit Card", "Uni", "Visa", "CREDIT", 0, 0, True,
        "https://www.uni.cards/nx-wave.png", fed_logo, "#0F0F1A", "#6C5CE7", 4.6,
        "No complimentary airport lounge access", 0.0, "Direct Cashback (100% ZERO Forex Markup worldwide, 1% cashback or split payment into 1/3rd with zero interest)", "Zero Forex Markup & Split in 3 Feature", 1.0))
    raw_cards_data.append(c("jupiter_edge_rupay", "Jupiter Edge RuPay Credit Card", "Jupiter", "RuPay", "CREDIT", 0, 0, True,
        "https://jupiter.money/edge-card.png", fed_logo, "#FF5E3A", "#FF2A6D", 4.7,
        "No complimentary airport lounge access", 3.5, "Jupiter Jewels (Flat 2% cashback on UPI Scan & Pay via Jupiter app, 100% Lifetime Free virtual card)", "UPI Scan & Pay Flat 2% Cashback", 2.0))
    raw_cards_data.append(c("fi_amplifi", "Fi Amplifi Credit Card", "Fi Money", "Visa", "CREDIT", 2000, 250000, False,
        "https://fi.money/amplifi.png", fed_logo, "#00E676", "#121212", 4.6,
        "1 complimentary domestic airport lounge per quarter", 0.0, "Fi Coins (3% rewards on curated top 20 lifestyle brands, zero forex markup on Infinite tier)", "Curated Brand Rewards & Low Forex", 1.5))
    raw_cards_data.append(c("fi_infinite_debit", "Fi Money Infinite Debit Card", "Fi Money", "Visa", "DEBIT", 0, 0, True,
        "https://fi.money/infinite-debit.png", fed_logo, "#00E676", "#050505", 4.8,
        "1 complimentary domestic airport lounge per quarter on salary account", 0.0, "Fi Coins (100% ZERO Forex Markup worldwide, zero balance salary account, smart deposits)", "Zero Forex Travel Debit Card & Free Lounges", 0.5))
    raw_cards_data.append(c("jupiter_federal_debit", "Jupiter Federal Debit Card", "Jupiter", "Visa", "DEBIT", 0, 0, True,
        "https://jupiter.money/debit-card.png", fed_logo, "#FF6B6B", "#2E384D", 4.4,
        "No complimentary airport lounge access", 3.5, "Jupiter Jewels (1% flat rewards on debit card shopping, zero forex markup on Salary tier)", "Smart Banking Debit Card & 1% Rewards", 0.5))
    raw_cards_data.append(c("niyo_global_debit", "Niyo Global Equitas / DCB Debit Card", "Niyo", "Visa", "DEBIT", 0, 0, True,
        "https://www.goniyo.com/global-card.png", fed_logo, "#0052CC", "#2684FF", 4.9,
        "Free international airport lounge access at Indian departure terminals on qualifying international flights", 0.0, "Niyo Coins (100% ZERO Forex Markup on POS, ATM, and e-commerce transactions across 150+ countries)", "Zero Forex International Travel Debit Card", 0.5))
    raw_cards_data.append(c("slice_spark", "Slice Spark Card", "Slice", "RuPay", "CREDIT", 0, 0, True,
        "https://sliceit.com/spark-card.png", fed_logo, "#9D4EDD", "#5A189A", 4.5,
        "No complimentary airport lounge access", 3.5, "Monies (Instant daily fire offers with up to 30% cashback on merchant QR codes and grocery apps)", "Daily Fire Offers & Merchant QR Codes", 1.0))

    # Save cards catalog container
    cards_container = {
        "version": "2.0.0",
        "lastUpdated": "2026-11-08T16:20:00Z",
        "totalCards": len(raw_cards_data),
        "cards": raw_cards_data,
        "liveOffers": [
            {"id": "off_swiggy_hdfc", "cardId": "hdfc_swiggy", "merchantName": "Swiggy", "title": "10% Flat Cashback on Food, Instamart & Genie", "cashbackPercent": 10.0, "validTill": "31 Dec 2026", "terms": "Capped at ₹1500 per calendar month."},
            {"id": "off_flipkart_axis", "cardId": "flipkart_axis", "merchantName": "Flipkart", "title": "5% Unlimited Cashback on Big Billion Days", "cashbackPercent": 5.0, "validTill": "31 Dec 2026", "terms": "Zero cap, directly credited to monthly statement."},
            {"id": "off_amazon_icici", "cardId": "amazon_pay_icici", "merchantName": "Amazon", "title": "5% Unlimited Cashback for Prime Members", "cashbackPercent": 5.0, "validTill": "Ongoing", "terms": "Credited as Amazon Pay balance every billing cycle."},
            {"id": "off_bob_eterna", "cardId": "bob_eterna", "merchantName": "BookMyShow", "title": "Buy 1 Get 1 Free Cinema Tickets (Up to ₹250)", "cashbackPercent": 25.0, "validTill": "Ongoing", "terms": "Valid twice a month across BookMyShow & Inox."},
            {"id": "off_scapia_travel", "cardId": "scapia_federal", "merchantName": "Scapia Travel", "title": "10% Valueback as Scapia Coins on Flights & Hotels", "cashbackPercent": 10.0, "validTill": "Ongoing", "terms": "Zero forex markup on all overseas swipes."}
        ]
    }

    # -------------------------------------------------------------------------
    # 2. GENERATE 35+ AIRPORT & RAILWAY LOUNGES ACROSS INDIA
    # -------------------------------------------------------------------------
    lounges_list = [
        # Delhi
        {"id": "del_t3_dom_encalm", "name": "Encalm Lounge (Domestic)", "city": "Delhi NCR", "airportOrStation": "Indira Gandhi International Airport (DEL)", "terminal": "Terminal 3", "type": "AIRPORT_DOMESTIC", "criteria": "SPEND_LINKED", "spendRequirementNote": "HDFC & ICICI cards require ₹10k–₹50k spend in previous quarter. Scapia, BoB Eterna & Axis Atlas unconditional.", "eligibleCardIds": ["scapia_federal", "axis_atlas", "bob_eterna", "tata_neu_infinity", "hdfc_millennia", "amex_platinum_travel"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay", "Amex"], "timings": "24 Hours", "locationDetail": "Mezzanine Level, Near Food Court, Post-Security", "amenities": ["Gourmet Buffet", "High Speed WiFi", "Live Cooking Station", "Flight Monitors", "Charging Docks"]},
        {"id": "del_t3_intl_encalm", "name": "Encalm Prive Lounge (International)", "city": "Delhi NCR", "airportOrStation": "Indira Gandhi International Airport (DEL)", "terminal": "Terminal 3", "type": "AIRPORT_INTERNATIONAL", "criteria": "PRIORITY_PASS", "spendRequirementNote": "Complimentary via Priority Pass / DreamFolks or premium tiers (Axis Atlas, Tata Neu Infinity, BoB Varunah, Amex Plat Charge).", "eligibleCardIds": ["axis_atlas", "tata_neu_infinity", "bob_varunah", "amex_platinum_travel", "amex_platinum_charge"], "eligibleNetworks": ["Visa", "Mastercard", "Amex"], "timings": "24 Hours", "locationDetail": "International Departures, Level 3, Near Gate 15", "amenities": ["Luxury Buffet", "Bar & Cocktails", "Shower Rooms", "Nap Pods", "High Speed WiFi"]},
        {"id": "del_t1_dom_encalm", "name": "Encalm Lounge (Domestic)", "city": "Delhi NCR", "airportOrStation": "Indira Gandhi International Airport (DEL)", "terminal": "Terminal 1", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on BoB Eterna, Axis Atlas, Scapia Federal & Tata Neu Infinity.", "eligibleCardIds": ["bob_eterna", "axis_atlas", "scapia_federal", "tata_neu_infinity", "au_zenith_plus"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Level 2, Post-Security, Domestic Departures", "amenities": ["Hot Buffet", "WiFi", "Bar Area", "Workstations"]},
        {"id": "del_t2_dom_encalm", "name": "Encalm Lounge (Domestic)", "city": "Delhi NCR", "airportOrStation": "Indira Gandhi International Airport (DEL)", "terminal": "Terminal 2", "type": "AIRPORT_DOMESTIC", "criteria": "SPEND_LINKED", "spendRequirementNote": "Bank spend criteria applies. Direct entry on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "hdfc_regalia_gold"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "First Floor, Post-Security, Near Gate 21", "amenities": ["Buffet", "WiFi", "Beverages", "Flight Displays"]},
        
        # Mumbai
        {"id": "bom_t2_dom_adani", "name": "Adani Lounge (Domestic)", "city": "Mumbai", "airportOrStation": "Chhatrapati Shivaji Maharaj International Airport (BOM)", "terminal": "Terminal 2", "type": "AIRPORT_DOMESTIC", "criteria": "SPEND_LINKED", "spendRequirementNote": "ICICI cards need ₹10k spend in previous quarter. BoB Eterna, Axis Atlas, Scapia give direct entry.", "eligibleCardIds": ["bob_eterna", "axis_atlas", "scapia_federal", "tata_neu_infinity", "hdfc_millennia", "hsbc_live_plus"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Level 3, Domestic Departures, Opposite Gate 44", "amenities": ["Hot Buffet", "Bar Counter", "Business Desks", "Flight Info Displays"]},
        {"id": "bom_t2_intl_adani", "name": "Adani First & Business Class Lounge (Intl)", "city": "Mumbai", "airportOrStation": "Chhatrapati Shivaji Maharaj International Airport (BOM)", "terminal": "Terminal 2", "type": "AIRPORT_INTERNATIONAL", "criteria": "PRIORITY_PASS", "spendRequirementNote": "Available via Priority Pass / DreamFolks network on Axis Atlas, BoB Varunah & Amex Platinum Travel.", "eligibleCardIds": ["axis_atlas", "bob_varunah", "amex_platinum_travel", "tata_neu_infinity", "idfc_first_wealth"], "eligibleNetworks": ["Visa", "Mastercard", "Amex"], "timings": "24 Hours", "locationDetail": "Level 4, International Departures, Post-Security", "amenities": ["Chef Buffet", "Premium Liquor", "Spa Showers", "Relaxation Zones"]},
        {"id": "bom_t1_dom_oasis", "name": "Oasis Lounge (Domestic)", "city": "Mumbai", "airportOrStation": "Chhatrapati Shivaji Maharaj International Airport (BOM)", "terminal": "Terminal 1", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary swipe on BoB Eterna, Tata Neu Infinity, Scapia Federal & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "tata_neu_infinity", "scapia_federal", "axis_atlas", "au_zenith_plus"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Terminal 1B, Near Gate 2, Post-Security", "amenities": ["Warm Snacks", "Coffee Bar", "WiFi", "Newspapers"]},

        # Bengaluru
        {"id": "blr_t1_dom_080", "name": "080 Domestic Lounge", "city": "Bengaluru", "airportOrStation": "Kempegowda International Airport (BLR)", "terminal": "Terminal 1", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "₹2 swipe for Scapia, BoB Eterna, Axis Atlas, and Tata Neu Infinity. HDFC/ICICI require spend qualification.", "eligibleCardIds": ["scapia_federal", "bob_eterna", "axis_atlas", "tata_neu_infinity", "hdfc_infinia", "icici_emeralde_private"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Mezzanine Floor, Post-Security, Domestic Terminal", "amenities": ["Artisan Kitchen", "Cocktail Bar", "Quiet Zone", "High Speed Internet", "Shower Suites"]},
        {"id": "blr_t2_dom_080", "name": "080 Domestic Lounge (Terminal in a Garden)", "city": "Bengaluru", "airportOrStation": "Kempegowda International Airport (BLR)", "terminal": "Terminal 2", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Direct ₹2 swipe on BoB Eterna, Scapia, Axis Atlas, Tata Neu Infinity. Spend threshold applies to standard cards.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity", "hdfc_regalia_gold", "idfc_first_wealth"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay", "Amex"], "timings": "24 Hours", "locationDetail": "Level 4, Terminal 2 Departures, Near Gate 12", "amenities": ["Forest View Terraces", "Gourmet Dining", "Barista Coffee", "Private Suites"]},
        {"id": "blr_t2_intl_080", "name": "080 International Lounge", "city": "Bengaluru", "airportOrStation": "Kempegowda International Airport (BLR)", "terminal": "Terminal 2", "type": "AIRPORT_INTERNATIONAL", "criteria": "PRIORITY_PASS", "spendRequirementNote": "Complimentary via Priority Pass / DreamFolks network on BoB Varunah, Axis Atlas & Amex.", "eligibleCardIds": ["bob_varunah", "axis_atlas", "amex_platinum_travel", "hdfc_infinia"], "eligibleNetworks": ["Visa", "Mastercard", "Amex"], "timings": "24 Hours", "locationDetail": "Level 3, International Departures, Post-Security", "amenities": ["Luxury Buffet", "Premium Liquor", "Shower Rooms", "Nap Pods"]},

        # Hyderabad
        {"id": "hyd_dom_encalm", "name": "Encalm Lounge (Domestic)", "city": "Hyderabad", "airportOrStation": "Rajiv Gandhi International Airport (HYD)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "SPEND_LINKED", "spendRequirementNote": "Spend qualification required for ICICI & HDFC cards. Unconditional on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity", "hdfc_millennia"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Near Gate 21, Post-Security, Domestic Terminal", "amenities": ["Hyderabadi Biryani Counter", "Beverage Station", "WiFi", "Relaxation Loungers"]},
        {"id": "hyd_intl_plaza", "name": "Plaza Premium Lounge (International)", "city": "Hyderabad", "airportOrStation": "Rajiv Gandhi International Airport (HYD)", "terminal": "International Departures", "type": "AIRPORT_INTERNATIONAL", "criteria": "PRIORITY_PASS", "spendRequirementNote": "Priority Pass and DreamFolks eligible.", "eligibleCardIds": ["axis_atlas", "bob_varunah", "amex_platinum_travel"], "eligibleNetworks": ["Visa", "Mastercard", "Amex"], "timings": "24 Hours", "locationDetail": "Level E, International Departures, Near Gate 32", "amenities": ["Full Bar", "Hot Buffet", "Shower Facilities", "WiFi"]},

        # Chennai
        {"id": "maa_t1_travel_club", "name": "Travel Club Lounge (Domestic T1)", "city": "Chennai", "airportOrStation": "Chennai International Airport (MAA)", "terminal": "Terminal 1", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Direct ₹2 swipe on BoB Eterna, Scapia Federal & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity", "au_zenith_plus"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "First Floor, Post-Security, Domestic Departures", "amenities": ["South Indian Tiffin", "Buffet", "WiFi", "Workstations"]},
        {"id": "maa_t4_intl_travel_club", "name": "Travel Club Lounge (International T4)", "city": "Chennai", "airportOrStation": "Chennai International Airport (MAA)", "terminal": "Terminal 4", "type": "AIRPORT_INTERNATIONAL", "criteria": "PRIORITY_PASS", "spendRequirementNote": "Priority Pass & DreamFolks lounge card.", "eligibleCardIds": ["axis_atlas", "bob_varunah", "amex_platinum_travel"], "eligibleNetworks": ["Visa", "Mastercard", "Amex"], "timings": "24 Hours", "locationDetail": "Level 3, International Departures, Post-Security", "amenities": ["Buffet", "Bar", "Showers", "Flight Displays"]},

        # Kolkata
        {"id": "ccu_dom_travel_club", "name": "Travel Club Lounge (Domestic)", "city": "Kolkata", "airportOrStation": "Netaji Subhash Chandra Bose International Airport (CCU)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "SPEND_LINKED", "spendRequirementNote": "Eligible via BoB Eterna, Scapia, Tata Neu Infinity and Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "tata_neu_infinity", "axis_atlas", "hdfc_regalia_gold"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Mezzanine Floor, Post-Security, Near Gate 15", "amenities": ["Hot Dishes", "Sweets & Desserts", "WiFi", "Flight Displays"]},
        {"id": "ccu_intl_travel_club", "name": "Travel Club Lounge (International)", "city": "Kolkata", "airportOrStation": "Netaji Subhash Chandra Bose International Airport (CCU)", "terminal": "International Departures", "type": "AIRPORT_INTERNATIONAL", "criteria": "PRIORITY_PASS", "spendRequirementNote": "Priority Pass and DreamFolks card required.", "eligibleCardIds": ["axis_atlas", "bob_varunah", "amex_platinum_travel"], "eligibleNetworks": ["Visa", "Mastercard", "Amex"], "timings": "24 Hours", "locationDetail": "Level 2, International Departures, Post-Security", "amenities": ["Hot Buffet", "Bar Area", "WiFi", "Newspapers"]},

        # Ahmedabad
        {"id": "amd_t1_the_lounge", "name": "The Lounge (Domestic T1)", "city": "Ahmedabad", "airportOrStation": "Sardar Vallabhbhai Patel International Airport (AMD)", "terminal": "Terminal 1", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on BoB Eterna, Scapia Federal, Tata Neu Infinity & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "tata_neu_infinity", "axis_atlas", "au_zenith_plus"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Post-Security, Near Gate 3", "amenities": ["Gujarati Delicacies", "Buffet", "WiFi", "Charging Points"]},
        {"id": "amd_t2_the_lounge", "name": "The Lounge (International T2)", "city": "Ahmedabad", "airportOrStation": "Sardar Vallabhbhai Patel International Airport (AMD)", "terminal": "Terminal 2", "type": "AIRPORT_INTERNATIONAL", "criteria": "PRIORITY_PASS", "spendRequirementNote": "Priority Pass and DreamFolks international access.", "eligibleCardIds": ["axis_atlas", "bob_varunah", "amex_platinum_travel"], "eligibleNetworks": ["Visa", "Mastercard", "Amex"], "timings": "24 Hours", "locationDetail": "Level 1, International Departures, Post-Security", "amenities": ["Hot Buffet", "Beverages", "WiFi", "Flight Displays"]},

        # Pune
        {"id": "pnq_dom_bird", "name": "Bird Lounge (Domestic)", "city": "Pune", "airportOrStation": "Pune International Airport (PNQ)", "terminal": "New Terminal", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Direct ₹2 swipe on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "First Floor, Post-Security, Near Gate 5", "amenities": ["Buffet", "Coffee Station", "WiFi", "Comfortable Seating"]},

        # Goa
        {"id": "gox_mopa_encalm", "name": "Encalm Lounge (Goa MOPA)", "city": "Goa", "airportOrStation": "Manohar International Airport (GOX)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary ₹2 swipe for Scapia Federal, BoB Eterna, Tata Neu Infinity & Axis Atlas.", "eligibleCardIds": ["scapia_federal", "bob_eterna", "tata_neu_infinity", "axis_atlas", "au_zenith_plus"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Mezzanine Level, Near Food Court, Post-Security", "amenities": ["Goan Delicacies", "Bar Counter", "Flight Displays", "High Speed WiFi"]},
        {"id": "goi_dabolim_good_times", "name": "Good Times Bar & Lounge", "city": "Goa", "airportOrStation": "Dabolim International Airport (GOI)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "SPEND_LINKED", "spendRequirementNote": "Card spend threshold required on select bank cards.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "hdfc_regalia_gold"], "eligibleNetworks": ["Visa", "Mastercard"], "timings": "05:00 - 23:00", "locationDetail": "First Floor, Security Hold Area", "amenities": ["Snacks", "Bar Services", "WiFi", "Flight Monitors"]},

        # Kochi
        {"id": "cok_dom_earth", "name": "Earth Lounge (Domestic)", "city": "Kochi", "airportOrStation": "Cochin International Airport (COK)", "terminal": "Terminal 1", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on BoB Eterna, Scapia Federal & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "First Floor, Domestic Departures, Post-Security", "amenities": ["Kerala Meals & Snacks", "Coffee", "WiFi", "Business Desks"]},
        {"id": "cok_intl_earth", "name": "Earth Lounge (International)", "city": "Kochi", "airportOrStation": "Cochin International Airport (COK)", "terminal": "Terminal 3", "type": "AIRPORT_INTERNATIONAL", "criteria": "PRIORITY_PASS", "spendRequirementNote": "Priority Pass and DreamFolks eligible.", "eligibleCardIds": ["axis_atlas", "bob_varunah", "amex_platinum_travel"], "eligibleNetworks": ["Visa", "Mastercard", "Amex"], "timings": "24 Hours", "locationDetail": "International Departures, Post-Security", "amenities": ["Buffet", "Bar Area", "Shower Suites", "WiFi"]},

        # Jaipur
        {"id": "jai_dom_primus", "name": "Primus Lounge (Domestic)", "city": "Jaipur", "airportOrStation": "Jaipur International Airport (JAI)", "terminal": "Terminal 2", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Direct ₹2 swipe on BoB Eterna, Scapia, Tata Neu Infinity & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "tata_neu_infinity", "axis_atlas", "au_zenith_plus"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "First Floor, Post-Security, Near Gate 2", "amenities": ["Rajasthani Snacks", "Buffet", "WiFi", "Charging Stations"]},

        # Lucknow
        {"id": "lko_dom_the_lounge", "name": "The Lounge (Domestic)", "city": "Lucknow", "airportOrStation": "Chaudhary Charan Singh International Airport (LKO)", "terminal": "Terminal 3", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Direct ₹2 swipe on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "First Floor, Domestic Departures", "amenities": ["Awadhi Cuisine", "Beverages", "WiFi", "Flight Displays"]},

        # Chandigarh
        {"id": "ixc_dom_cram_bar", "name": "Cram Bar & Lounge (Domestic)", "city": "Chandigarh", "airportOrStation": "Shaheed Bhagat Singh International Airport (IXC)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary swipe on BoB Eterna, Scapia, Axis Atlas & Tata Neu.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Level 1, Post-Security, Near Gate 4", "amenities": ["Buffet", "Beverages", "WiFi", "Flight Info"]},

        # Amritsar
        {"id": "atq_dom_costa", "name": "Costa Coffee Executive Lounge", "city": "Amritsar", "airportOrStation": "Sri Guru Ram Dass Jee International Airport (ATQ)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "SPEND_LINKED", "spendRequirementNote": "Card spend threshold required on select bank cards.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas"], "eligibleNetworks": ["Visa", "Mastercard"], "timings": "06:00 - 22:00", "locationDetail": "First Floor, Security Hold Area", "amenities": ["Warm Snacks", "Coffee", "WiFi", "Newspapers"]},

        # Bhubaneswar
        {"id": "bbi_dom_mayfair", "name": "Mayfair Lounge (Domestic)", "city": "Bhubaneswar", "airportOrStation": "Biju Patnaik International Airport (BBI)", "terminal": "Terminal 1", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Direct ₹2 swipe on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "First Floor, Post-Security, Near Gate 3", "amenities": ["Hot Buffet", "WiFi", "Beverages", "Flight Monitors"]},

        # Guwahati
        {"id": "gau_dom_the_lounge", "name": "The Lounge (Domestic)", "city": "Guwahati", "airportOrStation": "Lokpriya Gopinath Bordoloi International Airport (GAU)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "First Floor, Security Hold Area", "amenities": ["Assam Tea & Snacks", "Buffet", "WiFi", "Charging Points"]},

        # Indore
        {"id": "idr_dom_primus", "name": "Primus Lounge (Domestic)", "city": "Indore", "airportOrStation": "Devi Ahilya Bai Holkar Airport (IDR)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "First Floor, Security Hold Area", "amenities": ["Indori Poha & Snacks", "Buffet", "WiFi", "Flight Displays"]},

        # Nagpur
        {"id": "nag_dom_travel_club", "name": "Travel Club Lounge (Domestic)", "city": "Nagpur", "airportOrStation": "Dr. Babasaheb Ambedkar International Airport (NAG)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Security Hold Area, Ground Floor", "amenities": ["Buffet", "Tea & Coffee", "WiFi", "Flight Displays"]},

        # Coimbatore
        {"id": "cjb_dom_tfs", "name": "TFS Lounge (Domestic)", "city": "Coimbatore", "airportOrStation": "Coimbatore International Airport (CJB)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "05:00 - 23:00", "locationDetail": "First Floor, Post-Security", "amenities": ["South Indian Meals", "WiFi", "Beverages", "Flight Monitors"]},

        # Thiruvananthapuram
        {"id": "trv_dom_executive", "name": "Executive Lounge (Domestic)", "city": "Thiruvananthapuram", "airportOrStation": "Trivandrum International Airport (TRV)", "terminal": "Terminal 1", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary swipe on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "Domestic Departures, Post-Security", "amenities": ["Buffet", "Coffee", "WiFi", "Air Conditioning"]},

        # Visakhapatnam
        {"id": "vtz_dom_tfs", "name": "TFS Lounge (Domestic)", "city": "Visakhapatnam", "airportOrStation": "Visakhapatnam International Airport (VTZ)", "terminal": "Domestic Departures", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on BoB Eterna, Scapia & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "axis_atlas", "tata_neu_infinity"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "24 Hours", "locationDetail": "First Floor, Near Gate 2", "amenities": ["Buffet", "WiFi", "Beverages", "Flight Displays"]},

        # Varanasi Airport
        {"id": "vns_dom_take_off", "name": "Take Off Bar & Lounge (Domestic)", "city": "Varanasi", "airportOrStation": "Lal Bahadur Shastri International Airport (VNS)", "terminal": "Domestic Terminal", "type": "AIRPORT_DOMESTIC", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on BoB Eterna, Scapia, Tata Neu Infinity & Axis Atlas.", "eligibleCardIds": ["bob_eterna", "scapia_federal", "tata_neu_infinity", "axis_atlas"], "eligibleNetworks": ["Visa", "Mastercard", "RuPay"], "timings": "06:00 - 22:00", "locationDetail": "First Floor, Security Hold Area", "amenities": ["Snacks Buffet", "Beverages", "WiFi", "Flight Displays"]},

        # --- INDIAN RAILWAY EXECUTIVE LOUNGES ---
        {"id": "rail_ndls_irctc", "name": "IRCTC Executive Railway Lounge (Platform 16)", "city": "New Delhi", "airportOrStation": "New Delhi Railway Station (NDLS)", "terminal": "Platform 16 (Ajmeri Gate Side)", "type": "RAILWAY", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Free 2-hour stay with buffet on Ixigo AU, IRCTC BoB RuPay, IRCTC SBI Premier & select RuPay cards.", "eligibleCardIds": ["ixigo_au", "bob_irctc", "sbi_irctc_premier", "tata_neu_infinity", "au_zenith_plus"], "eligibleNetworks": ["RuPay", "Visa", "Mastercard"], "timings": "24 Hours", "locationDetail": "Platform 16, First Floor, Ajmeri Gate Entrance", "amenities": ["Recliner Chairs", "Buffet Meals", "High Speed WiFi", "Bath & Wash", "Shoe Shiner", "Luggage Lockers"]},
        {"id": "rail_ndls_plat1", "name": "IRCTC Executive Railway Lounge (Platform 1)", "city": "New Delhi", "airportOrStation": "New Delhi Railway Station (NDLS)", "terminal": "Platform 1 (Paharganj Side)", "type": "RAILWAY", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Free access on Ixigo AU, IRCTC BoB RuPay & IRCTC SBI Premier.", "eligibleCardIds": ["ixigo_au", "bob_irctc", "sbi_irctc_premier", "tata_neu_infinity"], "eligibleNetworks": ["RuPay", "Visa"], "timings": "24 Hours", "locationDetail": "Platform 1, Near VIP Lounge, Paharganj Entrance", "amenities": ["Air-conditioned Lounge", "Buffet", "WiFi", "Showers"]},
        {"id": "rail_bsb_irctc", "name": "IRCTC Executive Railway Lounge (Varanasi Cantt)", "city": "Varanasi", "airportOrStation": "Varanasi Junction (BSB)", "terminal": "Platform 1", "type": "RAILWAY", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary swipe on Ixigo AU, IRCTC BoB RuPay, IRCTC SBI Premier & RuPay Select.", "eligibleCardIds": ["ixigo_au", "bob_irctc", "sbi_irctc_premier", "tata_neu_infinity", "bob_varunah"], "eligibleNetworks": ["RuPay", "Visa"], "timings": "24 Hours", "locationDetail": "Platform 1, Near Main Concourse", "amenities": ["Luxury Recliners", "Hot Meals", "WiFi", "Bath Facility"]},
        {"id": "rail_sdah_irctc", "name": "IRCTC Executive Railway Lounge (Sealdah)", "city": "Kolkata", "airportOrStation": "Sealdah Railway Station (SDAH)", "terminal": "Main Concourse", "type": "RAILWAY", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Free stay on Ixigo AU, IRCTC BoB RuPay & IRCTC SBI Premier.", "eligibleCardIds": ["ixigo_au", "bob_irctc", "sbi_irctc_premier", "tata_neu_infinity"], "eligibleNetworks": ["RuPay", "Visa"], "timings": "24 Hours", "locationDetail": "Near Platform 1, Main Concourse Hall", "amenities": ["Recliners", "Snack Buffet", "WiFi", "Clean Restrooms"]},
        {"id": "rail_agc_irctc", "name": "IRCTC Executive Railway Lounge (Agra Cantt)", "city": "Agra", "airportOrStation": "Agra Cantt Railway Station (AGC)", "terminal": "Platform 1", "type": "RAILWAY", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary entry with Ixigo AU & IRCTC credit cards.", "eligibleCardIds": ["ixigo_au", "bob_irctc", "sbi_irctc_premier"], "eligibleNetworks": ["RuPay", "Visa"], "timings": "24 Hours", "locationDetail": "Platform 1, Near Enquiry Counter", "amenities": ["Air-conditioned Lounge", "WiFi", "Snacks Buffet", "Lockers"]},
        {"id": "rail_adi_irctc", "name": "IRCTC Executive Railway Lounge (Ahmedabad)", "city": "Ahmedabad", "airportOrStation": "Ahmedabad Junction (ADI)", "terminal": "Platform 1", "type": "RAILWAY", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on Ixigo AU, IRCTC BoB RuPay & IRCTC SBI Premier.", "eligibleCardIds": ["ixigo_au", "bob_irctc", "sbi_irctc_premier", "tata_neu_infinity"], "eligibleNetworks": ["RuPay", "Visa"], "timings": "24 Hours", "locationDetail": "Platform 1, Kalupur Side Entrance", "amenities": ["Vegetarian Buffet", "WiFi", "Air Conditioning", "Restrooms"]},
        {"id": "rail_jp_irctc", "name": "IRCTC Executive Railway Lounge (Jaipur)", "city": "Jaipur", "airportOrStation": "Jaipur Junction (JP)", "terminal": "Platform 1", "type": "RAILWAY", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary entry on Ixigo AU, IRCTC BoB RuPay & IRCTC SBI Premier.", "eligibleCardIds": ["ixigo_au", "bob_irctc", "sbi_irctc_premier"], "eligibleNetworks": ["RuPay", "Visa"], "timings": "24 Hours", "locationDetail": "Platform 1, Near Station Master Office", "amenities": ["Rajasthani Hospitality", "WiFi", "Buffet", "Washrooms"]},
        {"id": "rail_mdu_irctc", "name": "IRCTC Executive Railway Lounge (Madurai)", "city": "Madurai", "airportOrStation": "Madurai Junction (MDU)", "terminal": "Platform 1", "type": "RAILWAY", "criteria": "UNCONDITIONAL", "spendRequirementNote": "Complimentary on Ixigo AU & IRCTC RuPay cards.", "eligibleCardIds": ["ixigo_au", "bob_irctc", "sbi_irctc_premier"], "eligibleNetworks": ["RuPay", "Visa"], "timings": "24 Hours", "locationDetail": "Platform 1, Near Main Gate", "amenities": ["Air-conditioned Lounge", "Hot Coffee", "WiFi", "Restrooms"]}
    ]

    lounges_container = {
        "version": "2.0.0",
        "lastUpdated": "2026-11-08T16:20:00Z",
        "totalLounges": len(lounges_list),
        "lounges": lounges_list
    }

    # Write cards to both destinations
    for dest_dir in [app_assets_dir, root_assets_dir]:
        cards_path = os.path.join(dest_dir, "cards_catalog.json")
        with open(cards_path, "w", encoding="utf-8") as f:
            json.dump(cards_container, f, indent=2, ensure_ascii=False)
        print(f"✅ Successfully wrote {len(raw_cards_data)} cards to {cards_path}")

        lounges_path = os.path.join(dest_dir, "lounges_catalog.json")
        with open(lounges_path, "w", encoding="utf-8") as f:
            json.dump(lounges_container, f, indent=2, ensure_ascii=False)
        print(f"✅ Successfully wrote {len(lounges_list)} lounges to {lounges_path}")

if __name__ == "__main__":
    build_catalogs()
