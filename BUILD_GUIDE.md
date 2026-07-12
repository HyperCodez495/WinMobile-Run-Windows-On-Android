# WinMobile Build & Deployment Guide

## 🔨 Building the Project

### Prerequisites

Before building, ensure you have:

1. **Android Studio** (latest version)
   - Download: https://developer.android.com/studio

2. **Android SDK**
   - SDK Platform 34 (Android 14)
   - Build Tools 34.0.0+
   - NDK (optional, for native code)

3. **Gradle**
   - Included with Android Studio
   - Or install separately: https://gradle.org/

4. **Git**
   - For cloning repository
   - Download: https://git-scm.com/

### Setup Development Environment

```bash
# Clone repository
git clone https://github.com/yourusername/WinMobile-Android.git
cd WinMobile-Android

# Sync Gradle (automatic in Android Studio)
./gradlew sync

# Verify setup
./gradlew --version
```

---

## 📦 Build Types

### Debug Build (Development)

**Use for:** Testing, debugging, development

```bash
# Build debug APK
./gradlew assembleDebug

# Output: app/build/outputs/apk/debug/app-debug.apk
```

**Characteristics:**
- Larger file size (~80MB)
- Debuggable
- No optimization
- Includes debug symbols

### Release Build (Production)

**Use for:** Distribution, publishing

```bash
# Build release APK (unsigned)
./gradlew assembleRelease

# Output: app/build/outputs/apk/release/app-release-unsigned.apk
```

**Characteristics:**
- Smaller file size (~50MB)
- Optimized with ProGuard
- Not debuggable
- Requires signing

---

## 🔐 Signing the APK

### Generate Signing Key

```bash
# Create keystore (one-time)
keytool -genkey -v -keystore winmobile.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias winmobile-key

# When prompted, enter:
# - Password: (choose secure password)
# - First/Last Name: Your Name
# - Organization: Your Company
# - City: Your City
# - State: Your State
# - Country: US (or your country code)
```

### Sign Release APK

```bash
# Sign the APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
  -keystore winmobile.keystore \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  winmobile-key

# Verify signing
jarsigner -verify -verbose -certs \
  app/build/outputs/apk/release/app-release-unsigned.apk

# Optimize with zipalign
zipalign -v 4 \
  app/build/outputs/apk/release/app-release-unsigned.apk \
  app/build/outputs/apk/release/WinMobile-signed.apk
```

### Automated Signing (Recommended)

Create `local.properties`:

```properties
# local.properties
sdk.dir=/path/to/android/sdk
storeFile=winmobile.keystore
storePassword=your_password
keyAlias=winmobile-key
keyPassword=your_password
```

Then build:

```bash
./gradlew assembleRelease
```

---

## 📱 Installing on Device

### Via ADB (Android Debug Bridge)

```bash
# Install debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Install release APK
adb install app/build/outputs/apk/release/WinMobile-signed.apk

# Uninstall
adb uninstall com.winmobile

# View logs
adb logcat | grep "WinMobile"

# Clear app data
adb shell pm clear com.winmobile
```

### Via Android Studio

1. Connect device via USB
2. Enable Developer Mode on device
3. In Android Studio: Run → Run 'app'
4. Select device
5. Click OK

### Via File Transfer

1. Build APK: `./gradlew assembleRelease`
2. Transfer APK to phone
3. Open file manager
4. Tap APK file
5. Install

---

## 🧪 Testing

### Unit Tests

```bash
# Run unit tests
./gradlew test

# Run with coverage
./gradlew testDebugUnitTestCoverage

# View coverage report
open app/build/reports/coverage/index.html
```

### Instrumented Tests (Device Tests)

```bash
# Run instrumented tests
./gradlew connectedAndroidTest

# Run specific test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.winmobile.ExampleTest
```

### Performance Testing

```bash
# Profile app
./gradlew profileDebugBuild

# View profiler results
open app/build/reports/profile/...
```

---

## 🚀 Publishing to Google Play

### Prerequisites

1. Google Play Developer Account ($25 one-time)
2. Signed APK
3. App icon (512x512 PNG)
4. Screenshots (2-8 images)
5. Description and privacy policy

### Step-by-Step

1. **Create App in Google Play Console**
   - Go to: https://play.google.com/console
   - Click "Create app"
   - Enter app name: "WinMobile"
   - Select category: "Tools"

