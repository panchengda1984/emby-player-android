package com.emby.player.player

import android.content.Context
import android.view.Surface
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class IJKPlayerWrapper(context: Context) : VideoPlayer {
    private val player = IjkMediaPlayer()

    init {
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1)
        player.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 30000000)
    }

    override fun prepare(url: String) {
        player.dataSource = url
        player.prepareAsync()
    }

    override fun play() {
        player.start()
    }

    override fun pause() {
        player.pause()
    }

    override fun release() {
        player.reset()
        player.release()
    }

    override fun seekTo(positionMs: Long) {
        player.seekTo(positionMs)
    }

    override fun getCurrentPosition(): Long = player.currentPosition

    override fun getDuration(): Long = player.duration

    override fun isPlaying(): Boolean = player.isPlaying

    fun setSurface(surface: Surface?) {
        player.setSurface(surface)
    }
}
