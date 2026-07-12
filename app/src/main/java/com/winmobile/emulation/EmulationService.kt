package com.winmobile.emulation

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.*

/**
 * EmulationService manages the lifecycle of Windows emulation in the background.
 * Runs as a foreground service to prevent system from killing the emulation process.
 */
class EmulationService : Service() {

    private val binder = EmulationBinder()
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var emulationEngine: EmulationEngine? = null
    private var currentProcess: Process? = null

    override fun onCreate() {
        super.onCreate()
        emulationEngine = EmulationEngine(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY  // Restart if killed by system
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        currentProcess?.destroy()
    }

    inner class EmulationBinder : Binder() {
        fun getService(): EmulationService = this@EmulationService
    }

    /**
     * Initialize emulation engine asynchronously.
     */
    fun initializeEmulation(callback: (Boolean, String) -> Unit) {
        scope.launch {
            val result = emulationEngine?.initialize()
            when (result) {
                is EmulationEngine.Result.Success -> callback(true, result.message)
                is EmulationEngine.Result.Error -> callback(false, result.message)
                null -> callback(false, "Engine not initialized")
            }
        }
    }

    /**
     * Launch a Windows application.
     */
    fun launchApplication(
        exePath: String,
        preset: PerformancePreset = PerformancePreset.BALANCED,
        callback: (Boolean, String) -> Unit
    ) {
        scope.launch {
            val result = emulationEngine?.launchApplication(exePath, preset)
            when (result) {
                is EmulationEngine.Result.Success -> callback(true, result.message)
                is EmulationEngine.Result.Error -> callback(false, result.message)
                null -> callback(false, "Engine not initialized")
            }
        }
    }

    /**
     * Stop the running application.
     */
    fun stopApplication(callback: (Boolean, String) -> Unit) {
        scope.launch {
            val result = emulationEngine?.stopApplication()
            when (result) {
                is EmulationEngine.Result.Success -> callback(true, result.message)
                is EmulationEngine.Result.Error -> callback(false, result.message)
                null -> callback(false, "Engine not initialized")
            }
        }
    }

    /**
     * Get system information.
     */
    fun getSystemInfo(): SystemInfo? {
        return emulationEngine?.getSystemInfo()
    }
}
