package com.emby.player.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emby.player.data.repository.EmbyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: EmbyRepository,
    private val preferencesManager: com.emby.player.data.local.PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(serverUrl: String, username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            
            repository.authenticate(serverUrl, username, password)
                .onSuccess { response ->
                    preferencesManager.saveLoginInfo(
                        serverUrl = serverUrl,
                        userId = response.User.Id,
                        token = response.AccessToken,
                        username = username
                    )
                    _uiState.value = LoginUiState.Success(
                        userId = response.User.Id,
                        token = response.AccessToken,
                        serverUrl = serverUrl
                    )
                }
                .onFailure { error ->
                    _uiState.value = LoginUiState.Error(error.message ?: "登录失败")
                }
        }
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val userId: String, val token: String, val serverUrl: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
