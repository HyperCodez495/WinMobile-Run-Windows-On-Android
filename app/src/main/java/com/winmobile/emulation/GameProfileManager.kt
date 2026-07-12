package com.winmobile.emulation

import java.io.Serializable

/**
 * GameProfileManager handles creation and management of game-specific profiles.
 * Allows users to save custom settings for each game.
 */
class GameProfileManager {

    private val profiles = mutableMapOf<String, GameProfile>()

    /**
     * Create or update a game profile.
     */
    fun saveProfile(profile: GameProfile): Result {
        return try {
            profiles[profile.gameId] = profile
            Result.Success("Profile saved for '${profile.gameName}'")
        } catch (e: Exception) {
            Result.Error("Failed to save profile: ${e.message}")
        }
    }

    /**
     * Get a game profile by game ID.
     */
    fun getProfile(gameId: String): GameProfile? {
        return profiles[gameId]
    }

    /**
     * Get all profiles.
     */
    fun getAllProfiles(): List<GameProfile> {
        return profiles.values.toList()
    }

    /**
     * Delete a game profile.
     */
    fun deleteProfile(gameId: String): Result {
        return try {
            profiles.remove(gameId)
            Result.Success("Profile deleted")
        } catch (e: Exception) {
            Result.Error("Failed to delete profile: ${e.message}")
        }
    }

    sealed class Result {
        data class Success(val message: String) : Result()
        data class Error(val message: String) : Result()
    }
}

/**
 * Data class representing a game profile with custom settings.
 */
data class GameProfile(
    val gameId: String,
    val gameName: String,
    val exePath: String,
    val resolution: String = "1024x768",
    val fpsCap: Int = 30,
    val graphicsDriver: String = "Turnip",
    val performancePreset: PerformancePreset = PerformancePreset.BALANCED,
    val enableVSync: Boolean = true,
    val enableShaderCache: Boolean = true,
    val customEnvVars: Map<String, String> = emptyMap(),
    val createdAt: Long = System.currentTimeMillis(),
    val lastPlayed: Long = 0
) : Serializable {

    fun getDisplayName(): String {
        return gameName.substringBeforeLast(".")
    }

    fun getResolutionWidth(): Int {
        return resolution.split("x")[0].toIntOrNull() ?: 1024
    }

    fun getResolutionHeight(): Int {
        return resolution.split("x").getOrNull(1)?.toIntOrNull() ?: 768
    }
}
