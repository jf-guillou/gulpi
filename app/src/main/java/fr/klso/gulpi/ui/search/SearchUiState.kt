package fr.klso.gulpi.ui.search

import fr.klso.gulpi.models.search.PaginableSearchComputers

data class SearchUiState(
    val isLoading: Boolean = false,
    val items: PaginableSearchComputers = PaginableSearchComputers()
)