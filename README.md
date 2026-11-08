# 💳 PerkLens — Your AI Lens on Every Card, Lounge & Swipe

<p align="center">
  <img src="assets/perklens_logo.svg" width="180" height="180" alt="PerkLens Brand Logo"/>
</p>

<p align="center">
  <a href="https://github.com/sakinator/PerkLens/releases/latest"><img src="https://img.shields.io/badge/Download-PerkLens%20v1.0.0%20APK-00E676?style=for-the-badge&logo=android&logoColor=black" alt="Download APK"/></a>
  <a href="#-vibe-coded-philosophy--manifesto"><img src="https://img.shields.io/badge/Vibe%20Coded-100%25%20with%20AI-ff0c82?style=for-the-badge&logo=visualstudiocode&logoColor=white" alt="Vibe Coded"/></a>
</p>

<p align="center">
  <a href="https://github.com/sakinator/PerkLens/actions/workflows/build-apk.yml"><img src="https://img.shields.io/github/actions/workflow/status/sakinator/PerkLens/build-apk.yml?style=flat-square&logo=github&label=APK%20Build" alt="Build Status"/></a>
  <img src="https://img.shields.io/badge/Regression%20Suite-36%2F36%20Passed-00FF87?style=flat-square" alt="Regression Suite"/>
  <img src="https://img.shields.io/badge/Theme-Dark%20%26%20Neon%20Green-00E676?style=flat-square" alt="Theme"/>
  <img src="https://img.shields.io/badge/Security-Zero%20KYC%20%7C%20Local%20Keys%20Only-00FF87?style=flat-square" alt="Security"/>
  <img src="https://img.shields.io/badge/Platform-Android%20%28Kotlin%20%2B%20Compose%29-10B981?style=flat-square" alt="Android"/>
  <img src="https://img.shields.io/badge/License-MIT-blue?style=flat-square" alt="License"/>
</p>

> [!NOTE]
> **✨ 100% Vibe Coded with AI:** PerkLens is vibe-coded through continuous human-AI agentic collaboration, real-time feedback loops, automated regression test suites, and live self-healing pipelines. High velocity, zero bloat, pure vibes.

A modern, privacy-first Native Android application built with **Kotlin**, **Jetpack Compose**, and **Material 3**. 
Where **ThreatLens** protects your Android device from mobile threats, **PerkLens** protects your wallet from lost airport lounge visits, expired cinema BOGOs, missed cashbacks, and hidden bank fee traps.

---

## 🎨 Brand Identity & Visual Design

The **PerkLens** brand features a sleek **Dark & Cyber Neon Green** modern design:
1. **The Emerald Perspective Smart Card**: Deep obsidian-to-neon green body with glowing electric cyber borders (`#00FF87`), specular sheen, metallic EMV brass chip, and contactless radio waves.
2. **The 10-Lobe Scalloped Offer Rosette**: Smooth undulating curved badge in vibrant chartreuse-gold, matching classic coupon and retail reward seals.
3. **The Rounded `%` Geometry**: High-contrast geometric percentage emblem with a rounded pill diagonal slash and dual hollow rings.
4. **Obsidian Base**: Deep OLED black squircle frame matching the user's mobile security portfolio styling.

* **Vector Asset:** [`assets/perklens_logo.svg`](assets/perklens_logo.svg) | Android VectorDrawable: [`app/src/main/res/drawable/ic_perklens_logo.xml`](app/src/main/res/drawable/ic_perklens_logo.xml)

---

## 🔒 Security & Privacy First (Zero-KYC)

- **API Keys Stored Strictly Locally:** Any custom AI provider API keys (Google Gemini, OpenAI, Anthropic Claude, Groq) are entered directly on your device and persisted solely in private on-device storage. **No API keys or credentials are ever committed, logged, or sent to any remote telemetry server.**
- **Zero-KYC Architecture:** Never asks for 16-digit card numbers, CVVs, expiry dates, or banking passwords. All cards in your vault are tracked purely by product name (e.g., *"HDFC Millennia"*, *"SBI Cashback"*).
- **100% On-Device Parsing:** All SMS and camera OCR scanning executes locally on your hardware with Google ML Kit.

---

## 🌟 Core Modules & Architecture

