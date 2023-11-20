package fr.klso.gulpi.ui.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

private const val TAG = "SearchScreen"

@Composable
fun SearchScreen(
    navController: NavController,
    criteria: String?,
    viewModel: SearchViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Text(text = "SearchScreen")
    Text(criteria ?: "")
}