package com.emby.player.player

enum class PlayerType {
    EXOPLAYER,
    IJKPLAYER,
    SYSTEM
}

interface VideoPlayer {
    fun prepare(url: String)
    fun play()
    fun pause()
    fun seekTo(position: Long)
    fun release()
    fun getCurrentPosition(): Long
    fun getDuration(): Long
    fun isPlaying(): Boolean
}
