package com.emby.player.player

enum class PlayerType {
    EXO_PLAYER,
    IJK_PLAYER,
    SYSTEM_PLAYER
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
