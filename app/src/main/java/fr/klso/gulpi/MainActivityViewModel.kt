package fr.klso.gulpi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.klso.gulpi.data.AuthStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "MainActivityViewModel"

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val store: AuthStore,
) : ViewModel() {
    private val _state = MutableStateFlow(MainActivityUiState())
    val state: StateFlow<MainActivityUiState> = _state.asStateFlow()

    init {
        reset()
    }

    private fun reset() {
        store.getUrl.onEach { url ->
            _state.value = state.value.copy(url = url, isReady = true)
        }.launchIn(viewModelScope)

        store.getUserToken.onEach { userToken ->
            _state.value = state.value.copy(userToken = userToken)
        }.launchIn(viewModelScope)

        store.getAppToken.onEach { appToken ->
            _state.value = state.value.copy(appToken = appToken)
        }.launchIn(viewModelScope)

        store.getSessionToken.onEach { sessionToken ->
            _state.value = state.value.copy(sessionToken = sessionToken)
        }.launchIn(viewModelScope)
    }

    suspend fun saveSessionToken(token: String) {
        store.saveSessionToken(token)
    }
}

data class MainActivityUiState(
    val isReady: Boolean = false,
    val url: String = "",
    val userToken: String = "",
    val appToken: String = "",
    val sessionToken: String = "",
)