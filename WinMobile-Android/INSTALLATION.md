# WinMobile Installation & Quick Start Guide

## 📋 System Requirements

### Minimum Requirements
- **Android Version:** 8.0 (API 28) or higher
- **Architecture:** ARM64 (aarch64) only
- **RAM:** 4GB minimum
- **Storage:** 10GB free space
- **GPU:** Any GPU with OpenGL ES 3.0+ support

### Recommended Requirements
- **Android Version:** 11.0 or higher
- **RAM:** 6GB or more
- **Storage:** 20GB+ free space
- **GPU:** Snapdragon Adreno 6xx/7xx or equivalent
- **Processor:** Snapdragon 888 or higher

### Not Supported
- ❌ 32-bit devices (ARM32)
- ❌ iOS devices
- ❌ Windows devices
- ❌ Devices with less than 4GB RAM

---

## 🔧 Installation Steps

### Step 1: Download APK

1. Go to GitHub Releases: https://github.com/yourusername/WinMobile-Android/releases
2. Download the latest `WinMobile.apk` file
3. Save to your phone's Downloads folder

### Step 2: Enable Unknown Sources

1. Open Settings
2. Go to Security or Privacy
3. Enable "Unknown Sources" or "Install from Unknown Sources"
4. (Some devices: Settings → Apps & notifications → Advanced → Special app access → Install unknown apps)

### Step 3: Install APK

**Method 1: File Manager**
1. Open File Manager
2. Navigate to Downloads
3. Tap `WinMobile.apk`
4. Tap "Install"
5. Wait for installation to complete

**Method 2: ADB (Advanced)**
```bash
adb install WinMobile.apk
```

### Step 4: Grant Permissions

1. Open WinMobile app
2. Grant requested permissions:
   - Storage access (required)
   - System permissions (required)
   - Vibration (optional)

---

## 🚀 First Launch Setup

### Step 1: Check Device Compatibility

When you first open WinMobile:
1. App automatically checks device compatibility
2. If compatible, you'll see a green checkmark
3. If not compatible, you'll see warnings or errors

**Common Issues:**
- "Not ARM64" - Device architecture not supported
- "Less than 4GB RAM" - Need more RAM
- "Android too old" - Upgrade Android version

### Step 2: Create Your First Container

1. Tap **"Create Container"** button
2. Enter a name (e.g., "MyWindows", "Gaming", "Work")
3. Tap **"Create"**
4. Wait for initialization (takes 1-2 minutes)

**What happens during initialization:**
- Creates Windows directory structure
- Downloads Box64 (~100MB)
- Downloads Wine (~300MB)
- Downloads GPU drivers (~50MB)
- **Total download: ~450MB**

### Step 3: Wait for Download

1. You'll see a progress bar
2. Download speed depends on your internet connection
3. Keep the app open during download
4. Do NOT close the app or turn off your phone

**Estimated download times:**
- Fast WiFi (100+ Mbps): 5-10 minutes
- Regular WiFi (50 Mbps): 10-20 minutes
- 4G LTE: 15-30 minutes
- 3G: 30-60+ minutes

### Step 4: Launch Windows

1. After download completes, you'll see "Ready" status
2. Tap **"Launch"** button
3. Wait 30-60 seconds for Windows to boot
4. Windows desktop appears on your screen!

---

## 💻 First Time Using Windows

### What You'll See

When Windows launches:
1. **Windows Desktop** - The familiar Windows interface
2. **Start Menu** - Click to access programs
3. **Taskbar** - At the bottom with system tray
4. **File Explorer** - Browse files and folders
5. **System Tray** - Clock and system notifications

### Basic Controls

**Mouse/Touch:**
- Tap once = Left click
- Long press = Right click
- Drag = Move mouse

**Keyboard:**
- On-screen keyboard appears when needed
- Connect Bluetooth keyboard for better experience

**Navigation:**
- Tap desktop to interact
- Swipe from edge to access Android
- Back button = Exit Windows

### Install Your First Application

1. In Windows, open **File Manager**
2. Navigate to `/storage/emulated/0/` (your phone storage)
3. Find a `.exe` file (Windows installer or portable app)
4. Double-click to run
5. Follow installation wizard

**Popular applications to try:**
- Notepad++ (text editor)
- 7-Zip (file archiver)
- VLC Media Player (video player)
- Blender (3D modeling)
- Indie games

---

## ⚙️ Initial Configuration

### Set Performance Preset

1. Open WinMobile
2. Go to **Settings** tab
3. Select performance preset:
   - **Performance** - Maximum FPS, high power usage
   - **Balanced** - Good FPS, moderate power usage
   - **Compatibility** - Lower FPS, stable, low power usage

### Select Graphics Driver

1. Settings → Graphics Driver
2. Choose:
   - **Turnip** - Best for Snapdragon (recommended)
   - **VirGL** - For other GPUs

### Adjust Resolution

1. Settings → Resolution
2. Choose:
   - **1280x720** - Best quality (requires high-end device)
   - **1024x768** - Balanced (recommended)
   - **800x600** - Best performance (budget devices)

### Set FPS Cap

1. Settings → FPS Cap
2. Choose:
   - **60** - Smooth but uses more battery
   - **30** - Balanced (recommended)
   - **20** - Battery saver

