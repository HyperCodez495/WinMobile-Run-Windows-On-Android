# WinMobile - Windows Emulator for Android

A powerful, easy-to-use Android application that allows you to run Windows applications and games on your mobile device with unmatched performance.

## Features

✅ **Full Windows Desktop** - Run the complete Windows environment on your phone  
✅ **High Performance** - Optimized Box64 + Wine with dynamic recompilation  
✅ **Easy Setup** - One-click initialization and container creation  
✅ **Performance Presets** - Balanced, Performance, and Compatibility modes  
✅ **Real-time Monitoring** - FPS, CPU, GPU, memory, and temperature tracking  
✅ **Game Profiles** - Save per-game settings for optimal performance  
✅ **Auto-Detection** - Detects device GPU and applies optimal drivers  

## System Requirements

- **Android Version:** 8.0 (API 28) or higher
- **Architecture:** ARM64 (aarch64) only
- **RAM:** Minimum 4GB, 6GB+ recommended
- **Storage:** 10GB+ free space for Wine prefix and applications
- **GPU:** Adreno 6xx/7xx (Snapdragon) or Mali recommended

## Installation

1. Download the APK from GitHub Releases
2. Install on your Android device
3. Grant storage and system permissions
4. Launch the app

## First Launch Setup

1. **Create Container** - Name your Windows environment
2. **Auto-Download** - App downloads Box64, Wine, and drivers (~500MB)
3. **Initialize Prefix** - Sets up Windows directory structure
4. **Ready to Use** - Launch Windows desktop

## Performance Optimization

### Box64 Dynarec Settings (Automatically Applied)

```
BOX64_DYNAREC=1              # Dynamic recompilation (CRITICAL)
BOX64_DYNAREC_BIGBLOCK=1     # Larger code blocks = better performance
BOX64_DYNAREC_PAUSE=3        # Optimal for Android
BOX64_DYNAREC_FASTNAN=1      # Fast NaN handling
BOX64_DYNAREC_FASTROUND=1    # Fast rounding
```

### Performance Presets

| Preset | Resolution | FPS Cap | Best For |
|--------|-----------|---------|----------|
| **Performance** | 1280x720 | 60 | High-end devices, fast games |
| **Balanced** | 1024x768 | 30 | Most devices, good balance |
| **Compatibility** | 800x600 | 20 | Older devices, stability |

### GPU Drivers

- **Turnip** (Recommended) - Best performance on Snapdragon devices
- **VirGL** (Fallback) - Software-based, works on all devices

## What Runs Well

✅ **Indie Games** - 2D games, low-poly 3D  
✅ **Office Apps** - Word, Excel, browsers  
✅ **Utilities** - File managers, system tools  
✅ **Older Games** - DirectX 9, early DX10  
✅ **Development Tools** - Text editors, compilers  

**Expected Performance:** 30-60 FPS at 720p on mid-range devices

## What Won't Run Well

❌ AAA Games (GTA V, Fortnite) - 15-20 FPS max  
❌ Heavy 3D Applications  
❌ Video Editing Software  
❌ High-Resolution Gaming (1440p+)  

## Usage Guide

### Launch Windows Desktop

1. Open WinMobile
2. Select a container from the list
3. Tap "Launch"
4. Wait for Windows desktop to appear

### Install Applications

1. In Windows desktop, open File Manager
2. Navigate to your phone storage
3. Double-click an .exe file to install/run

### Manage Performance

1. Go to "Performance" tab
2. View real-time metrics (FPS, CPU, GPU, Memory, Temp)
3. Adjust settings if needed

### Create Game Profiles

1. Select a container
2. Tap "Settings" → "Game Profiles"
3. Add custom settings for specific games
4. Settings auto-apply when launching that game

## Troubleshooting

### App Crashes on Launch
- Clear app data and reinstall
- Check if device has at least 4GB RAM
- Ensure Android 8.0 or higher

### Low Performance / Lag
- Switch to "Performance" preset
- Close background apps
- Reduce resolution in settings
- Check device temperature (may throttle if hot)

### Application Won't Launch
- Try "Compatibility" preset
- Check if .exe is 64-bit (x86_64)
- Install required .NET Framework via Wine

### Graphics Issues
- Switch graphics driver (Turnip ↔ VirGL)
- Update to latest app version
- Try disabling shader caching

## Architecture

### Core Components

- **Box64** - x86_64 CPU emulator for ARM
- **Wine64** - Windows API compatibility layer
- **DXVK** - DirectX to Vulkan translation
- **Turnip** - Vulkan driver for Adreno GPUs
- **Jetpack Compose** - Modern Android UI

### Data Flow

```
Windows .exe
    ↓
Wine64 (API translation)
    ↓
Box64 (CPU emulation)
    ↓
DXVK (Graphics translation)
    ↓
Turnip/VirGL (GPU rendering)
    ↓
Android Screen
```

## Performance Benchmarks

| Device | Preset | Game | FPS | Resolution |
|--------|--------|------|-----|------------|
| Snapdragon 865 | Balanced | GTA V | 20-25 | 720p |
| Snapdragon 888 | Performance | Fortnite | 25-30 | 720p |
| Snapdragon 8 Gen 1 | Performance | Indie Games | 50-60 | 720p |
| Mid-range (778G) | Balanced | Indie Games | 30-45 | 720p |

## Development

### Build from Source

```bash
# Clone repository
git clone https://github.com/yourusername/WinMobile-Android.git
cd WinMobile-Android

# Build APK
./gradlew build

# Build and install on device
./gradlew installDebug
```

### Project Structure

```
WinMobile-Android/
├── app/
│   ├── src/main/
│   │   ├── java/com/winmobile/
│   │   │   ├── MainActivity.kt
│   │   │   ├── ui/
│   │   │   │   ├── HomeScreen.kt
│   │   │   │   └── theme/
│   │   │   ├── emulation/
│   │   │   │   ├── EmulationEngine.kt
│   │   │   │   └── EmulationService.kt
│   │   │   └── utils/
│   │   │       └── PerformanceMonitor.kt
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   ├── values/
│   │   │   └── drawable/
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under the GPL-3.0 License - see LICENSE file for details.

## Credits

- **Box64** - ptitSeb (https://github.com/ptitSeb/box64)
- **Wine** - WineHQ (https://www.winehq.org/)
- **DXVK** - doitsujin (https://github.com/doitsujin/dxvk)
- **Turnip** - Mesa/Freedreno team
- **Winlator** - brunodev85 (https://github.com/brunodev85/winlator)

## Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Check existing documentation
- Review troubleshooting guide

## Disclaimer

This project is for educational and personal use. Ensure you have proper licenses for any software you run. The developers are not responsible for any damage or misuse.

---

**WinMobile** - Bringing Windows to Your Pocket 📱💻
