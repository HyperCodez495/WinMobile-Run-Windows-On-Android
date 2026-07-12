# WinMobile Setup & Optimization Guide

## Quick Start (5 Minutes)

### Step 1: Installation
1. Download `WinMobile.apk` from GitHub Releases
2. Enable "Unknown Sources" in Settings → Security
3. Install the APK
4. Open WinMobile

### Step 2: Initial Setup
1. Tap **"Create Container"**
2. Enter a name (e.g., "MyWindows")
3. Tap **"Create"**
4. Wait for automatic download (~500MB)

### Step 3: Launch
1. Select your container
2. Tap **"Launch"**
3. Windows desktop appears in ~30 seconds

### Step 4: Use Windows
- Click on desktop to interact
- Use on-screen keyboard/mouse
- Install programs like on regular Windows

---

## Performance Optimization Guide

### For Maximum Performance (High-End Devices)

**Recommended Devices:** Snapdragon 888+, 8 Gen 1+

1. Open Settings
2. Select **"Performance"** preset
3. Set Resolution to **1280x720**
4. Set FPS Cap to **60**
5. Graphics Driver: **Turnip** (auto-detected)

**Expected Performance:** 40-60 FPS in indie games, 25-35 FPS in AAA games

### For Balanced Performance (Mid-Range Devices)

**Recommended Devices:** Snapdragon 778G, 870, 888

1. Open Settings
2. Select **"Balanced"** preset
3. Set Resolution to **1024x768**
4. Set FPS Cap to **30**
5. Graphics Driver: **Turnip** or **VirGL**

**Expected Performance:** 25-40 FPS in indie games, 15-25 FPS in AAA games

### For Stability (Budget/Older Devices)

**Recommended Devices:** Snapdragon 665, 778G, older flagships

1. Open Settings
2. Select **"Compatibility"** preset
3. Set Resolution to **800x600**
4. Set FPS Cap to **20**
5. Graphics Driver: **VirGL** (software rendering)

**Expected Performance:** 15-30 FPS in indie games, 10-15 FPS in AAA games

---

## Advanced Optimization

### Box64 Environment Variables

These are automatically set, but you can customize them:

```
BOX64_DYNAREC=1              # Enable dynamic recompilation (CRITICAL)
BOX64_DYNAREC_BIGBLOCK=1     # Build larger code blocks
BOX64_DYNAREC_PAUSE=3        # Optimal pause setting
BOX64_DYNAREC_STRONGMEM=1    # Strong memory model (safer)
BOX64_DYNAREC_SAFEFLAGS=1    # Safe flag handling
BOX64_DYNAREC_FASTNAN=1      # Fast NaN handling
BOX64_DYNAREC_FASTROUND=1    # Fast rounding
BOX64_DYNAREC_BLEEDING_EDGE=1 # Use latest optimizations
```

**For Maximum Performance (risky):**
```
BOX64_DYNAREC_STRONGMEM=0
BOX64_DYNAREC_SAFEFLAGS=0
BOX64_DYNAREC_BLEEDING_EDGE=1
```

### Wine Configuration

1. In Windows desktop, open **Wine Configuration**
2. Graphics tab:
   - Enable CSMT (multithreading)
   - Disable Strict Draw Ordering
   - Enable Video Memory Size (set to device RAM)

3. Libraries tab:
   - Override DirectX libraries with DXVK versions

### GPU Driver Selection

**Turnip (Recommended for Snapdragon):**
- Best performance on Adreno 6xx/7xx
- Hardware-accelerated Vulkan
- Supports DXVK 2.5+

**VirGL (Fallback):**
- Works on all GPUs
- Software-based rendering
- Lower performance but more compatible

**Switch in Settings:**
1. Go to Settings → Graphics Driver
2. Select Turnip or VirGL
3. Restart container

---

## Game-Specific Optimization

### For Indie Games (2D/Low-Poly)

**Settings:**
- Preset: **Performance**
- Resolution: **1280x720**
- FPS Cap: **60**
- DXVK: **Latest version**

**Expected:** 40-60 FPS

### For AAA Games (GTA V, Fortnite)

**Settings:**
- Preset: **Balanced**
- Resolution: **1024x768**
- FPS Cap: **30**
- In-game graphics: **Low/Medium**
- DXVK: **Async enabled**

**Expected:** 20-30 FPS

### For Older Games (DirectX 9)

**Settings:**
- Preset: **Compatibility**
- Resolution: **800x600**
- FPS Cap: **20**
- Wine: **Use Mono for .NET support**

**Expected:** 15-30 FPS

---

## Troubleshooting

### Issue: App Crashes on Startup

**Solution:**
1. Go to Settings → Apps → WinMobile
2. Tap "Storage" → "Clear Cache"
3. Tap "Clear Data"
4. Reinstall app
5. Ensure 4GB+ RAM available

