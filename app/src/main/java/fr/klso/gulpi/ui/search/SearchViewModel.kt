package fr.klso.gulpi.ui.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.klso.gulpi.models.search.SearchCriteria
import fr.klso.gulpi.models.search.SearchLink
import fr.klso.gulpi.services.Glpi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {
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
        val c = listOf(
            SearchCriteria(
                field = 2,
                searchtype = "contains",
                value = textCriteria,
                link = SearchLink.OR,
            ),
            SearchCriteria(
                field = 5,
                searchtype = "contains",
                value = textCriteria,
                link = SearchLink.OR,
            ),
            SearchCriteria(
                field = 6,
                searchtype = "contains",
                value = textCriteria,
                link = SearchLink.OR,
            )
        )

        viewModelScope.launch {
            val computers = Glpi.searchComputers(c)
            Log.d(TAG, "Found $computers")
            _state.value = _state.value.copy(items = computers)
        }
    }
}