package fr.klso.gulpi.ui.credentials

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.klso.gulpi.data.AuthStore
import fr.klso.gulpi.services.Glpi
import fr.klso.gulpi.utilities.exceptions.ApiAuthFailedException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "CredentialsViewModel"

@HiltViewModel
class CredentialsViewModel @Inject constructor(
    private val store: AuthStore,
) : ViewModel() {
    private val _state = MutableStateFlow(CredentialsUiState())
    val state: StateFlow<CredentialsUiState> = _state.asStateFlow()

    var appToken by mutableStateOf("")
    var userToken by mutableStateOf("")

    init {
        reset()
    }

    private fun reset() {
        _state.value = CredentialsUiState()
    }

    private fun isValidAppToken(token: String): Boolean {
        return token.isNotEmpty() && token.length <= 32
    }

    private fun isValidUserToken(token: String): Boolean {
        return token.isNotEmpty() && token.length <= 32
    }

    fun validateTokens() {
        _state.value = CredentialsUiState(isLoading = true)

        if (!isValidAppToken(appToken) || !isValidUserToken(userToken)) {
            _state.update { s -> s.copy(isBadCredentials = true, isLoading = false) }
        } else {
            viewModelScope.launch {
                Glpi.appToken = appToken
                try {
                    Glpi.initSession(userToken)
                    store.saveAppAndUserToken(appToken, userToken)
                    _state.update { s -> s.copy(isBadCredentials = false, isLoading = false) }
                } catch (e: ApiAuthFailedException) {
                    _state.update { s -> s.copy(isBadCredentials = true, isLoading = false) }
                }
            }
        }
    }

    fun goBack() {
        viewModelScope.launch {
            store.saveUrl("")
        }
    }
}