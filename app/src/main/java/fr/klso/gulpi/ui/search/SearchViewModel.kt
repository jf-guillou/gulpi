package fr.klso.gulpi.ui.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.klso.gulpi.models.search.SearchCriteria
import fr.klso.gulpi.services.Glpi
import fr.klso.gulpi.utilities.exceptions.ApiSessionTokenInvalidException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(
//    private val store: AuthStore,
) : ViewModel() {
    private val _state = MutableStateFlow(SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    var textCriteria by mutableStateOf("")

    init {
        reset()
    }

    private fun reset() {
        _state.value = SearchUiState()
    }

    fun searchFromScan(criteria: String) {
        if (textCriteria == "") {
            textCriteria = criteria
            search()
        }
    }

    fun search() {
        Log.d(TAG, "Search for $textCriteria")
        viewModelScope.launch {
            try {
                Glpi.searchComputers(listOf(SearchCriteria(value = textCriteria)))
            } catch (e: ApiSessionTokenInvalidException) {
//                store.saveSessionToken("")
            }
        }
    }
}