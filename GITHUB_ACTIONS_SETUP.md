# GitHub Actions Setup Guide

This guide explains how to set up GitHub Actions for automated building and testing of WinMobile.

## 🚀 Quick Start

### 1. Push Project to GitHub

```bash
# Initialize git repo
git init
git add .
git commit -m "Initial commit: WinMobile Android app"

# Add remote (replace with your repo)
git remote add origin https://github.com/yourusername/WinMobile-Android.git
git branch -M main
git push -u origin main
```

### 2. GitHub Actions Automatically Activates

Once you push to GitHub:
1. Go to your repository
2. Click "Actions" tab
3. Workflows will start running automatically

### 3. View Build Results

- **Build logs:** Actions → Workflow → Job output
- **Artifacts:** Actions → Workflow → Artifacts (download APK)
- **Test results:** Actions → Workflow → Test results

---

## 🔐 Setting Up Signing (For Releases)

To automatically sign and release APKs, you need to configure secrets.

### Step 1: Generate Signing Key

```bash
# Create keystore file
keytool -genkey -v -keystore winmobile.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias winmobile-key \
  -keypass your_key_password \
  -storepass your_store_password

# Encode to Base64
base64 winmobile.keystore > winmobile.keystore.b64
```

### Step 2: Add GitHub Secrets

1. Go to your GitHub repository
2. Settings → Secrets and variables → Actions
3. Click "New repository secret"
4. Add these secrets:

| Secret Name | Value |
|------------|-------|
| `SIGNING_KEY` | Contents of `winmobile.keystore.b64` |
| `KEY_ALIAS` | `winmobile-key` |
| `KEY_STORE_PASSWORD` | Your store password |
| `KEY_PASSWORD` | Your key password |

### Step 3: Create Release

```bash
# Create tag
git tag -a v1.0.0 -m "Release version 1.0.0"

# Push tag
git push origin v1.0.0
```

GitHub Actions will:
1. Build the APK
2. Sign it automatically
3. Create a GitHub Release
4. Upload signed APK

---

## 📋 Workflows Included

### 1. **build.yml** - Main Build Workflow

**Triggers:**
- Push to `main` or `develop` branches
- Create a tag (v*)
- Manual trigger

**Jobs:**
- Build debug APK
- Build release APK
- Run unit tests
- Run lint checks
- Security scanning

**Outputs:**
- Debug APK
- Release APK (unsigned)
- Test reports
- Lint reports

### 2. **test.yml** - Testing Workflow

**Triggers:**
- Push to `main` or `develop` branches
- Daily at 2 AM UTC

**Jobs:**
- Unit tests
- Code quality checks
- Dependency vulnerability scanning
- Code coverage

**Outputs:**
- Test results
- Coverage reports
- Code quality reports

---

## 🔄 Workflow Details

### Build Workflow (build.yml)

```
┌─────────────────────────────────────────┐
│ Push to main/develop or create tag      │
└──────────────┬──────────────────────────┘
               │
               ├─→ Build debug APK
               ├─→ Build release APK
               ├─→ Run unit tests
               ├─→ Run lint
               └─→ Security scan
               │
               ├─→ Upload artifacts
               │
               └─→ If tag: Sign & Release
                   ├─→ Sign APK
                   ├─→ Create GitHub Release
                   └─→ Upload signed APK
```

### Test Workflow (test.yml)

```
┌─────────────────────────────────────────┐
│ Push to main/develop or daily schedule  │
└──────────────┬──────────────────────────┘
               │
               ├─→ Unit tests
               ├─→ Code quality (ktlint, detekt)
               ├─→ Lint checks
               ├─→ Dependency vulnerabilities
               └─→ Code coverage
               │
               └─→ Upload reports
```

---

## 📊 Monitoring Builds

### GitHub Actions Dashboard

1. Go to repository → Actions
2. See all workflow runs
3. Click on a run to see details
4. View logs for each job

### Build Status Badge

Add to README.md:

