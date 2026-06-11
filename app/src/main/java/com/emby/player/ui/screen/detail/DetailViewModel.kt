package com.emby.player.ui.screen.detail

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
class DetailViewModel @Inject constructor(
    private val repository: EmbyRepository,
    private val preferencesManager: com.emby.player.data.local.PreferencesManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String = savedStateHandle.get<String>("itemId") ?: ""

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState

    var serverUrl: String = ""
        private set

    init {
        loadDetail()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = DetailUiState.Loading

            val userId = preferencesManager.userId.first()
            val token = preferencesManager.token.first()
            serverUrl = preferencesManager.serverUrl.first() ?: ""

            if (userId == null || token == null) {
                _uiState.value = DetailUiState.Error("未登录")
                return@launch
            }

            // 直接根据 itemId 获取单个媒体项
            repository.getItems(userId, token, parentId = itemId, itemTypes = null)
                .onSuccess { response ->
                    if (response.Items.isNotEmpty()) {
                        // 如果 itemId 是媒体库，返回其子项列表
                        _uiState.value = DetailUiState.Success(response.Items.first())
                    } else {
                        // 尝试获取该媒体项本身
                        repository.getItems(userId, token, parentId = null, itemTypes = null)
                            .onSuccess { allItems ->
                                val item = allItems.Items.find { it.Id == itemId }
                                if (item != null) {
                                    _uiState.value = DetailUiState.Success(item)
                                } else {
                                    _uiState.value = DetailUiState.Error("未找到媒体")
                                }
                            }
                            .onFailure {
                                _uiState.value = DetailUiState.Error("加载失败")
                            }
                    }
                }
                .onFailure {
                    _uiState.value = DetailUiState.Error("加载失败")
                }
        }
    }
}

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val item: MediaItem) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}
