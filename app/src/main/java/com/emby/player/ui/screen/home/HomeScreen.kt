package com.emby.player.ui.screen.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.emby.player.data.model.MediaItem

@Composable
fun HomeScreen(
    onItemClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is HomeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is HomeUiState.Success -> {
            HomeContent(
                resumeItems = state.resumeItems,
                latestItems = state.latestItems,
                libraries = state.libraries,
                onItemClick = onItemClick
            )
        }
        is HomeUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun HomeContent(
    resumeItems: List<MediaItem>,
    latestItems: List<MediaItem>,
    libraries: List<MediaItem>,
    onItemClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        if (resumeItems.isNotEmpty()) {
            item {
                SectionTitle("继续观看")
                MediaRow(items = resumeItems, onItemClick = onItemClick)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (latestItems.isNotEmpty()) {
            item {
                SectionTitle("最新添加")
                MediaRow(items = latestItems, onItemClick = onItemClick)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        if (libraries.isNotEmpty()) {
            item {
                SectionTitle("媒体库")
            }
            items(libraries) { library ->
                LibraryItem(library = library, onClick = { onItemClick(library.Id) })
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun MediaRow(
    items: List<MediaItem>,
    onItemClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            MediaCard(item = item, onClick = { onItemClick(item.Id) })
        }
    }
}

@Composable
private fun MediaCard(item: MediaItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = item.ImageTags?.get("Primary")?.let { tag ->
                    // 需要从 ViewModel 传入 serverUrl
                    "placeholder_url"
                },
                contentDescription = item.Name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.Name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp),
                maxLines = 2
            )
        }
    }
}

@Composable
private fun LibraryItem(library: MediaItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = library.Name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
