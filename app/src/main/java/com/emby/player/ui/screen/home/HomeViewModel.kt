package com.emby.player.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emby.player.data.model.MediaItem
import com.emby.player.data.repository.EmbyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EmbyRepository,
    private val preferencesManager: com.emby.player.data.local.PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

    var serverUrl: String = ""
        private set

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading

            val userId = preferencesManager.userId.first()
            val token = preferencesManager.token.first()
            serverUrl = preferencesManager.serverUrl.first() ?: ""

            if (userId == null || token == null) {
                _uiState.value = HomeUiState.Error("未登录")
                return@launch
            }

            val resumeResult = repository.getResumeItems(userId, token)
            val latestResult = repository.getLatestItems(userId, token)
            val librariesResult = repository.getLibraries(userId, token)

            if (resumeResult.isSuccess && latestResult.isSuccess && librariesResult.isSuccess) {
                _uiState.value = HomeUiState.Success(
                    resumeItems = resumeResult.getOrNull() ?: emptyList(),
                    latestItems = latestResult.getOrNull() ?: emptyList(),
                    libraries = librariesResult.getOrNull() ?: emptyList()
                )
            } else {
                _uiState.value = HomeUiState.Error("加载失败")
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val resumeItems: List<MediaItem>,
        val latestItems: List<MediaItem>,
        val libraries: List<MediaItem>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
