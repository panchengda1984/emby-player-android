package com.emby.player.player

enum class PlayerType {
    EXOPLAYER,
    SYSTEM
}

interface IPlayer {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun release()
    fun seekTo(position: Long)
    fun getCurrentPosition(): Long
    fun getDuration(): Long
    fun isPlaying(): Boolean
}
