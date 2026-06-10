package com.emby.player.player

import android.content.Context
import android.view.Surface
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class IJKPlayerWrapper(private val context: Context) : IPlayer {
    
    private var ijkPlayer: IjkMediaPlayer? = null

    init {
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
    }

    override fun prepare(url: String) {
        ijkPlayer = IjkMediaPlayer().apply {
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "dns_cache_clear", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1)
            dataSource = url
            prepareAsync()
        }
    }

    override fun start() {
        ijkPlayer?.start()
    }

    override fun pause() {
        ijkPlayer?.pause()
    }

    override fun release() {
        ijkPlayer?.release()
        ijkPlayer = null
    }

    override fun seekTo(position: Long) {
        ijkPlayer?.seekTo(position)
    }

    override fun getCurrentPosition(): Long {
        return ijkPlayer?.currentPosition ?: 0L
    }

    override fun getDuration(): Long {
        return ijkPlayer?.duration ?: 0L
    }

    override fun isPlaying(): Boolean {
        return ijkPlayer?.isPlaying ?: false
    }

    fun setSurface(surface: Surface?) {
        ijkPlayer?.setSurface(surface)
    }
}
