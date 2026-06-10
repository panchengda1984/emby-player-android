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
    private val serverUrlKey = stringPreferencesKey("server_url")
    private val userIdKey = stringPreferencesKey("user_id")
    private val tokenKey = stringPreferencesKey("token")
    private val usernameKey = stringPreferencesKey("username")

    val serverUrl: Flow<String?> = context.dataStore.data.map { it[serverUrlKey] }
    val userId: Flow<String?> = context.dataStore.data.map { it[userIdKey] }
    val token: Flow<String?> = context.dataStore.data.map { it[tokenKey] }
    val username: Flow<String?> = context.dataStore.data.map { it[usernameKey] }

    suspend fun saveLoginInfo(serverUrl: String, userId: String, token: String, username: String) {
        context.dataStore.edit { prefs ->
            prefs[serverUrlKey] = serverUrl
            prefs[userIdKey] = userId
            prefs[tokenKey] = token
            prefs[usernameKey] = username
        }
    }

    suspend fun clearLoginInfo() {
        context.dataStore.edit { it.clear() }
    }
}
