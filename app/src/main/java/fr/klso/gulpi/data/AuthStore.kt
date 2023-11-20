package fr.klso.gulpi.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG = "AuthStore"

class AuthStore @Inject constructor(@ApplicationContext private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("auth")
        private val READY_KEY = stringPreferencesKey("ready")
        private val URL_KEY = stringPreferencesKey("url")
        private val APP_TOKEN_KEY = stringPreferencesKey("app_token")
        private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
        private val SESSION_TOKEN_KEY = stringPreferencesKey("session_token")
    }

    val dataStore: Flow<Preferences> = context.dataStore.data

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
    val ready: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[READY_KEY] != null
    }

    suspend fun saveUrl(url: String) {
        Log.d(TAG, "saveUrl : $url")
        context.dataStore.edit { preferences ->
            preferences[URL_KEY] = url
        }
    }

    suspend fun saveAppAndUserToken(appToken: String, userToken: String) {
        context.dataStore.edit { preferences ->
            preferences[APP_TOKEN_KEY] = appToken
            preferences[USER_TOKEN_KEY] = userToken
        }
    }

    suspend fun saveSessionToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[SESSION_TOKEN_KEY] = token
        }
    }

    suspend fun setReady() {
        context.dataStore.edit { preferences ->
            preferences[READY_KEY] = ""
        }
    }
}