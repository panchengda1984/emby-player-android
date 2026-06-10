package com.emby.player.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class ExoPlayerWrapper(context: Context) : VideoPlayer {
    private val player = ExoPlayer.Builder(context).build()

    override fun prepare(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
    }

    override fun play() {
        player.play()
    }

    override fun pause() {
        player.pause()
    }

    override fun release() {
        player.release()
    }

    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    override fun getCurrentPosition(): Long = player.currentPosition

    override fun getDuration(): Long = player.duration

    override fun isPlaying(): Boolean = player.isPlaying

    fun getPlayer(): Player = player
}
