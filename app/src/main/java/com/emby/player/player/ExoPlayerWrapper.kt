package com.emby.player.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class ExoPlayerWrapper(private val context: Context) : VideoPlayer {
    
    private var player: ExoPlayer? = null

    override fun prepare(url: String) {
        player = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
        }
    }

    override fun start() {
        player?.play()
    }

    override fun pause() {
        player?.pause()
    }

    override fun release() {
        player?.release()
        player = null
    }

    override fun seekTo(position: Long) {
        player?.seekTo(position)
    }

    override fun getCurrentPosition(): Long {
        return player?.currentPosition ?: 0
    }

    override fun getDuration(): Long {
        return player?.duration ?: 0
    }

    override fun isPlaying(): Boolean {
        return player?.isPlaying ?: false
    }

    fun getPlayer(): ExoPlayer? = player
}
