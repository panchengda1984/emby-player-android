package com.emby.player.data.model

data class EmbyServer(
    val id: String,
    val name: String,
    val address: String,
    val userId: String? = null,
    val accessToken: String? = null
)

data class AuthResponse(
    val User: User,
    val AccessToken: String,
    val ServerId: String
)

data class User(
    val Id: String,
    val Name: String,
    val HasPassword: Boolean,
    val HasConfiguredPassword: Boolean,
    val HasConfiguredEasyPassword: Boolean,
    val EnableAutoLogin: Boolean?
)

data class MediaItem(
    val Id: String,
    val Name: String,
    val Type: String,
    val UserData: UserData?,
    val ImageTags: Map<String, String>? = null,
    val BackdropImageTags: List<String>? = null,
    val ProductionYear: Int? = null,
    val Overview: String? = null,
    val CommunityRating: Float? = null,
    val RunTimeTicks: Long? = null,
    val Genres: List<String>? = null,
    val SeriesName: String? = null,
    val SeasonName: String? = null,
    val IndexNumber: Int? = null,
    val ParentIndexNumber: Int? = null
)

data class UserData(
    val PlaybackPositionTicks: Long,
    val PlayCount: Int,
    val IsFavorite: Boolean,
    val Played: Boolean
)

data class ItemsResponse(
    val Items: List<MediaItem>,
    val TotalRecordCount: Int
)

data class PlaybackInfo(
    val MediaSources: List<MediaSource>
)

data class MediaSource(
    val Id: String,
    val Protocol: String,
    val Path: String,
    val Type: String,
    val Container: String,
    val DirectStreamUrl: String?,
    val MediaStreams: List<MediaStream>
)

data class MediaStream(
    val Index: Int,
    val Type: String,
    val Codec: String?,
    val Language: String?,
    val DisplayTitle: String?,
    val IsDefault: Boolean,
    val IsForced: Boolean,
    val DeliveryUrl: String?
)
