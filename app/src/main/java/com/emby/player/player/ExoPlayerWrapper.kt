package com.emby.player.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class ExoPlayerWrapper(private val context: Context) : IPlayer {
    
    private var exoPlayer: ExoPlayer? = null

    override fun prepare(url: String) {
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(url)
            setMediaItem(mediaItem)
            prepare()
        }
    }

    override fun start() {
        exoPlayer?.play()
    }

    override fun pause() {
        exoPlayer?.pause()
    }

    override fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun seekTo(position: Long) {
        exoPlayer?.seekTo(position)
    }

    override fun getCurrentPosition(): Long {
        return exoPlayer?.currentPosition ?: 0L
    }

    override fun getDuration(): Long {
        return exoPlayer?.duration ?: 0L
    }

    override fun isPlaying(): Boolean {
        return exoPlayer?.isPlaying ?: false
    }

    fun getPlayer(): ExoPlayer? = exoPlayer
}
