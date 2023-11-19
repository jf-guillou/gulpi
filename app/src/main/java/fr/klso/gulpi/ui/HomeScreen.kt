package fr.klso.gulpi.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.klso.gulpi.navigation.Scan
import fr.klso.gulpi.navigation.Search

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Button(onClick = {
            navController.navigate(Scan.route) {
                launchSingleTop = true
            }
        }) {
            Text("Scan")
        }
        Button(onClick = {
            navController.navigate(Search.route) {
                launchSingleTop = true
            }
        }) {
            Text("Search")
        }
    }
}

@Preview
@Composable
fun HomeScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Button(onClick = {}) {
            Text("Scan")
        }
        Button(onClick = { }) {
            Text("Search")
        }
    }
}