package com.emby.player.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "emby_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val SERVER_URL = stringPreferencesKey("server_url")
        private val USER_ID = stringPreferencesKey("user_id")
        private val TOKEN = stringPreferencesKey("token")
        private val USERNAME = stringPreferencesKey("username")
        private val PLAYER_TYPE = stringPreferencesKey("player_type")
    }

    val serverUrl: Flow<String?> = dataStore.data.map { it[SERVER_URL] }
    val userId: Flow<String?> = dataStore.data.map { it[USER_ID] }
    val token: Flow<String?> = dataStore.data.map { it[TOKEN] }
    val username: Flow<String?> = dataStore.data.map { it[USERNAME] }
    val playerType: Flow<String?> = dataStore.data.map { it[PLAYER_TYPE] ?: "EXOPLAYER" }

    suspend fun saveLoginInfo(serverUrl: String, userId: String, token: String, username: String) {
        dataStore.edit { prefs ->
            prefs[SERVER_URL] = serverUrl
            prefs[USER_ID] = userId
            prefs[TOKEN] = token
            prefs[USERNAME] = username
        }
    }

    suspend fun savePlayerType(type: String) {
        dataStore.edit { prefs ->
            prefs[PLAYER_TYPE] = type
        }
    }

    suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
