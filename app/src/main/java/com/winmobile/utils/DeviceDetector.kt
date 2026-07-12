package com.winmobile.utils

import android.content.Context
import android.os.Build
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * DeviceDetector analyzes device hardware and recommends optimal settings.
 * Detects GPU, CPU, RAM, and other hardware characteristics.
 */
class DeviceDetector(private val context: Context) {

    /**
     * Get device hardware information.
     */
    fun getDeviceInfo(): DeviceInfo {
        return DeviceInfo(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            androidVersion = Build.VERSION.SDK_INT,
            cpuAbi = Build.SUPPORTED_ABIS.firstOrNull() ?: "unknown",
            ramGB = getTotalRAM(),
            gpuName = detectGPU(),
            gpuVendor = detectGPUVendor(),
            supportedGLVersion = detectOpenGLVersion(),
            hasVulkan = hasVulkanSupport()
        )
    }

    /**
     * Detect GPU vendor and model.
     */
    private fun detectGPU(): String {
        return try {
            val glRenderer = GLES20.glGetString(GLES20.GL_RENDERER)
            glRenderer ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    /**
     * Detect GPU vendor.
     */
    private fun detectGPUVendor(): GPUVendor {
        val gpu = detectGPU().lowercase()
        return when {
            gpu.contains("adreno") -> GPUVendor.QUALCOMM_ADRENO
            gpu.contains("mali") -> GPUVendor.ARM_MALI
            gpu.contains("powervr") -> GPUVendor.POWERVR
            gpu.contains("tegra") -> GPUVendor.NVIDIA_TEGRA
            gpu.contains("apple") -> GPUVendor.APPLE
            else -> GPUVendor.UNKNOWN
        }
    }

    /**
     * Detect OpenGL version.
     */
    private fun detectOpenGLVersion(): String {
        return try {
            val version = GLES20.glGetString(GLES20.GL_VERSION)
            version ?: "2.0"
        } catch (e: Exception) {
            "2.0"
        }
    }

    /**
     * Check if device supports Vulkan.
     */
    private fun hasVulkanSupport(): Boolean {
        return try {
            context.packageManager.hasSystemFeature("android.hardware.vulkan.level")
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Get total RAM in GB.
     */
    private fun getTotalRAM(): Int {
        return try {
            val runtime = Runtime.getRuntime()
            val totalMemory = runtime.totalMemory() / (1024 * 1024 * 1024)
            totalMemory.toInt()
        } catch (e: Exception) {
            4  // Default to 4GB
        }
    }

    /**
     * Recommend performance preset based on device.
     */
    fun recommendPreset(): PerformanceRecommendation {
        val info = getDeviceInfo()
        val score = calculatePerformanceScore(info)

        return when {
            score >= 80 -> PerformanceRecommendation(
                preset = "Performance",
                resolution = "1280x720",
                fpsCap = 60,
                reason = "High-end device detected. Maximum performance recommended."
            )
            score >= 50 -> PerformanceRecommendation(
                preset = "Balanced",
                resolution = "1024x768",
                fpsCap = 30,
                reason = "Mid-range device detected. Balanced settings recommended."
            )
            else -> PerformanceRecommendation(
                preset = "Compatibility",
                resolution = "800x600",
                fpsCap = 20,
                reason = "Budget device detected. Compatibility mode recommended."
            )
        }
    }

    /**
     * Calculate device performance score (0-100).
     */
    private fun calculatePerformanceScore(info: DeviceInfo): Int {
        var score = 0

        // RAM score (max 30 points)
        score += when {
            info.ramGB >= 12 -> 30
            info.ramGB >= 8 -> 25
            info.ramGB >= 6 -> 20
            info.ramGB >= 4 -> 15
            else -> 5
        }

        // GPU score (max 40 points)
        score += when (info.gpuVendor) {
            GPUVendor.QUALCOMM_ADRENO -> {
                when {
                    info.gpuName.contains("740") || info.gpuName.contains("730") -> 40
                    info.gpuName.contains("660") || info.gpuName.contains("650") -> 35
                    info.gpuName.contains("640") || info.gpuName.contains("630") -> 25
                    else -> 15
                }
            }
            GPUVendor.ARM_MALI -> 25
            GPUVendor.POWERVR -> 20
            GPUVendor.NVIDIA_TEGRA -> 20
            GPUVendor.APPLE -> 40
            else -> 10
        }

        // Android version score (max 20 points)
        score += when {
            info.androidVersion >= 13 -> 20
            info.androidVersion >= 12 -> 18
            info.androidVersion >= 11 -> 15
            info.androidVersion >= 10 -> 12
            else -> 5
        }

        // Vulkan support bonus (max 10 points)
        if (info.hasVulkan) score += 10

        return score.coerceIn(0, 100)
    }

    /**
     * Recommend graphics driver.
     */
    fun recommendGraphicsDriver(): String {
        val info = getDeviceInfo()
        return when (info.gpuVendor) {
            GPUVendor.QUALCOMM_ADRENO -> "Turnip"  // Best for Snapdragon
            else -> "VirGL"  // Fallback
        }
    }

    /**
     * Check device compatibility.
     */
    fun checkCompatibility(): CompatibilityReport {
        val info = getDeviceInfo()
        val issues = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // Check architecture
        if (!info.cpuAbi.contains("arm64")) {
            issues.add("Device is not ARM64. WinMobile requires ARM64 architecture.")
        }

        // Check RAM
        if (info.ramGB < 4) {
            issues.add("Device has less than 4GB RAM. Minimum 4GB required.")
        } else if (info.ramGB < 6) {
            warnings.add("Device has ${info.ramGB}GB RAM. 6GB+ recommended for smooth operation.")
        }

        // Check Android version
        if (info.androidVersion < 28) {
            issues.add("Android version is too old. Minimum Android 8.0 (API 28) required.")
        }

        // Check GPU
        if (info.gpuVendor == GPUVendor.UNKNOWN) {
            warnings.add("GPU vendor could not be detected. Performance may vary.")
        }

        return CompatibilityReport(
            isCompatible = issues.isEmpty(),
            issues = issues,
            warnings = warnings,
            deviceInfo = info
        )
    }
}

enum class GPUVendor {
    QUALCOMM_ADRENO,
    ARM_MALI,
    POWERVR,
    NVIDIA_TEGRA,
    APPLE,
    UNKNOWN
}

data class DeviceInfo(
    val manufacturer: String,
    val model: String,
    val androidVersion: Int,
    val cpuAbi: String,
    val ramGB: Int,
    val gpuName: String,
    val gpuVendor: GPUVendor,
    val supportedGLVersion: String,
    val hasVulkan: Boolean
)

data class PerformanceRecommendation(
    val preset: String,
    val resolution: String,
    val fpsCap: Int,
    val reason: String
)

data class CompatibilityReport(
    val isCompatible: Boolean,
    val issues: List<String>,
    val warnings: List<String>,
    val deviceInfo: DeviceInfo
)
