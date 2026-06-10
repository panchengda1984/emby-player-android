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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
                title = { Text(item.Name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←")
                    }
                }
            )
        }
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
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 标题和信息
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = item.Name,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    item.ProductionYear?.let {
                        Text("$it", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    item.CommunityRating?.let {
                        Text("⭐ $it", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 播放按钮
                Button(
                    onClick = { onPlayClick(item.Id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("播放")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 类型
                item.Genres?.let { genres ->
                    if (genres.isNotEmpty()) {
                        Text("类型", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(genres.joinToString(", "), style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                // 简介
                item.Overview?.let { overview ->
                    Text("简介", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(overview, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
