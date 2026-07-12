# WinMobile Android App - Project Overview

## 🎯 Project Goal

Build a **custom Android application that runs Windows natively on mobile devices** with high performance and ease of use. The app integrates Box64 (x86_64 CPU emulator) and Wine (Windows compatibility layer) to create a complete Windows environment on Android.

## 📱 What You Get

When you launch WinMobile, you see:
- **Full Windows Desktop** - The classic Windows interface running on your phone
- **Windows Start Menu** - Launch programs and access settings
- **File Explorer** - Browse your phone's storage as Windows sees it
- **Run Any Windows .exe** - Install and run Windows applications
- **Real-time Performance Monitoring** - FPS, CPU, GPU, memory, temperature

## 🏗️ Architecture

### Core Components

```
┌─────────────────────────────────────────────────┐
│           WinMobile Android App                 │
│        (Jetpack Compose UI)                     │
├─────────────────────────────────────────────────┤
│  EmulationEngine  │  EmulationService           │
│  (Orchestration)  │  (Background Service)       │
├─────────────────────────────────────────────────┤
│  Box64 (x86_64 CPU Emulator)                    │
│  Wine64 (Windows API Layer)                     │
│  DXVK (DirectX → Vulkan Translation)            │
├─────────────────────────────────────────────────┤
│  Turnip/VirGL (GPU Drivers)                     │
│  Mesa (Graphics Library)                        │
├─────────────────────────────────────────────────┤
│           Android Kernel                        │
│           ARM64 CPU                             │
└─────────────────────────────────────────────────┘
```

### Data Flow

```
Windows .exe File
      ↓
Wine64 (Translates Windows API to Linux API)
      ↓
Box64 (Translates x86_64 CPU instructions to ARM64)
      ↓
DXVK (Translates DirectX calls to Vulkan)
      ↓
Turnip/VirGL (GPU Driver)
      ↓
Android GPU
      ↓
Phone Screen
```

## 📂 Project Structure

```
WinMobile-Android/
│
├── README.md                          # Main documentation
├── SETUP_GUIDE.md                     # User setup and optimization guide
├── PROJECT_OVERVIEW.md                # This file
│
├── build.gradle                       # Root build configuration
├── settings.gradle                    # Project settings
│
└── app/
    ├── build.gradle                   # App build configuration
    ├── proguard-rules.pro             # Code obfuscation rules
    │
    └── src/main/
        ├── AndroidManifest.xml        # App manifest with permissions
        │
        ├── java/com/winmobile/
        │   ├── MainActivity.kt         # App entry point
        │   │
        │   ├── ui/
        │   │   ├── HomeScreen.kt       # Main UI with tabs
        │   │   │   ├── ContainersTab   # Manage Windows containers
        │   │   │   ├── SettingsTab     # App settings
        │   │   │   └── PerformanceTab  # Real-time metrics
        │   │   │
        │   │   └── theme/
        │   │       ├── Theme.kt        # Material Design 3 theme
        │   │       └── Type.kt         # Typography configuration
        │   │
        │   ├── emulation/
        │   │   ├── EmulationEngine.kt  # Box64 + Wine orchestration
        │   │   │   ├── initialize()    # Setup Box64 & Wine
        │   │   │   ├── launchApplication()
        │   │   │   └── stopApplication()
        │   │   │
        │   │   └── EmulationService.kt # Background service
        │   │       └── Manages emulation lifecycle
        │   │
        │   └── utils/
        │       └── PerformanceMonitor.kt # Real-time metrics
        │           ├── getMetrics()
        │           ├── calculateFPS()
        │           ├── calculateCPUUsage()
        │           ├── calculateGPUUsage()
        │           ├── calculateMemoryUsage()
        │           └── getDeviceTemperature()
        │
        └── res/
            ├── values/
            │   ├── strings.xml         # UI text strings
            │   └── themes.xml          # Color definitions
            │
            ├── layout/                 # (Reserved for future)
            └── drawable/               # (Reserved for future)
```

## 🔧 Key Features Implemented

