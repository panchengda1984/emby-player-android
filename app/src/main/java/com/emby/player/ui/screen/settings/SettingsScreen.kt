package com.emby.player.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val playerType by viewModel.playerType.collectAsState()
    var showPlayerDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
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
                .padding(16.dp)
        ) {
            Text("播放器", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPlayerDialog = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("解码器")
                    Text(
                        text = when (playerType) {
                            com.emby.player.player.PlayerType.EXOPLAYER -> "ExoPlayer"
                            com.emby.player.player.PlayerType.IJKPLAYER -> "IJKPlayer"
                            com.emby.player.player.PlayerType.SYSTEM -> "系统播放器"
                        },
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (showPlayerDialog) {
            AlertDialog(
                onDismissRequest = { showPlayerDialog = false },
                title = { Text("选择解码器") },
                text = {
                    Column {
                        listOf(
                            com.emby.player.player.PlayerType.EXOPLAYER to "ExoPlayer (推荐)",
                            com.emby.player.player.PlayerType.IJKPLAYER to "IJKPlayer",
                            com.emby.player.player.PlayerType.SYSTEM to "系统播放器"
                        ).forEach { (type, name) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.setPlayerType(type)
                                        showPlayerDialog = false
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = playerType == type,
                                    onClick = {
                                        viewModel.setPlayerType(type)
                                        showPlayerDialog = false
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(name)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showPlayerDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}
