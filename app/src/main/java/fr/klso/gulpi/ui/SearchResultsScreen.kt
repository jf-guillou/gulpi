package fr.klso.gulpi.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SearchResultsScreen(navController: NavController, criteria: String?) {
    Text(text = "SearchScreen")
    Text(criteria ?: "")
}