### 1. **EmulationEngine** (`emulation/EmulationEngine.kt`)
- **Initializes** Box64 and Wine binaries
- **Manages** Wine prefix (C: drive)
- **Launches** Windows applications with optimized settings
- **Applies** performance presets (Performance/Balanced/Compatibility)
- **Handles** Box64 dynarec configuration

**Key Methods:**
```kotlin
initialize()              // Download and setup components
launchApplication()       // Run a Windows .exe
stopApplication()         // Terminate emulation
getSystemInfo()          // Device compatibility check
```

### 2. **EmulationService** (`emulation/EmulationService.kt`)
- **Background service** that keeps emulation running
- **Prevents** system from killing the emulation process
- **Manages** application lifecycle
- **Handles** async operations with Coroutines

### 3. **HomeScreen UI** (`ui/HomeScreen.kt`)
- **Three main tabs:**
  - **Containers** - Create and manage Windows environments
  - **Settings** - Configure performance and graphics
  - **Performance** - Real-time monitoring

- **Container Management:**
  - Create new Windows containers
  - Launch containers
  - Delete containers
  - View container status

- **Settings:**
  - Resolution (720p, 768p, 600p)
  - FPS cap (20, 30, 60)
  - Graphics driver (Turnip, VirGL)
  - Performance preset

### 4. **PerformanceMonitor** (`utils/PerformanceMonitor.kt`)
- **Real-time tracking** of:
  - FPS (frames per second)
  - CPU usage (%)
  - GPU usage (%)
  - Memory usage (MB)
  - Device temperature (°C)

- **Used for:**
  - Performance monitoring UI
  - Thermal throttling detection
  - Resource optimization

