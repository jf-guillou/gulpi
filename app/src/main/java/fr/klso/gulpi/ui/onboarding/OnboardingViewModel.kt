package fr.klso.gulpi.ui.onboarding

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.klso.gulpi.services.Glpi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "OnboardingViewModel"

class OnboardingViewModel() : ViewModel() {
    private val _state = MutableStateFlow(OnboardingUiState())
    val state: StateFlow<OnboardingUiState> = _state.asStateFlow()

    var url by mutableStateOf("")

    init {
        reset()
    }

    private fun reset() {
        _state.value = OnboardingUiState()
    }

    private fun isValidUrl(url: String): Boolean {
        val uri = Uri.parse(url)
        return url.isNotEmpty() && uri != null && uri.scheme?.startsWith("http") == true
    }

    fun checkAndPingUrl() {
        _state.value = OnboardingUiState(isLoading = true)

        if (!isValidUrl(url)) {
            _state.update { s -> s.copy(isMalformedUrl = true, isLoading = false) }
        } else {
            Log.d(TAG, "Init GlpiApi : $url")
            Glpi.init(url)
            viewModelScope.launch {
                val isReachable = Glpi.checkEndpoint()
                _state.update { s -> s.copy(isEndpointInvalid = !isReachable, isLoading = false) }
                if (isReachable) {
                    // TODO: store URL in DataStore
                }
            }
        }
    }
}