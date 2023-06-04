package fr.klso.gulpi.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import fr.klso.gulpi.navigation.SearchResults

@Composable
fun HomeScreen(navController: NavController) {
    Text(text = "HomeScreen")
    navController.navigate("${SearchResults.route}/a") {
        launchSingleTop = true
    }
}