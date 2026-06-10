package com.emby.player.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "emby_settings")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val SERVER_URL = stringPreferencesKey("server_url")
    private val USER_ID = stringPreferencesKey("user_id")
    private val TOKEN = stringPreferencesKey("token")
    private val USERNAME = stringPreferencesKey("username")

    val serverUrl: Flow<String?> = context.dataStore.data.map { it[SERVER_URL] }
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID] }
    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN] }
    val username: Flow<String?> = context.dataStore.data.map { it[USERNAME] }

    suspend fun saveLoginInfo(serverUrl: String, userId: String, token: String, username: String) {
        context.dataStore.edit { prefs ->
            prefs[SERVER_URL] = serverUrl
            prefs[USER_ID] = userId
            prefs[TOKEN] = token
            prefs[USERNAME] = username
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
