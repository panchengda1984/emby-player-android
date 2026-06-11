package com.emby.player.ui.screen.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.emby.player.data.model.MediaItem

@Composable
fun DetailScreen(
    onPlayClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is DetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DetailUiState.Success -> {
            DetailContent(
                item = state.item,
                serverUrl = viewModel.serverUrl,
                onPlayClick = onPlayClick,
                onBackClick = onBackClick
            )
        }
        is DetailUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailContent(
    item: MediaItem,
    serverUrl: String,
    onPlayClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item.Name, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←", style = MaterialTheme.typography.headlineMedium)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // 背景图
            item.BackdropImageTags?.firstOrNull()?.let { tag ->
                AsyncImage(
                    model = "$serverUrl/Items/${item.Id}/Images/Backdrop?tag=$tag",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 标题和信息
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = item.Name,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    item.ProductionYear?.let {
                        Text("$it", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    item.CommunityRating?.let {
                        Text("⭐ %.1f".format(it), style = MaterialTheme.typography.bodyMedium, color = Color(0xFFFFD700))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 播放按钮
                Button(
                    onClick = { onPlayClick(item.Id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("▶ 播放", style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 类型
                item.Genres?.let { genres ->
                    if (genres.isNotEmpty()) {
                        Text("类型", style = MaterialTheme.typography.titleMedium, color = Color.White)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(genres.joinToString(" · "), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                // 简介
                item.Overview?.let { overview ->
                    Text("简介", style = MaterialTheme.typography.titleMedium, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(overview, style = MaterialTheme.typography.bodyMedium, color = Color.Gray, lineHeight = 24.sp)
                }
            }
        }
    }
}
