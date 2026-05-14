# 🛠️ Smart Expense Tracker Build Guide

This project is a high-fidelity Android application built with Kotlin, Jetpack Compose, and Room Database.

## Prerequisites
- **Android Studio Iguana** (or newer)
- **JDK 17** (included with Android Studio)

## Steps to Generate APK
1. **Open Project**: Launch Android Studio and open this directory.
2. **Sync Gradle**: Click the elephant icon (Sync Project with Gradle Files) if it doesn't start automatically.
3. **Run on Device**: Connect your Android phone via USB and click the green **Play** button.
4. **Export APK**: 
   - Go to `Build > Build Bundle(s) / APK(s) > Build APK(s)`.
   - After the build finishes, click the `locate` link in the popup to find your `.apk` file.

## Project Structure
- `app/src/main/java/.../data`: Room Database & Models
- `app/src/main/java/.../ui`: Jetpack Compose Screens & Theme
- `app/src/main/java/.../viewmodel`: App Logic
- `tester.html`: A web-based interactive prototype for immediate testing.