2. **Fill App Details**
   - Title: "WinMobile - Windows on Mobile"
   - Short description: "Run Windows on your Android device"
   - Full description: (see README.md)
   - Screenshots: 2-8 images
   - Icon: 512x512 PNG

3. **Set Content Rating**
   - Fill questionnaire
   - Get content rating

4. **Set Pricing**
   - Free or paid
   - Select countries

5. **Upload APK**
   - Go to Release → Production
   - Upload signed APK
   - Review and publish

---

## 📊 Build Optimization

### Reduce APK Size

```gradle
// In app/build.gradle

android {
    // Enable minification
    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    // Split APK by architecture (optional)
    bundle {
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }
}
```

### Build Performance

```bash
# Parallel builds
./gradlew build -x test --parallel

# Daemon mode (faster rebuilds)
./gradlew build --daemon

# View build times
./gradlew build --profile

# Clean build
./gradlew clean build
```

---

## 🔍 Debugging

### Debug APK on Device

```bash
# Install debug APK
./gradlew installDebug

# Start debugger
adb shell am start -D -N com.winmobile/.MainActivity

# View logs
adb logcat -s "WinMobile"

# Filter by level
adb logcat -s "WinMobile" *:E  # Errors only
adb logcat -s "WinMobile" *:W  # Warnings and above
```

### Android Studio Debugger

1. Set breakpoints in code
2. Run → Debug 'app'
3. App pauses at breakpoints
4. Step through code
5. Inspect variables

### Performance Profiling

1. Run → Profile 'app'
2. Select profiler
3. Monitor:
   - CPU usage
   - Memory allocation
   - Network activity
   - Energy usage

---

## 📋 CI/CD Pipeline (GitHub Actions)

### Setup Automated Builds

Create `.github/workflows/build.yml`:

```yaml
name: Build WinMobile

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v2

    - name: Set up JDK 11
      uses: actions/setup-java@v2
      with:
        java-version: '11'

    - name: Build with Gradle
      run: ./gradlew build

    - name: Upload APK
      uses: actions/upload-artifact@v2
      with:
        name: WinMobile-debug.apk
        path: app/build/outputs/apk/debug/app-debug.apk

    - name: Run tests
      run: ./gradlew test
```

---

## 🔄 Version Management

### Update Version

Edit `app/build.gradle`:

```gradle
android {
    defaultConfig {
        versionCode 2        // Increment by 1
        versionName "1.1.0"  // Semantic versioning
    }
}
```

**Version naming:**
- Major.Minor.Patch (e.g., 1.0.0)
- Major: Breaking changes
- Minor: New features
- Patch: Bug fixes

### Create Release

```bash
# Tag release
git tag -a v1.0.0 -m "Release version 1.0.0"

# Push tag
git push origin v1.0.0

# Create GitHub release with APK
# Go to: https://github.com/yourusername/WinMobile-Android/releases
# Upload signed APK and changelog
```

---

## 🐛 Troubleshooting Build Issues

### Gradle Sync Fails

```bash
# Clean and resync
./gradlew clean
./gradlew sync

# Update Gradle
./gradlew wrapper --gradle-version=latest
```

### Build Fails with "Duplicate class"

```bash
# Check for duplicate dependencies
./gradlew dependencies

# Remove duplicates from build.gradle
```

### APK Not Installing

```bash
# Check minimum SDK
# app/build.gradle: minSdk 28

# Clear app data
adb shell pm clear com.winmobile

# Reinstall
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Out of Memory During Build

```bash
# Increase Gradle heap size
# In gradle.properties:
org.gradle.jvmargs=-Xmx4096m
```

---

## 📚 Resources

### Official Documentation
- Android Developers: https://developer.android.com/
- Gradle: https://gradle.org/
- Kotlin: https://kotlinlang.org/

### Tools
- Android Studio: https://developer.android.com/studio
- Android Emulator: https://developer.android.com/studio/run/emulator
- ADB: https://developer.android.com/studio/command-line/adb

### Learning
- Android Codelabs: https://developer.android.com/codelabs
- Kotlin Tutorials: https://kotlinlang.org/docs/

---

## ✅ Build Checklist

Before releasing:

- [ ] Code compiles without errors
- [ ] All tests pass
- [ ] ProGuard rules configured
- [ ] Version number updated
- [ ] Changelog updated
- [ ] APK signed
- [ ] APK tested on device
- [ ] Screenshots updated
- [ ] Description updated
- [ ] Privacy policy ready

---

**Happy building! 🚀**