### Issue: Very Low FPS / Extreme Lag

**Solution:**
1. Close all background apps
2. Switch to "Performance" preset
3. Reduce resolution to 800x600
4. Check device temperature (may be throttling)
5. Try switching graphics driver

### Issue: Application Won't Launch

**Solution:**
1. Switch to "Compatibility" preset
2. Check if .exe is 64-bit (x86_64)
3. Install required libraries:
   - Wine Mono (for .NET)
   - Wine Gecko (for IE)
4. Try running in Wine Configuration

### Issue: Graphics Glitches / Artifacts

**Solution:**
1. Switch graphics driver (Turnip ↔ VirGL)
2. Disable shader caching
3. Update to latest app version
4. Try different DXVK version

### Issue: High Temperature / Battery Drain

**Solution:**
1. Reduce FPS cap to 30
2. Lower resolution to 1024x768
3. Close background apps
4. Use "Balanced" preset
5. Take breaks to let device cool

---

## Performance Monitoring

### Real-Time Metrics

Open **Performance** tab to see:
- **FPS** - Frames per second (target: 30+)
- **CPU Usage** - CPU load percentage
- **GPU Usage** - GPU load percentage
- **Memory** - RAM used in MB
- **Temperature** - Device temperature in °C

### Optimization Tips Based on Metrics

| Metric | Issue | Solution |
|--------|-------|----------|
| FPS < 20 | Too slow | Switch to Performance preset |
| CPU > 90% | Bottleneck | Reduce resolution or FPS cap |
| GPU > 90% | Bottleneck | Switch graphics driver |
| Memory > 3GB | Low RAM | Close background apps |
| Temp > 45°C | Overheating | Reduce settings, take break |

---

## Storage Management

### Container Size

Each container takes ~5-10GB:
- Wine prefix: ~2GB
- Box64 binary: ~100MB
- Installed apps: ~3-8GB

### Free Up Space

1. Delete unused containers
2. Uninstall unused Windows programs
3. Clear Wine cache: Settings → Clear Cache
4. Move container to external storage (if supported)

---

## Advanced Settings

### Custom Game Profiles

1. Select container
2. Tap "Settings" → "Game Profiles"
3. Add new profile:
   - Name: Game name
   - Resolution: Custom
   - FPS Cap: Custom
   - Graphics Driver: Turnip/VirGL
   - Box64 Preset: Performance/Balanced/Compatibility

4. Settings auto-apply when launching that game

### Environment Variables

For advanced users, set custom Box64 variables:

1. Settings → Advanced → Environment Variables
2. Add custom variables
3. Restart container to apply

---

## Device Compatibility

### Fully Supported

✅ Snapdragon 888, 8 Gen 1, 8 Gen 2+  
✅ Exynos 2200+  
✅ MediaTek Dimensity 9000+  

### Partially Supported

⚠️ Snapdragon 870, 778G (good performance)  
⚠️ Exynos 1280 (moderate performance)  

### Not Recommended

❌ Snapdragon 665 and below  
❌ Exynos 1080 and below  
❌ Devices with < 4GB RAM  

---

## FAQ

**Q: Can I run Windows 11?**
A: Yes, Wine runs Windows 11 API. You see Windows 10/11 desktop.

**Q: How much storage do I need?**
A: Minimum 10GB free space. 20GB+ recommended for multiple containers.

**Q: Can I play AAA games?**
A: Yes, but expect 15-25 FPS at 720p with medium settings.

**Q: Will it drain my battery?**
A: Yes, emulation is CPU/GPU intensive. Expect 50-70% battery per hour.

**Q: Can I connect a mouse/keyboard?**
A: Yes, Bluetooth or USB devices work. On-screen controls also available.

**Q: Is it legal?**
A: Yes, Wine is open-source and legal. Ensure you have licenses for software you run.

---

## Performance Benchmarks

### Indie Games (2D/Low-Poly)

| Device | Preset | FPS | Resolution |
|--------|--------|-----|------------|
| SD 8 Gen 1 | Performance | 50-60 | 1280x720 |
| SD 888 | Balanced | 35-45 | 1024x768 |
| SD 778G | Balanced | 25-35 | 1024x768 |

### AAA Games (GTA V)

| Device | Preset | FPS | Resolution |
|--------|--------|-----|------------|
| SD 8 Gen 1 | Balanced | 25-30 | 1024x768 |
| SD 888 | Balanced | 20-25 | 1024x768 |
| SD 778G | Compatibility | 15-20 | 800x600 |

---

## Getting Help

- **GitHub Issues:** Report bugs and request features
- **Discord:** Join community for tips and support
- **Documentation:** Check README.md for more info
- **Troubleshooting:** See troubleshooting section above

---

**Happy Gaming! 🎮**
