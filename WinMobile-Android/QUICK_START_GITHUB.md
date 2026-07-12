# Quick Start: Deploy WinMobile with GitHub Actions

Get your WinMobile app building automatically in 5 minutes!

## Step 1: Create GitHub Repository

1. Go to https://github.com/new
2. Repository name: `WinMobile-Android`
3. Description: "Windows Emulator for Android"
4. Choose Public or Private
5. Click "Create repository"

## Step 2: Push Project to GitHub

```bash
cd ~/WinMobile-Android

# Initialize git
git init
git add .
git commit -m "Initial commit: WinMobile Android app"

# Add remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/WinMobile-Android.git
git branch -M main
git push -u origin main
```

## Step 3: Verify GitHub Actions

1. Go to your GitHub repository
2. Click "Actions" tab
3. You should see two workflows:
   - **Build WinMobile APK** - Builds on every push
   - **Run Tests** - Tests on every push

4. Click on a workflow to see it running
5. Wait for build to complete (~10 minutes)

## Step 4: Download Your APK

1. Go to Actions tab
2. Click on the latest "Build WinMobile APK" workflow
3. Scroll down to "Artifacts"
4. Download `WinMobile-debug` or `WinMobile-release-unsigned`
5. Install on your Android device

## Step 5: Set Up Automatic Signing (Optional)

To automatically sign and release APKs:

### Generate Signing Key

```bash
# Create keystore
keytool -genkey -v -keystore winmobile.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias winmobile-key \
  -keypass YourKeyPassword \
  -storepass YourStorePassword

# Encode to Base64
base64 winmobile.keystore > winmobile.keystore.b64
cat winmobile.keystore.b64
```

### Add GitHub Secrets

1. Go to repository Settings
2. Click "Secrets and variables" → "Actions"
3. Click "New repository secret"
4. Add these 4 secrets:

| Name | Value |
|------|-------|
| `SIGNING_KEY` | Output from `cat winmobile.keystore.b64` |
| `KEY_ALIAS` | `winmobile-key` |
| `KEY_STORE_PASSWORD` | Your store password |
| `KEY_PASSWORD` | Your key password |

### Create Release

```bash
# Create tag
git tag -a v1.0.0 -m "Release version 1.0.0"

# Push tag
git push origin v1.0.0
```

GitHub Actions will automatically:
- Build the APK
- Sign it
- Create a GitHub Release
- Upload signed APK

## Step 6: Make Changes and Auto-Build

Every time you push code:

```bash
# Make changes to code
# ...

# Commit and push
git add .
git commit -m "Your changes"
git push origin main
```

GitHub Actions automatically:
1. Builds debug APK
2. Builds release APK
3. Runs tests
4. Runs lint checks
5. Uploads artifacts

## 🎯 What Happens Automatically

### On Every Push to `main` or `develop`:

✅ Build debug APK  
✅ Build release APK  
✅ Run unit tests  
✅ Run lint checks  
✅ Security scanning  
✅ Upload artifacts  
✅ Generate reports  

### On Tag Creation (v*):

✅ All above steps  
✅ Sign APK  
✅ Create GitHub Release  
✅ Upload signed APK  

### Daily at 2 AM UTC:

✅ Run all tests  
✅ Check code quality  
✅ Scan dependencies  
✅ Generate coverage report  

## 📊 Monitoring Builds

### View Build Status

1. Go to Actions tab
2. See all workflow runs
3. Green checkmark = Success
4. Red X = Failed

### Download Artifacts

1. Click on workflow run
2. Scroll to "Artifacts"
3. Download APK files
4. Install on device

### View Test Results

1. Click on workflow run
2. Click "test-results" artifact
3. Download and open HTML report

## 🔍 Troubleshooting

### Build Fails: "Gradle wrapper not found"

**Solution:** Add gradle wrapper

```bash
./gradlew wrapper --gradle-version=latest
git add gradle/wrapper/
git commit -m "Add gradle wrapper"
git push
```

### Build Fails: "SDK not found"

The workflow downloads SDK automatically. If it fails:
1. Check build logs for error
2. May need to update SDK versions in `build.gradle`
3. Retry workflow

### Can't Find Artifacts

1. Make sure workflow completed (green checkmark)
2. Scroll down in workflow run page
3. Look for "Artifacts" section
4. Download APK files

### Signing Fails

1. Verify GitHub secrets are correct
2. Regenerate keystore if needed
3. Re-encode to Base64
4. Update secrets
5. Retry workflow

## 📱 Install APK on Device

### From GitHub Actions

1. Download APK from artifacts
2. Transfer to phone
3. Enable "Unknown Sources" in Settings
4. Tap APK to install

### Via ADB

```bash
adb install WinMobile-debug.apk
```

## 🚀 Next Steps

1. **Customize the app** - Edit Kotlin files
2. **Add features** - Implement new functionality
3. **Test locally** - Build and test on device
4. **Push to GitHub** - Trigger automatic build
5. **Download APK** - Get built artifact
6. **Share with others** - Create releases

## 📚 More Information

- **Build Guide:** See `BUILD_GUIDE.md`
- **GitHub Actions Setup:** See `GITHUB_ACTIONS_SETUP.md`
- **Installation Guide:** See `INSTALLATION.md`
- **Project Overview:** See `PROJECT_OVERVIEW.md`

## ✅ Verification Checklist

- [ ] Repository created on GitHub
- [ ] Code pushed to main branch
- [ ] Actions tab shows workflows
- [ ] Build workflow completed successfully
- [ ] Debug APK downloaded
- [ ] APK installed on device
- [ ] App launches successfully
- [ ] GitHub secrets configured (optional)
- [ ] Release tag created (optional)
- [ ] Signed APK released (optional)

## 🎉 You're Done!

Your WinMobile app now has:
- ✅ Automated builds on every push
- ✅ Automated testing
- ✅ Automated signing and releasing
- ✅ Security scanning
- ✅ Code quality checks

Every push to GitHub automatically builds your app!

**Happy building! 🚀**

---

**Need Help?**
- GitHub Issues: Report bugs
- GitHub Discussions: Ask questions
- Check documentation files