```markdown
[![Build Status](https://github.com/yourusername/WinMobile-Android/actions/workflows/build.yml/badge.svg)](https://github.com/yourusername/WinMobile-Android/actions)
```

### Email Notifications

1. GitHub Settings → Notifications
2. Enable "Actions"
3. Get notified on build failures

---

## 🐛 Troubleshooting

### Build Fails: "Gradle wrapper not found"

**Solution:**
```bash
# Add gradle wrapper
./gradlew wrapper --gradle-version=latest
git add gradle/wrapper/
git commit -m "Add gradle wrapper"
git push
```

### Build Fails: "SDK not found"

The workflow automatically downloads the SDK. If it fails:
1. Check build logs for specific error
2. May need to update `build.gradle` SDK versions
3. Retry the workflow

### Signing Fails: "Invalid keystore"

**Solution:**
1. Regenerate keystore
2. Re-encode to Base64
3. Update GitHub secrets
4. Retry workflow

### APK Too Large

**Solution:**
Enable ProGuard in `app/build.gradle`:
```gradle
buildTypes {
    release {
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

---

## 🔧 Customizing Workflows

### Change Build Triggers

Edit `.github/workflows/build.yml`:

```yaml
on:
  push:
    branches: [ main, develop, feature/* ]  # Add branches
  pull_request:
    branches: [ main ]
  schedule:
    - cron: '0 0 * * 0'  # Weekly builds
```

### Add Custom Build Steps

```yaml
- name: Custom step
  run: |
    echo "Running custom commands"
    ./gradlew customTask
```

### Add Slack Notifications

```yaml
- name: Notify Slack
  if: failure()
  uses: slackapi/slack-github-action@v1
  with:
    webhook-url: ${{ secrets.SLACK_WEBHOOK }}
    payload: |
      {
        "text": "Build failed for WinMobile"
      }
```

---

## 📈 Advanced Features

### Automatic Versioning

Add to workflow:

```yaml
- name: Get version
  id: version
  run: |
    VERSION=$(grep versionName app/build.gradle | grep -oE '[0-9]+\.[0-9]+\.[0-9]+')
    echo "version=$VERSION" >> $GITHUB_OUTPUT

- name: Create release with version
  uses: softprops/action-gh-release@v1
  with:
    name: "WinMobile ${{ steps.version.outputs.version }}"
```

### Automatic Changelog

```yaml
- name: Generate changelog
  uses: conventional-changelog/action@v1
  with:
    github-token: ${{ secrets.GITHUB_TOKEN }}
```

### Deploy to Play Store

```yaml
- name: Deploy to Play Store
  uses: r0adkll/upload-google-play@v1
  with:
    serviceAccountJsonPlainText: ${{ secrets.PLAY_STORE_KEY }}
    packageName: com.winmobile
    releaseFiles: 'app/build/outputs/apk/release/*.apk'
    track: internal
```

---

## 📚 Resources

### GitHub Actions Documentation
- https://docs.github.com/en/actions
- https://github.com/actions

### Android Build Actions
- https://github.com/android-actions
- https://github.com/r0adkll/sign-android-release

### Gradle Documentation
- https://gradle.org/
- https://docs.gradle.org/

---

## ✅ Setup Checklist

- [ ] Project pushed to GitHub
- [ ] Workflows visible in Actions tab
- [ ] Debug APK builds successfully
- [ ] Unit tests pass
- [ ] Lint checks pass
- [ ] Signing key created
- [ ] GitHub secrets configured
- [ ] Release tag created
- [ ] Signed APK released
- [ ] Build badge added to README

---

## 🎉 You're All Set!

Your project now has:
- ✅ Automated builds on every push
- ✅ Automated testing
- ✅ Automated signing and releasing
- ✅ Security scanning
- ✅ Code quality checks

Every time you push code or create a tag, GitHub Actions will automatically:
1. Build your APK
2. Run tests
3. Check code quality
4. Sign and release (for tags)

**Happy building! 🚀**
