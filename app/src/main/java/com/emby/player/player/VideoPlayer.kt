package com.emby.player.player

enum class PlayerType {
    EXO_PLAYER,
    IJK_PLAYER,
    FFMPEG_PLAYER
}

interface VideoPlayer {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun release()
    fun seekTo(positionMs: Long)
    fun getCurrentPosition(): Long
    fun getDuration(): Long
    fun isPlaying(): Boolean
}