### 5. **Material Design 3 Theme** (`ui/theme/`)
- **Cyberpunk aesthetic:**
  - Primary: Electric Cyan (#00D9FF)
  - Secondary: Vibrant Purple (#7C3AED)
  - Background: Deep Charcoal (#0F172A)
  - Success: Lime Green (#00FF00)

- **Typography:**
  - Display fonts for headlines
  - Body fonts for content
  - Label fonts for UI elements

## 🚀 Performance Optimization Strategy

### Box64 Dynarec Settings (Automatically Applied)

```kotlin
BOX64_DYNAREC=1              // Enable dynamic recompilation (CRITICAL)
BOX64_DYNAREC_BIGBLOCK=1     // Build larger code blocks
BOX64_DYNAREC_PAUSE=3        // Optimal pause setting for Android
BOX64_DYNAREC_FASTNAN=1      // Fast NaN handling
BOX64_DYNAREC_FASTROUND=1    // Fast rounding
```

**Performance Preset Configurations:**

| Preset | Resolution | FPS Cap | Box64 Settings | Use Case |
|--------|-----------|---------|---|---|
| **Performance** | 1280x720 | 60 | STRONGMEM=0, SAFEFLAGS=0 | High-end devices |
| **Balanced** | 1024x768 | 30 | STRONGMEM=1, SAFEFLAGS=1 | Most devices |
| **Compatibility** | 800x600 | 20 | Interpreter mode | Stability |

### Graphics Optimization

- **Turnip Driver** - Best on Snapdragon (Adreno GPU)
- **VirGL Driver** - Fallback for other GPUs
- **DXVK 2.5+** - Latest DirectX translation
- **Shader Caching** - Reduces stuttering

## 📊 Performance Targets

### Expected Performance by Device

| Device Type | Preset | Indie Games | AAA Games |
|-------------|--------|------------|-----------|
| Flagship (SD 8 Gen 1+) | Performance | 50-60 FPS | 25-35 FPS |
| High-End (SD 888) | Balanced | 35-45 FPS | 20-25 FPS |
| Mid-Range (SD 778G) | Balanced | 25-35 FPS | 15-20 FPS |
| Budget (SD 665) | Compatibility | 15-25 FPS | 10-15 FPS |

## 🔐 Permissions Required

```xml
<!-- Storage -->
READ_EXTERNAL_STORAGE
WRITE_EXTERNAL_STORAGE
MANAGE_EXTERNAL_STORAGE

<!-- System -->
INTERNET
ACCESS_NETWORK_STATE

<!-- Performance -->
GET_TASKS
PACKAGE_USAGE_STATS

<!-- Input -->
VIBRATE
```

## 📦 Dependencies

### Core Libraries
- **Jetpack Compose** - Modern Android UI
- **Material 3** - Design system
- **AndroidX** - Android framework
- **Kotlin Coroutines** - Async operations

### Build Tools
- **Gradle 8.1** - Build system
- **Kotlin 1.9** - Language
- **Android SDK 34** - Target SDK
- **Min SDK 28** - Android 8.0+

## 🎮 Usage Flow

### 1. First Launch
```
App Start
  ↓
Request Permissions
  ↓
Create Container (Dialog)
  ↓
Auto-Download Box64 + Wine (~500MB)
  ↓
Initialize Wine Prefix
  ↓
Ready to Use
```

### 2. Launch Windows
```
Select Container
  ↓
Tap "Launch"
  ↓
Start EmulationService
  ↓
Initialize EmulationEngine
  ↓
Launch Wine Desktop
  ↓
Windows Appears on Screen
```

### 3. Run Application
```
In Windows Desktop
  ↓
Open File Manager
  ↓
Navigate to .exe file
  ↓
Double-click to run
  ↓
Application launches in Windows
```

## 🔄 Build & Deployment

### Build APK
```bash
./gradlew build
```

### Build and Install on Device
```bash
./gradlew installDebug
```

### Create Release APK
```bash
./gradlew assembleRelease
```

### APK Size Estimate
- App APK: ~50MB
- Box64 binary: ~100MB (downloaded on first run)
- Wine: ~300MB (downloaded on first run)
- Total: ~450MB + user data

## 🐛 Debugging

### View Logs
```bash
adb logcat | grep "WinMobile"
```

### Debug on Device
```bash
./gradlew installDebug
adb shell am start -D -N com.winmobile/.MainActivity
```

### Performance Profiling
- Use Android Studio Profiler
- Monitor CPU, GPU, Memory, Battery

## 🔮 Future Enhancements

### Planned Features
- [ ] Game library integration (Steam, GOG)
- [ ] Cloud save sync
- [ ] Multi-container management UI
- [ ] Custom game profiles with per-game settings
- [ ] Controller support (gamepad mapping)
- [ ] Recording and streaming
- [ ] Network drive mounting
- [ ] Cloud gaming integration

### Performance Improvements
- [ ] GPU-accelerated rendering
- [ ] Improved shader caching
- [ ] Advanced dynarec optimizations
- [ ] Memory compression
- [ ] Thermal management

## 📝 Development Notes

### Important Considerations

1. **ARM64 Only** - Box64 only supports ARM64 architecture
2. **Storage** - Each container needs 5-10GB space
3. **RAM** - Minimum 4GB, 6GB+ recommended
4. **Thermals** - Emulation generates heat, thermal throttling may occur
5. **Battery** - Expect 50-70% battery drain per hour

### Common Issues

- **Crashes on Startup** - Clear app data, check RAM
- **Low Performance** - Switch preset, close background apps
- **Graphics Issues** - Switch GPU driver (Turnip ↔ VirGL)
- **App Won't Launch** - Try Compatibility preset

## 📚 Resources

### GitHub Projects Used
- **Box64** - https://github.com/ptitSeb/box64
- **Wine** - https://www.winehq.org/
- **DXVK** - https://github.com/doitsujin/dxvk
- **Turnip** - Mesa/Freedreno project
- **Winlator** - https://github.com/brunodev85/winlator

### Documentation
- Android Developers: https://developer.android.com/
- Jetpack Compose: https://developer.android.com/jetpack/compose
- Box64 Docs: https://github.com/ptitSeb/box64/tree/main/docs
- Wine Docs: https://wiki.winehq.org/

## 📄 License

This project is licensed under **GPL-3.0** License.

See LICENSE file for full text.

## 👥 Contributing

Contributions welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📞 Support

- **Issues** - GitHub Issues
- **Discussions** - GitHub Discussions
- **Email** - winmobile@example.com

---

**WinMobile** - Bringing Windows to Your Pocket 📱💻

Last Updated: July 2026
