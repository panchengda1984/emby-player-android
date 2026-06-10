package com.emby.player.ui.screen.player

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emby.player.data.model.PlaybackInfo
import com.emby.player.data.repository.EmbyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: EmbyRepository,
    private val preferencesManager: com.emby.player.data.local.PreferencesManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String = savedStateHandle.get<String>("itemId") ?: ""

    private val _uiState = MutableStateFlow<PlayerUiState>(PlayerUiState.Loading)
    val uiState: StateFlow<PlayerUiState> = _uiState

    private var userId: String = ""
    private var token: String = ""

    init {
        loadPlaybackInfo()
    }

    private fun loadPlaybackInfo() {
        viewModelScope.launch {
            _uiState.value = PlayerUiState.Loading

            userId = preferencesManager.userId.first() ?: ""
            token = preferencesManager.token.first() ?: ""

            if (userId.isEmpty() || token.isEmpty()) {
                _uiState.value = PlayerUiState.Error("未登录")
                return@launch
            }

            repository.getPlaybackInfo(itemId, userId, token)
                .onSuccess { playbackInfo ->
                    var videoUrl = playbackInfo.MediaSources.firstOrNull()?.DirectStreamUrl 
                        ?: playbackInfo.MediaSources.firstOrNull()?.Path ?: ""
                    
                    if (com.emby.player.util.StrmHelper.isStrmFile(videoUrl)) {
                        videoUrl = com.emby.player.util.StrmHelper.resolveStrmUrl(videoUrl)
                    }
                    
                    _uiState.value = PlayerUiState.Success(playbackInfo, videoUrl)
                }
                .onFailure {
                    _uiState.value = PlayerUiState.Error("获取播放信息失败")
                }
        }
    }

    fun reportPlaybackStart(positionTicks: Long) {
        viewModelScope.launch {
            repository.reportPlaybackStart(itemId, positionTicks, token)
        }
    }

    fun reportPlaybackProgress(positionTicks: Long, isPaused: Boolean) {
        viewModelScope.launch {
            repository.reportPlaybackProgress(itemId, positionTicks, isPaused, token)
        }
    }

    fun reportPlaybackStopped(positionTicks: Long) {
        viewModelScope.launch {
            repository.reportPlaybackStopped(itemId, positionTicks, token)
        }
    }
}

sealed class PlayerUiState {
    object Loading : PlayerUiState()
    data class Success(val playbackInfo: PlaybackInfo, val videoUrl: String) : PlayerUiState()
    data class Error(val message: String) : PlayerUiState()
}
