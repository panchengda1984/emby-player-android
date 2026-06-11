package com.emby.player.ui.screen.library

import androidx.lifecycle.SavedStateHandle
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
class LibraryViewModel @Inject constructor(
    private val repository: EmbyRepository,
    private val preferencesManager: com.emby.player.data.local.PreferencesManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val libraryId: String = savedStateHandle.get<String>("libraryId") ?: ""

    private val _uiState = MutableStateFlow<LibraryUiState>(LibraryUiState.Loading)
    val uiState: StateFlow<LibraryUiState> = _uiState

    var serverUrl: String = ""
        private set

    init {
        loadLibraryItems()
    }

    private fun loadLibraryItems() {
        viewModelScope.launch {
            _uiState.value = LibraryUiState.Loading

            val userId = preferencesManager.userId.first()
            val token = preferencesManager.token.first()
            serverUrl = preferencesManager.serverUrl.first() ?: ""

            if (userId == null || token == null) {
                _uiState.value = LibraryUiState.Error("未登录")
                return@launch
            }

            repository.getItems(userId, token, parentId = libraryId, itemTypes = null)
                .onSuccess { response ->
                    _uiState.value = LibraryUiState.Success(response.Items)
                }
                .onFailure {
                    _uiState.value = LibraryUiState.Error("加载失败: ${it.message}")
                }
        }
    }
}

sealed class LibraryUiState {
    object Loading : LibraryUiState()
    data class Success(val items: List<MediaItem>) : LibraryUiState()
    data class Error(val message: String) : LibraryUiState()
}
