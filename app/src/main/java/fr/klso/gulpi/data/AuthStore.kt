package fr.klso.gulpi.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("auth")
        private val URL_KEY = stringPreferencesKey("url")
        private val APP_TOKEN_KEY = stringPreferencesKey("app_token")
        private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
        private val SESSION_TOKEN_KEY = stringPreferencesKey("session_token")
    }

    val getUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[URL_KEY] ?: ""
    }
    val getAppToken: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[APP_TOKEN_KEY] ?: ""
    }
    val getUserToken: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[USER_TOKEN_KEY] ?: ""
    }
    val getSessionToken: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[SESSION_TOKEN_KEY] ?: ""
    }

    suspend fun saveUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[URL_KEY] = url
        }
    }

    suspend fun saveAppToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[APP_TOKEN_KEY] = token
        }
    }

    suspend fun saveUserToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = token
        }
    }

    suspend fun saveSessionToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[SESSION_TOKEN_KEY] = token
        }
    }
}