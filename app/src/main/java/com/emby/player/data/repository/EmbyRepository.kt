package com.emby.player.data.repository

import com.emby.player.data.api.EmbyApiService
import com.emby.player.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmbyRepository @Inject constructor(
    private val apiService: EmbyApiService
) {
    suspend fun authenticate(
        serverUrl: String,
        username: String,
        password: String
    ): Result<AuthResponse> = runCatching {
        val authHeader = buildAuthHeader()
        apiService.authenticate(
            credentials = mapOf(
                "Username" to username,
                "Pw" to password
            ),
            auth = authHeader
        )
    }

    suspend fun getLibraries(userId: String, token: String): Result<List<MediaItem>> = runCatching {
        val response = apiService.getItems(
            userId = userId,
            includeItemTypes = "CollectionFolder",
            token = token
        )
        response.Items
    }

    suspend fun getItems(
        userId: String,
        token: String,
        parentId: String? = null,
        itemTypes: String? = null,
        startIndex: Int = 0,
        limit: Int = 100
    ): Result<ItemsResponse> = runCatching {
        apiService.getItems(
            userId = userId,
            parentId = parentId,
            includeItemTypes = itemTypes,
            startIndex = startIndex,
            limit = limit,
            token = token
        )
    }

    suspend fun getResumeItems(userId: String, token: String): Result<List<MediaItem>> = runCatching {
        apiService.getResumeItems(userId, token = token).Items
    }

    suspend fun getLatestItems(userId: String, token: String): Result<List<MediaItem>> = runCatching {
        apiService.getLatestItems(userId, token = token)
    }

    suspend fun getPlaybackInfo(
        itemId: String,
        userId: String,
        token: String
    ): Result<PlaybackInfo> = runCatching {
        apiService.getPlaybackInfo(itemId, userId, token)
    }

    suspend fun reportPlaybackStart(
        itemId: String,
        positionTicks: Long,
        token: String
    ): Result<Unit> = runCatching {
        apiService.reportPlaybackStart(
            body = mapOf(
                "ItemId" to itemId,
                "PositionTicks" to positionTicks
            ),
            token = token
        )
    }

    suspend fun reportPlaybackProgress(
        itemId: String,
        positionTicks: Long,
        isPaused: Boolean,
        token: String
    ): Result<Unit> = runCatching {
        apiService.reportPlaybackProgress(
            body = mapOf(
                "ItemId" to itemId,
                "PositionTicks" to positionTicks,
                "IsPaused" to isPaused
            ),
            token = token
        )
    }

    suspend fun reportPlaybackStopped(
        itemId: String,
        positionTicks: Long,
        token: String
    ): Result<Unit> = runCatching {
        apiService.reportPlaybackStopped(
            body = mapOf(
                "ItemId" to itemId,
                "PositionTicks" to positionTicks
            ),
            token = token
        )
    }

    private fun buildAuthHeader(): String {
        return "MediaBrowser Client=\"EmbyPlayer\", Device=\"Android\", DeviceId=\"${System.currentTimeMillis()}\", Version=\"1.0.0\""
    }

    fun getImageUrl(serverUrl: String, itemId: String, imageTag: String, type: String = "Primary"): String {
        return "$serverUrl/Items/$itemId/Images/$type?tag=$imageTag"
    }
}
