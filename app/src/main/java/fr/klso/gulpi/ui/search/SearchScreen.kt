package fr.klso.gulpi.ui.search

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SearchScreen(
    navController: NavController,
    criteria: String?,
    viewModel: SearchViewModel = viewModel()
) {
    Text(text = "SearchScreen")
    Text(criteria ?: "")
}