package com.winmobile.emulation

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.Serializable

/**
 * ContainerManager handles creation, deletion, and management of Windows containers.
 * Each container is a separate Wine prefix with its own Windows environment.
 */
class ContainerManager(private val context: Context) {

    private val containersDir = File(context.filesDir, "containers")
    private val containersList = mutableListOf<WindowsContainer>()

    init {
        containersDir.mkdirs()
        loadContainers()
    }

    /**
     * Create a new Windows container.
     */
    suspend fun createContainer(name: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                // Check if container already exists
                if (containersList.any { it.name == name }) {
                    return@withContext Result.Error("Container with this name already exists")
                }

                val containerDir = File(containersDir, name)
                containerDir.mkdirs()

                // Create Wine prefix directory structure
                val cDrive = File(containerDir, "prefix/drive_c")
                cDrive.mkdirs()

                // Create standard Windows directories
                File(cDrive, "Windows").mkdirs()
                File(cDrive, "Program Files").mkdirs()
                File(cDrive, "Program Files (x86)").mkdirs()
                File(cDrive, "Users").mkdirs()
                File(cDrive, "Temp").mkdirs()

                // Create container metadata
                val container = WindowsContainer(
                    id = System.currentTimeMillis().toString(),
                    name = name,
                    path = containerDir.absolutePath,
                    createdAt = System.currentTimeMillis(),
                    status = "Ready",
                    size = 0L
                )

                containersList.add(container)
                saveContainers()

                Result.Success("Container '$name' created successfully")
            } catch (e: Exception) {
                Result.Error("Failed to create container: ${e.message}")
            }
        }
    }

    /**
     * Delete a Windows container.
     */
    suspend fun deleteContainer(containerId: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                val container = containersList.find { it.id == containerId }
                    ?: return@withContext Result.Error("Container not found")

                val containerDir = File(container.path)
                if (containerDir.exists()) {
                    containerDir.deleteRecursively()
                }

                containersList.remove(container)
                saveContainers()

                Result.Success("Container '${container.name}' deleted successfully")
            } catch (e: Exception) {
                Result.Error("Failed to delete container: ${e.message}")
            }
        }
    }

    /**
     * Get all containers.
     */
    fun getContainers(): List<WindowsContainer> {
        return containersList.toList()
    }

    /**
     * Get a specific container by ID.
     */
    fun getContainer(containerId: String): WindowsContainer? {
        return containersList.find { it.id == containerId }
    }

    /**
     * Update container status.
     */
    suspend fun updateContainerStatus(containerId: String, status: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                val container = containersList.find { it.id == containerId }
                    ?: return@withContext Result.Error("Container not found")

                val index = containersList.indexOf(container)
                containersList[index] = container.copy(status = status)
                saveContainers()

                Result.Success("Container status updated to '$status'")
            } catch (e: Exception) {
                Result.Error("Failed to update container: ${e.message}")
            }
        }
    }

    /**
     * Get container size in bytes.
     */
    suspend fun getContainerSize(containerId: String): Long {
        return withContext(Dispatchers.IO) {
            try {
                val container = containersList.find { it.id == containerId }
                    ?: return@withContext 0L

                val containerDir = File(container.path)
                if (containerDir.exists()) {
                    containerDir.walkTopDown().sumOf { it.length() }
                } else {
                    0L
                }
            } catch (e: Exception) {
                0L
            }
        }
    }

    /**
     * Clear container cache.
     */
    suspend fun clearContainerCache(containerId: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                val container = containersList.find { it.id == containerId }
                    ?: return@withContext Result.Error("Container not found")

                val cacheDir = File(container.path, "prefix/drive_c/Temp")
                if (cacheDir.exists()) {
                    cacheDir.deleteRecursively()
                    cacheDir.mkdirs()
                }

                Result.Success("Container cache cleared")
            } catch (e: Exception) {
                Result.Error("Failed to clear cache: ${e.message}")
            }
        }
    }

    /**
     * Load containers from storage.
     */
    private fun loadContainers() {
        try {
            if (containersDir.exists()) {
                containersDir.listFiles()?.forEach { dir ->
                    if (dir.isDirectory) {
                        val container = WindowsContainer(
                            id = dir.name,
                            name = dir.name,
                            path = dir.absolutePath,
                            createdAt = dir.lastModified(),
                            status = "Ready",
                            size = dir.walkTopDown().sumOf { it.length() }
                        )
                        containersList.add(container)
                    }
                }
            }
        } catch (e: Exception) {
            // Log error but continue
        }
    }

    /**
     * Save containers metadata.
     */
    private fun saveContainers() {
        try {
            // In production, save to JSON or database
            // For now, containers are persisted by directory structure
        } catch (e: Exception) {
            // Log error
        }
    }

    sealed class Result {
        data class Success(val message: String) : Result()
        data class Error(val message: String) : Result()
    }
}

/**
 * Data class representing a Windows container.
 */
data class WindowsContainer(
    val id: String,
    val name: String,
    val path: String,
    val createdAt: Long,
    val status: String,
    val size: Long
) : Serializable {
    fun getSizeInGB(): Double = size / (1024.0 * 1024.0 * 1024.0)
    
    fun getFormattedSize(): String {
        return when {
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> "${size / 1024} KB"
            size < 1024 * 1024 * 1024 -> "${size / (1024 * 1024)} MB"
            else -> String.format("%.2f GB", getSizeInGB())
        }
    }
}
