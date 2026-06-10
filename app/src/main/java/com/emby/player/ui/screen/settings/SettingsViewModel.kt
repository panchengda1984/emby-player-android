package com.emby.player.ui.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emby.player.data.local.PreferencesManager
import com.emby.player.player.PlayerType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val playerType: StateFlow<PlayerType> = preferencesManager.playerType

    fun setPlayerType(type: PlayerType) {
        viewModelScope.launch {
            preferencesManager.savePlayerType(type)
        }
    }
}
