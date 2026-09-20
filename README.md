# TOBI GT — Master Android Studio Project
Developed by **Tobi** (`sakibulhasanmd69@gmail.com`)

## Overview
**TOBI GT** is a vertical arcade sci-fi fighter combat game built with **Kotlin** and **Jetpack Compose** (Material 3). It features a 60 FPS Canvas-rendered flight engine, anonymous pilot identity system, local Room database persistence, authoritative transaction audit logging, and Google Play Console integration for Play Points coupons.

---

## Key Features & Systems

### 1. Retro-Futuristic Combat Engine (`com.example.game`)
- **60 FPS Hardware-Accelerated Canvas Gameplay** with delta-timed frame pacing.
- **Dynamic Wave Management**: Enemy scouts, agile interceptors, heavy bombers, meteor hazards, and the dreadnought **Titan GT-01 Boss**.
- **Player Jet System**: Fluid touch/drag control, dual laser cannons, spread plasma shot, homing rockets, energy shields, and emergency EMP bomb wave-clearing.
- **Audio Synthesizer Engine** (`GameAudio.kt`): Zero-dependency procedural sound effects using Android `AudioTrack` PCM tone generation.

### 2. Diamond Economy & Store (`com.example.data.model.CoinPackages`)
- Complete **22-Tier Diamond Catalog** featuring exact micro and fleet options ($0.51/51, $1.01/101, $2.01/201, $3.01/301 ... $1,000.01/100,001 plus $1.00/100 and $2.00/200 Diamonds).
- **Google Play Billing Compatibility**: Strict separation of real-money transactions from coupon redemption rules.
- **Authoritative Room Persistence**: Every purchase generates an immutable cryptographic transaction receipt with zero simulated fake accounts.
- **Hardware-Optimized Rendering**: Zero-allocation Skia canvas paths and throttled native audio flinger playback for 60/120 FPS buttery smooth performance.

### 3. Google Play Points Rewards Integration
- Full developer architecture for configuring **12 Official Coupon Promotion Tiers** in Google Play Console ($1 to $40 OFF).
- In-game Play Points documentation screen with console setup navigation steps, minimum purchase thresholds, and country targeting guidance.

### 4. Anonymous Pilot Architecture
- Zero mandatory registration, zero email/password forms, and zero friction.
- Randomly generated anonymous Pilot UUIDs generated locally on first boot.
- Authoritative audit ledger for customer support, balance adjustments, and verified refunds.

### 5. Premium Dark Theme UI (`com.example.ui.theme`)
- **Deep Space Palette**: Navy-black canvas (`#090D16`), deep slate cards (`#111827`), glowing cyan crystal accents (`#06B6D4`), and 3D faceted crystal diamond rendering (`TobiDiamond3D.kt`).

---

## Opening and Building in Android Studio

1. **Prerequisites**:
   - Android Studio Jellyfish / Koala / Ladybug or newer
   - Android SDK 35 (Android 15)
   - JDK 17 or JDK 21

2. **Importing the Project**:
   - Launch Android Studio.
   - Click **File** > **Open...** and select this directory (`TOBI GT` root).
   - Allow Gradle to sync dependencies automatically via `gradle/libs.versions.toml`.

3. **Running the App**:
   - Select the `app` run configuration.
   - Choose a connected physical Android device or an Android Virtual Device (AVD) running Android 8.0 (API 26) or higher.
   - Click **Run** (Shift+F10) or **Debug** (Shift+F9).

4. **Generating Signed APK / Bundle**:
   - Go to **Build** > **Generate Signed Bundle / APK**.
   - Follow standard Android signing procedures for release to Google Play.
