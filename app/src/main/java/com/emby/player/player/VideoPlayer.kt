package com.emby.player.player

enum class PlayerType {
    EXO_PLAYER,
    IJK_PLAYER,
    SYSTEM_PLAYER
}

interface VideoPlayer {
    fun prepare(url: String)
    fun start()
    fun pause()
    fun stop()
    fun seekTo(position: Long)
    fun release()
    fun getCurrentPosition(): Long
    fun getDuration(): Long
    fun isPlaying(): Boolean
}
