package com.emby.player.player

import android.content.Context
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
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32.toLong())
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1)
            setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
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

    fun getPlayer(): IjkMediaPlayer? = player
}
