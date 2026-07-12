package com.winmobile.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * FileManager handles file operations for the emulation system.
 * Manages storage, file browsing, and installation of Windows applications.
 */
class FileManager(private val context: Context) {

    private val externalStorageDir = context.getExternalFilesDir(null)
    private val cacheDir = context.cacheDir
    private val filesDir = context.filesDir

    /**
     * Get available storage space in bytes.
     */
    fun getAvailableStorage(): Long {
        return try {
            val stat = android.os.StatFs(externalStorageDir?.absolutePath ?: "/")
            stat.availableBlocksLong * stat.blockSizeLong
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Get total storage space in bytes.
     */
    fun getTotalStorage(): Long {
        return try {
            val stat = android.os.StatFs(externalStorageDir?.absolutePath ?: "/")
            stat.blockCountLong * stat.blockSizeLong
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * Check if there's enough space for an operation.
     */
    fun hasEnoughSpace(requiredBytes: Long): Boolean {
        return getAvailableStorage() > requiredBytes
    }

    /**
     * Get formatted storage info.
     */
    fun getStorageInfo(): StorageInfo {
        val total = getTotalStorage()
        val available = getAvailableStorage()
        val used = total - available

        return StorageInfo(
            totalBytes = total,
            usedBytes = used,
            availableBytes = available,
            usagePercentage = if (total > 0) (used * 100) / total else 0
        )
    }

    /**
     * List files in a directory.
     */
    suspend fun listFiles(path: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                val dir = File(path)
                if (!dir.exists() || !dir.isDirectory) {
                    return@withContext Result.Error("Directory not found")
                }

                val files = dir.listFiles()?.map { file ->
                    FileInfo(
                        name = file.name,
                        path = file.absolutePath,
                        isDirectory = file.isDirectory,
                        size = file.length(),
                        lastModified = file.lastModified()
                    )
                } ?: emptyList()

                Result.Success(files)
            } catch (e: Exception) {
                Result.Error("Failed to list files: ${e.message}")
            }
        }
    }

    /**
     * Copy a file.
     */
    suspend fun copyFile(sourcePath: String, destinationPath: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                val source = File(sourcePath)
                val destination = File(destinationPath)

                if (!source.exists()) {
                    return@withContext Result.Error("Source file not found")
                }

                source.copyTo(destination, overwrite = true)
                Result.Success("File copied successfully")
            } catch (e: Exception) {
                Result.Error("Failed to copy file: ${e.message}")
            }
        }
    }

    /**
     * Delete a file.
     */
    suspend fun deleteFile(path: String): Result {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(path)
                if (!file.exists()) {
                    return@withContext Result.Error("File not found")
                }

                if (file.isDirectory) {
                    file.deleteRecursively()
                } else {
                    file.delete()
                }

                Result.Success("File deleted successfully")
            } catch (e: Exception) {
                Result.Error("Failed to delete file: ${e.message}")
            }
        }
    }

    /**
     * Get file size in human-readable format.
     */
    fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
            else -> String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
        }
    }

    /**
     * Get file extension.
     */
    fun getFileExtension(fileName: String): String {
        return fileName.substringAfterLast(".", "")
    }

    /**
     * Check if file is executable.
     */
    fun isExecutable(path: String): Boolean {
        val file = File(path)
        return file.exists() && (
            file.extension == "exe" ||
            file.extension == "bat" ||
            file.extension == "com" ||
            file.extension == "msi"
        )
    }

    sealed class Result {
        data class Success(val data: Any? = null) : Result()
        data class Error(val message: String) : Result()
    }
}

data class FileInfo(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long,
    val lastModified: Long
) {
    fun getFormattedSize(): String {
        return when {
            size < 1024 -> "$size B"
            size < 1024 * 1024 -> "${size / 1024} KB"
            size < 1024 * 1024 * 1024 -> "${size / (1024 * 1024)} MB"
            else -> String.format("%.2f GB", size / (1024.0 * 1024.0 * 1024.0))
        }
    }
}

data class StorageInfo(
    val totalBytes: Long,
    val usedBytes: Long,
    val availableBytes: Long,
    val usagePercentage: Long
) {
    fun getFormattedTotal(): String = formatBytes(totalBytes)
    fun getFormattedUsed(): String = formatBytes(usedBytes)
    fun getFormattedAvailable(): String = formatBytes(availableBytes)

    private fun formatBytes(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
            else -> String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0))
        }
    }
}
