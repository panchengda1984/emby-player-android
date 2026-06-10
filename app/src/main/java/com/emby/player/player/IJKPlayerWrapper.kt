package com.emby.player.player

import android.content.Context
import android.view.Surface
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class IJKPlayerWrapper(private val context: Context) : VideoPlayer {
    private var player: IjkMediaPlayer? = null

    init {
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
    }

    override fun prepare(url: String) {
        player = IjkMediaPlayer().apply {
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
            setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
            dataSource = url
            prepareAsync()
        }
    }

    override fun start() {
        player?.start()
    }

    override fun pause() {
        player?.pause()
    }

    override fun stop() {
        player?.stop()
    }

    override fun seekTo(position: Long) {
        player?.seekTo(position)
    }

    override fun release() {
        player?.release()
        player = null
    }

    override fun getCurrentPosition(): Long = player?.currentPosition ?: 0L

    override fun getDuration(): Long = player?.duration ?: 0L

    override fun isPlaying(): Boolean = player?.isPlaying ?: false

    fun setSurface(surface: Surface?) {
        player?.setSurface(surface)
    }

    fun getPlayer(): IjkMediaPlayer? = player
}
