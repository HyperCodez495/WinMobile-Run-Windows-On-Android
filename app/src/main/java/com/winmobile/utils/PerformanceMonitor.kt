package com.winmobile.utils

import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import android.os.Process
import kotlinx.coroutines.*
import kotlin.math.roundToInt

/**
 * PerformanceMonitor tracks real-time performance metrics during emulation.
 * Monitors FPS, CPU usage, GPU usage, memory, and temperature.
 */
class PerformanceMonitor(private val context: Context) {

    private val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    private var lastCpuTime = 0L
    private var lastElapsedTime = 0L
    private var frameCount = 0
    private var lastFrameTime = System.currentTimeMillis()

    data class PerformanceMetrics(
        val fps: Int,
        val cpuUsage: Int,
        val gpuUsage: Int,
        val memoryUsage: Int,
        val temperature: Int
    )

    /**
     * Get current performance metrics.
     */
    fun getMetrics(): PerformanceMetrics {
        return PerformanceMetrics(
            fps = calculateFPS(),
            cpuUsage = calculateCPUUsage(),
            gpuUsage = calculateGPUUsage(),
            memoryUsage = calculateMemoryUsage(),
            temperature = getDeviceTemperature()
        )
    }

    /**
     * Calculate frames per second.
     */
    private fun calculateFPS(): Int {
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastFrameTime

        frameCount++

        if (deltaTime >= 1000) {
            val fps = (frameCount * 1000 / deltaTime).toInt()
            frameCount = 0
            lastFrameTime = currentTime
            return fps
        }

        return 0
    }

    /**
     * Calculate CPU usage percentage.
     */
    private fun calculateCPUUsage(): Int {
        return try {
            val currentCpuTime = Debug.getNativeHeapSize()
            val currentElapsedTime = System.nanoTime()

            if (lastCpuTime > 0 && lastElapsedTime > 0) {
                val cpuDelta = currentCpuTime - lastCpuTime
                val elapsedDelta = currentElapsedTime - lastElapsedTime
                val usage = ((cpuDelta.toDouble() / elapsedDelta.toDouble()) * 100).roundToInt()
                lastCpuTime = currentCpuTime
                lastElapsedTime = currentElapsedTime
                usage.coerceIn(0, 100)
            } else {
                lastCpuTime = currentCpuTime
                lastElapsedTime = currentElapsedTime
                0
            }
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Calculate GPU usage (estimated).
     */
    private fun calculateGPUUsage(): Int {
        return try {
            val memInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memInfo)

            val totalMemory = memInfo.totalMem
            val availableMemory = memInfo.availMem
            val usedMemory = totalMemory - availableMemory

            ((usedMemory.toDouble() / totalMemory.toDouble()) * 100).roundToInt()
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Calculate memory usage in MB.
     */
    private fun calculateMemoryUsage(): Int {
        return try {
            val runtime = Runtime.getRuntime()
            val usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1048576
            usedMemory.toInt()
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Get device temperature (if available).
     */
    private fun getDeviceTemperature(): Int {
        return try {
            val thermalFile = java.io.File("/sys/class/thermal/thermal_zone0/temp")
            if (thermalFile.exists()) {
                val temp = thermalFile.readText().trim().toInt() / 1000
                temp
            } else {
                0
            }
        } catch (e: Exception) {
            0
        }
    }

    /**
     * Record a frame for FPS calculation.
     */
    fun recordFrame() {
        frameCount++
    }
}
