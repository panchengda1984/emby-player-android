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

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "emby_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKeys {
        val SERVER_URL = stringPreferencesKey("server_url")
        val USER_ID = stringPreferencesKey("user_id")
        val TOKEN = stringPreferencesKey("token")
        val USERNAME = stringPreferencesKey("username")
    }

    val serverUrl: Flow<String?> = context.dataStore.data.map { it[PreferencesKeys.SERVER_URL] }
    val userId: Flow<String?> = context.dataStore.data.map { it[PreferencesKeys.USER_ID] }
    val token: Flow<String?> = context.dataStore.data.map { it[PreferencesKeys.TOKEN] }
    val username: Flow<String?> = context.dataStore.data.map { it[PreferencesKeys.USERNAME] }

    suspend fun saveLoginInfo(serverUrl: String, userId: String, token: String, username: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.SERVER_URL] = serverUrl
            prefs[PreferencesKeys.USER_ID] = userId
            prefs[PreferencesKeys.TOKEN] = token
            prefs[PreferencesKeys.USERNAME] = username
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}
