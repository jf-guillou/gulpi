package fr.klso.gulpi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun SearchFormScreen(navController: NavController) {
    Column() {
        Row() {
            Text("SearchRow")
        }
    }
}

@Preview
@Composable
fun SearchFormScreen() {
    Column() {
        Row() {
            Text("SearchRow")
        }
    }
}