---

## 📊 Performance Monitoring

### View Real-Time Metrics

1. Open WinMobile
2. Go to **Performance** tab
3. See live metrics:
   - **FPS** - Frames per second (target: 30+)
   - **CPU** - CPU usage percentage
   - **GPU** - GPU usage percentage
   - **Memory** - RAM used in MB
   - **Temperature** - Device temperature in °C

### Optimize if Needed

**If FPS is too low:**
- Switch to "Performance" preset
- Reduce resolution
- Close background apps

**If device is hot:**
- Reduce FPS cap
- Switch to "Balanced" preset
- Take a break

---

## 🎮 Installing Games

### Before Installing

1. Check device compatibility
2. Ensure 5-10GB free storage
3. Close background apps
4. Keep device cool

### Installation Process

1. Download game `.exe` file to phone
2. In Windows, open File Manager
3. Navigate to Downloads folder
4. Double-click game installer
5. Follow installation wizard
6. Wait for installation (can take 5-30 minutes)

### Launch Game

1. After installation, find game shortcut
2. Double-click to launch
3. Game runs in Windows environment
4. Adjust graphics settings if needed

### Game Performance Tips

- Start with "Balanced" preset
- Reduce in-game graphics settings
- Close other apps
- Ensure device is cool
- Use "Performance" preset for better FPS

---

## 🔄 Updating WinMobile

### Check for Updates

1. Open WinMobile
2. Settings → About
3. Check current version
4. If update available, tap "Update"

### Update Process

1. Download new APK
2. Install like first installation
3. Existing containers are preserved
4. No need to recreate containers

---

## 🆘 Troubleshooting

### App Won't Install

**Problem:** "Installation failed"

**Solutions:**
1. Enable "Unknown Sources" in Security settings
2. Check if you have enough storage (500MB+ free)
3. Try downloading APK again
4. Restart phone and try again

### App Crashes on Startup

**Problem:** App crashes immediately after opening

**Solutions:**
1. Clear app cache:
   - Settings → Apps → WinMobile → Storage → Clear Cache
2. Clear app data (WARNING: deletes containers):
   - Settings → Apps → WinMobile → Storage → Clear Data
3. Reinstall app
4. Check if device has 4GB+ RAM available

### Download Stuck

**Problem:** Download doesn't progress

**Solutions:**
1. Check internet connection (WiFi recommended)
2. Close other apps using internet
3. Cancel and retry download
4. Try using mobile data instead of WiFi

### Windows Won't Launch

**Problem:** Container shows "Ready" but won't launch

**Solutions:**
1. Wait a few seconds and try again
2. Close and reopen WinMobile
3. Restart phone
4. Delete container and create new one

### Very Low Performance

**Problem:** FPS is 5-10, very laggy

**Solutions:**
1. Switch to "Performance" preset
2. Reduce resolution to 800x600
3. Close all background apps
4. Restart phone
5. Check device temperature (may be throttling)

### Graphics Glitches

**Problem:** Screen artifacts, flickering, or visual bugs

**Solutions:**
1. Switch graphics driver (Turnip ↔ VirGL)
2. Disable shader caching in settings
3. Update app to latest version
4. Try different resolution

---

## 📱 Device-Specific Setup

### Snapdragon Devices (Recommended)

**Best Performance:**
- Preset: Performance
- Driver: Turnip
- Resolution: 1280x720
- FPS Cap: 60

**Expected Performance:** 40-60 FPS in indie games

### Exynos Devices

**Balanced Setup:**
- Preset: Balanced
- Driver: VirGL
- Resolution: 1024x768
- FPS Cap: 30

**Expected Performance:** 20-30 FPS in indie games

### MediaTek Devices

**Conservative Setup:**
- Preset: Compatibility
- Driver: VirGL
- Resolution: 800x600
- FPS Cap: 20

**Expected Performance:** 15-25 FPS in indie games

---

## 🔐 Data & Privacy

### What Data is Stored?

- Windows containers (on your device)
- Game settings and profiles
- Performance logs
- Cache files

### Where is Data Stored?

- Internal storage: `/data/data/com.winmobile/`
- External storage: `/Android/data/com.winmobile/`

### Privacy

- No data sent to servers
- No ads or tracking
- All data stays on your device
- Open source (can audit code)

---

## 📞 Getting Help

### Common Resources

- **GitHub Issues:** Report bugs or request features
- **Documentation:** Check README.md and SETUP_GUIDE.md
- **Community:** Join Discord for tips and support

### Before Asking for Help

1. Check troubleshooting section above
2. Read documentation
3. Search existing GitHub issues
4. Provide device info (model, Android version, RAM)
5. Describe what you tried

---

## ✅ Verification Checklist

After installation, verify:

- [ ] App installed successfully
- [ ] Permissions granted
- [ ] Device compatibility check passed
- [ ] Container created
- [ ] Download completed
- [ ] Windows launched
- [ ] Can see Windows desktop
- [ ] Can interact with Windows
- [ ] Performance metrics visible

---

## 🎉 You're Ready!

Congratulations! You now have Windows running on your mobile device. 

**Next steps:**
1. Explore Windows desktop
2. Install applications
3. Optimize settings for your device
4. Join community and share your experience

**Happy computing! 💻📱**