### 1. 📸 AI Multi-Card Camera Vision Scanner
- **Spread & Scan Multiple Cards at Once:** Capture a single photo of multiple cards spread on a table (or select from gallery).
- **On-Device Segregation:** Google ML Kit OCR and vision algorithms isolate distinct card entities, match card product names against the catalog, and ignore sensitive 16-digit PAN numbers/CVVs.
- **Batch Confirmation Review:** Checkbox list allowing you to review all recognized cards and add them to your vault in 1 tap.

### 2. 🎬 Dedicated Privilege Offers by Category
- Select a category chip (**Movies**, **Dining**, **Online Shopping**, **Quick Groceries**, **Utilities**, **Travel**, **Fuel**).
- Displays all cards in your vault with:
  - **Platform Badges & Logos:** BookMyShow, PVR INOX, Swiggy, Zomato, Amazon, BigBasket, MakeMyTrip.
  - **Offer Details:** Buy 1 Get 1 Free (BOGO), 10% direct cashback, 5% SmartBuy CashPoints.
  - **Quota Limits & Terms:** e.g., *"Twice per calendar month up to ₹250/ticket"*.
  - **Redemption Guide:** Clear instructions on where to apply the offer.

### 3. ✈️ Dedicated Airport & Railway Lounge Pass Hub
- Covers Delhi (DEL T1/T2/T3), Mumbai (BOM T1/T2), Bengaluru (BLR T1/T2 080), Hyderabad (HYD), Chennai (MAA), Goa (GOX), and Executive Railway Lounges (New Delhi, Varanasi).
- **"You Can Enter ✓" Live Badge:** Automatically cross-references lounges against cards in your wallet.
- **Criteria Decoder:**
  - 🟢 **Unconditional (₹2 Swipe):** Scapia Federal, Axis Atlas, Tata Neu Infinity.
  - 🟡 **Spend-Linked:** Clarifies exact spend thresholds (e.g., *HDFC/ICICI requiring ₹10k–₹50k spend in previous calendar quarter*).
  - 🟣 **Priority Pass / DreamFolks:** International lounges & guest allowances.

### 4. 🤖 Intelligent Multi-Horizon Exhaustion Engine (SMS & Gmail)
- **Multi-Horizon Tracking:** Automatically analyzes parsed SMS & Gmail notifications to track what has already been consumed:
  - **Monthly:** BookMyShow BOGO tickets used, Swiggy/Zomato cashback caps exhausted (e.g. ₹500 cap), utility bill cashback caps.
  - **Quarterly:** Airport lounge visits consumed for Q1/Q2/Q3/Q4.
  - **6-Months / Semi-Annual:** Times Prime renewals and half-yearly voucher windows.
  - **Yearly / Annual Milestones:** Progress toward annual fee waivers (₹1L, ₹2L, ₹3L) and milestone bonuses (e.g. Amex Platinum Travel ₹4 Lakhs spend = ₹10k Taj voucher).
- **🔒 100% On-Device Privacy:** Runs completely locally via regex parsers. No SMS, OTP, or email content is ever uploaded to external servers.

### 5. 🗓️ Billing Cycle Setting & 50-Day Interest-Free Runway
- Set each card's statement generation day (Day 1–31) using the interactive slider.
- **Live Swipe Runway:** Calculates the exact number of interest-free days available if swiped today (up to ~50 days grace).
- Automatically calculates countdowns to monthly cap and cinema quota resets.

### 6. 🤖 Multi-Engine AI Integration (ThreatLens Style)
- **AI Models Supported:**
  - **Google Gemini (Free Public Tier):** Works immediately with zero API keys required.
  - **Custom API Keys:** Google Gemini 1.5/2.0 Flash, OpenAI (GPT-4o Mini), Anthropic Claude (3.5 Sonnet), and Groq (Llama 3).
  - **Built-in Offline Smart Analyst:** 100% offline rule-based heuristic evaluator.
- **Portfolio Gap Advisor:** Analyzes what categories your wallet is missing (RuPay UPI, Lounge, Dining, Forex) with a toggle for **"Suggest Lifetime Free (LTF) Cards Only"**.

### 7. 🔄 Dynamic Remote Sync (Self-Sufficient Updates)
- Automatically checks a remote CDN/endpoint on launch to pull new bank offers, card devaluations, new card artwork, and brand logos live without requiring an APK release or repo update.

---

## 📁 Project Directory Layout

