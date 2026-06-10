package com.emby.player.data.api

import com.emby.player.data.model.*
import retrofit2.http.*

interface EmbyApiService {
    
    @POST("Users/AuthenticateByName")
    suspend fun authenticate(
        @Body credentials: Map<String, String>,
        @Header("X-Emby-Authorization") auth: String
    ): AuthResponse

    @GET("Users/{userId}/Items")
    suspend fun getItems(
        @Path("userId") userId: String,
        @Query("ParentId") parentId: String? = null,
        @Query("IncludeItemTypes") includeItemTypes: String? = null,
        @Query("Recursive") recursive: Boolean = true,
        @Query("SortBy") sortBy: String = "SortName",
        @Query("SortOrder") sortOrder: String = "Ascending",
        @Query("Fields") fields: String = "Overview,Genres",
        @Query("StartIndex") startIndex: Int = 0,
        @Query("Limit") limit: Int = 100,
        @Header("X-Emby-Token") token: String
    ): ItemsResponse

    @GET("Users/{userId}/Items/Resume")
    suspend fun getResumeItems(
        @Path("userId") userId: String,
        @Query("Limit") limit: Int = 20,
        @Header("X-Emby-Token") token: String
    ): ItemsResponse

    @GET("Users/{userId}/Items/Latest")
    suspend fun getLatestItems(
        @Path("userId") userId: String,
        @Query("Limit") limit: Int = 20,
        @Query("Fields") fields: String = "Overview,Genres",
        @Header("X-Emby-Token") token: String
    ): List<MediaItem>

    @GET("Items/{itemId}/PlaybackInfo")
    suspend fun getPlaybackInfo(
        @Path("itemId") itemId: String,
        @Query("UserId") userId: String,
        @Header("X-Emby-Token") token: String
    ): PlaybackInfo

    @POST("Sessions/Playing")
    suspend fun reportPlaybackStart(
        @Body body: Map<String, Any>,
        @Header("X-Emby-Token") token: String
    )

    @POST("Sessions/Playing/Progress")
    suspend fun reportPlaybackProgress(
        @Body body: Map<String, Any>,
        @Header("X-Emby-Token") token: String
    )

    @POST("Sessions/Playing/Stopped")
    suspend fun reportPlaybackStopped(
        @Body body: Map<String, Any>,
        @Header("X-Emby-Token") token: String
    )
}
