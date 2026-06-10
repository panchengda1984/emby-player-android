package com.emby.player.ui.screen.player

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun PlayerScreen(
    onBackClick: () -> Unit,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    when (val state = uiState) {
        is PlayerUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is PlayerUiState.Success -> {
            val playbackInfo = state.playbackInfo
            val mediaSource = playbackInfo.MediaSources.firstOrNull()

            if (mediaSource != null) {
                val videoUrl = mediaSource.DirectStreamUrl ?: mediaSource.Path

                DisposableEffect(Unit) {
                    val exoPlayer = ExoPlayer.Builder(context).build()
                    val mediaItem = MediaItem.fromUri(videoUrl)
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true

                    exoPlayer.addListener(object : Player.Listener {
                        override fun onPlaybackStateChanged(state: Int) {
                            if (state == Player.STATE_READY) {
                                viewModel.reportPlaybackStart(exoPlayer.currentPosition * 10000)
                            }
                        }
                    })

                    onDispose {
                        viewModel.reportPlaybackStopped(exoPlayer.currentPosition * 10000)
                        exoPlayer.release()
                    }
                }

                VideoPlayer(
                    videoUrl = videoUrl,
                    onBackClick = onBackClick
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("无可用播放源", color = MaterialTheme.colorScheme.error)
                }
            }
        }
        is PlayerUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBackClick) {
                        Text("返回")
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoPlayer(
    videoUrl: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val player = ExoPlayer.Builder(ctx).build()
                    exoPlayer = player
                    this.player = player

                    val mediaItem = MediaItem.fromUri(videoUrl)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    player.playWhenReady = true
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 返回按钮
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text("←", color = Color.White, style = MaterialTheme.typography.headlineMedium)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer?.release()
        }
    }
}