```
PerkLens/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/
│       │   ├── cards_catalog.json
│       │   ├── lounges_catalog.json
│       │   └── perklens_logo.svg
│       └── java/io/perklens/
│           ├── MainActivity.kt
│           ├── core/ai/              # AI Verdicts, Gap Analysis & Model Providers
│           ├── core/parser/          # SMS Bank Parser, Multi-Horizon Exhaustion Engine
│           ├── core/scanner/         # Camera Multi-Card Vision Scanner (ML Kit)
│           ├── core/sync/            # Gmail E-Statement Sync
│           ├── data/model/           # Cards, Lounges, Offers, Usages
│           ├── data/repository/      # Cards, Lounges, Category Offers & Wallet Usages
│           ├── data/sync/            # Dynamic OTA JSON Catalog Sync
│           ├── engine/               # Smart Swipe Optimizer
│           └── ui/
│               ├── MainNavigation.kt
│               ├── components/       # CreditCardVisual, CameraCardScanSheet, CardDetailBottomSheet
│               ├── screens/          # Wallet, Offers, Lounges, Decide, Discover, Advisor, Settings
│               └── theme/            # Color, Theme, Type (Dark & Neon Green)
├── assets/
│   └── perklens_logo.svg
├── keystore/
│   └── release.jks                   # 30-year permanent release signing keystore
├── tools/
│   ├── codebase_auditor.py           # ThreatLens-style static syntax & symbol auditor
│   └── run_regression_suite.py       # 36 automated regression verification tests
└── .github/workflows/
    └── build-apk.yml                 # Automated Android APK CI/CD pipeline
```

---

## 📦 Download Latest APK Release & Long-Term Keystore

Download the official signed APK directly from GitHub:
👉 **[PerkLens Latest GitHub Release](https://github.com/sakinator/PerkLens/releases/latest)**

* **Guaranteed In-Place Upgrades:** Signed with a permanent 30-year release keystore (`keystore/release.jks`, valid until **2056**) matching both debug and release configurations. This ensures smooth direct updates over older versions without data loss or Android signature mismatch errors (`INSTALL_FAILED_UPDATE_INCOMPATIBLE`).
* **Zero Signature Conflicts:** Debug and release builds share the identical signing identity so developers, beta testers, and end users can switch between builds seamlessly.

---

## ✨ Vibe Coded Philosophy & Manifesto

> *"Code at the speed of thought. Guided by vibes, verified by automated test suites."*

This entire project — spanning the on-device ML Kit multi-card vision scanner, real-time Indian bank SMS parsing heuristics, multi-horizon exhaustion engine, dynamic OTA catalog synchronization, and the reactive Jetpack Compose cyber-neon dashboard — is **100% Vibe Coded**.

### What does "Vibe Coded" mean here?
- **AI-Native Architecture**: Conceived, architected, debugged, and refined through symbiotic interaction between human vision and autonomous AI coding agents (DeepMind Antigravity / Gemini 2.0 / Claude).
- **Continuous Agentic Pair Programming**: Built not through bureaucratic sprint tickets or boilerplate corporate design documents, but through organic, intent-driven iterations where design, logic, and polish happen simultaneously in tight agentic feedback loops.
- **Rapid Self-Healing Pipelines**: Backed by a ThreatLens-grade static auditor (`tools/codebase_auditor.py`) and a comprehensive 36-test regression suite (`tools/run_regression_suite.py`) that verify bracket closures, schema structures, OCR sanitization, and quota math on every push.
- **Vibe Velocity**: Shipped iteratively at extreme velocity without technical debt — evolving rapidly as new credit card perks, airport lounge devaluation rules, and bank SMS notification templates change.
- **Zero-KYC Respect**: Vibecoding prioritizes user privacy above all: zero KYC data collection, zero telemetry bloat, zero tracking SDKs, and local-only API key persistence. Pure utility, pure vibes.
- **As-Is Provision**: As with all vibe-coded software, it is offered purely as open-source research and experimental tooling for the credit card enthusiast community. Enjoy the vibes responsibly!

---

## 🛠️ Building & Contributing

### Prerequisites
- Android Studio Ladybug / Iguana or later
- JDK 17
- Android SDK 34 (Android 14)

### Clone & Build
```bash
git clone https://github.com/sakinator/PerkLens.git
cd PerkLens
./gradlew assembleDebug
```

---

## 📄 License
Released under the MIT License. Built with privacy and zero-KYC principles for credit card enthusiasts in India.
