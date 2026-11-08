# 💳 PerkLens v1.0.0 — Your AI Lens on Every Card, Lounge & Swipe

### 🌟 Initial Production Release Highlights

PerkLens is a privacy-first, zero-KYC Native Android application built with Kotlin, Jetpack Compose, and Material 3, designed as a companion to **ThreatLens** to maximize card privileges, lounge access, and interest-free runway.

#### 1. 🎨 Cyber Neon Green & Obsidian Brand Identity
- Custom vector logo featuring a 10-lobe soft wave scalloped rosette badge and rounded pill `%` geometry.
- Premium OLED Dark (`#070A0F`) and Cyber Neon Green (`#00E676` / `#00FF87`) visual design system.

#### 2. 📸 AI Multi-Card Vision Scanner (ML Kit)
- Spread multiple credit/debit cards on a desk and capture them in a single frame.
- Automatically isolates card entities, matching catalog names while **strictly stripping sensitive 16-digit PANs and CVVs**.

#### 3. ✈️ Airport & Railway Lounge Pass Hub
- Comprehensive directory spanning Delhi (T1/T2/T3), Mumbai (T1/T2), Bengaluru (080 T1/T2), Hyderabad, Chennai, Goa, and Indian Railway Executive lounges.
- Real-time "You Can Enter ✓" qualification badge.
- Access criteria classification: Unconditional (₹2 swipe) vs Spend-Linked quarterly thresholds.

#### 4. 🤖 Intelligent Multi-Horizon Exhaustion Engine
- Parses local bank transaction SMS & statement notifications to track what has already been consumed across multiple horizons:
  - **Monthly:** BookMyShow / PVR INOX cinema BOGO quotas and Swiggy / Zomato cashback caps.
  - **Quarterly:** Airport lounge complimentary visit allowances.
  - **Annual:** Milestone spend trackers and annual fee waiver progress (₹1L / ₹2L / ₹3L).
- Multi-card fallback logic that dynamically re-routes transactions when caps are exhausted.

#### 5. 🗓️ Statement Day & 50-Day Interest-Free Runway Optimizer
- Configurable statement generation date (Day 1–31) per card.
- Live swipe optimizer calculating maximum interest-free runway (up to 50 days).

#### 6. 🤖 Multi-Engine AI Integration Hub & Live Latency Tester
- **Dedicated "Settings & AI" Tab:** Promoted to the main bottom navigation bar alongside quick wallet status pills for instant access.
- **AI Models Supported:**
  - **Google Gemini (Free Public Tier):** Works immediately with zero API keys required.
  - **Custom API Keys:** Google Gemini 1.5/2.0 Flash, OpenAI (GPT-4o Mini), Anthropic Claude (3.5 Sonnet), and Groq (Ultra-fast sub-second Llama 3 70B/8B).
  - **Built-in Offline Smart Analyst:** 100% offline rule-based heuristic evaluator (0ms latency, zero network transmission).
- **Live Connection Tester:** Instant one-tap "Test AI Connection & Check Latency" button that verifies your credentials and reports server response times.
- **Strictly Local-Only Key Storage:** API keys persist in `Context.MODE_PRIVATE` on-device storage. Zero telemetry, zero uploads, zero leakage.
- **Portfolio Gap Advisor:** Analyzes what categories your wallet is missing (RuPay UPI, Lounge, Dining, Forex) with a toggle for **"Suggest Lifetime Free (LTF) Cards Only"**.

#### 7. 💳 44+ Comprehensive Credit & Debit Card Catalog & Reliable Update Sources
- **Bank of Baroda Flagship Portfolio:** Featuring **BoB Eterna** (unlimited domestic lounge access, 3.75% travel/dining reward rate, FitPass Pro, 1+1 BMS cinema), **BoB Premier**, **BoB Varunah (RuPay)**, and **BoB Easy**.
- **All Major Indian Banks & Fintechs:** HDFC (Infinia Metal, Diners Black, Regalia Gold, Swiggy, EasyShop Platinum Debit), SBI (Aurum, Elite, SimplyCLICK, Octane, Global Debit), ICICI (Emeralde Private, Sapphiro, Coral), Axis (Magnus, Atlas, Flipkart, Ace, Burgundy Debit), IDFC FIRST (Wealth, WOW 0% Forex), IndusInd, Kotak, Amex (Platinum, MRCC, Platinum Travel), AU Small Finance (Zenith+ 0.99% forex, Ixigo 0% forex), RBL (World Safari 0% forex), and Fintechs (Fi Money, Jupiter Edge RuPay).
- **Photorealistic Card Visualizer:** Authentic EMV gold microchips with contact traces, contactless NFC waves, crisp card network emblems (RuPay with tricolor accents, Visa, Mastercard dual overlapping circles, Amex blue box, Diners Club), and dynamic glassmorphism badge overlays.
- **Reliable Update Sources:**
  1. **Official Bank SOC & MITC:** Directly sourced from quarterly bank tariff sheets mandated under RBI Master Directions.
  2. **Community Reward Matrices:** TechnoFino & CardExpert curated open databases for category accelerators and exclusions.
  3. **PerksLens OTA Dynamic Sync:** Checks the remote repository on launch to pull new bank offers and devaluations live without requiring APK re-installs.

#### 8. 🛡️ ThreatLens-Grade Static Auditor & 36-Test Regression Suite
- Bundled with `tools/codebase_auditor.py` and `tools/run_regression_suite.py`.
- **36/36 Regression Tests Passing** across 11 test suites.

#### 9. 🔑 Guaranteed In-Place Upgrades (30-Year Keystore)
- Signed with a permanent 30-year release keystore (`keystore/release.jks`, valid through **2056**) shared across both debug and release configurations.
- Guaranteed seamless updates over older versions without data loss or Android signature mismatch errors (`INSTALL_FAILED_UPDATE_INCOMPATIBLE`).

#### 10. ✨ 100% Vibe Coded with AI
- Built through symbiotic human-AI agentic collaboration, real-time feedback loops, automated regression test suites, and live self-healing pipelines. High velocity, zero bloat, pure vibes.

---

### 📦 Download Assets
- **`PerkLens.apk`** (12.4 MB) — Official production release signed with the permanent 30-year keystore.
