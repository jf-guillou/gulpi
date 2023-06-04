package fr.klso.gulpi

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.klso.gulpi.navigation.Destination
import fr.klso.gulpi.navigation.Home
import fr.klso.gulpi.navigation.Scan
import fr.klso.gulpi.navigation.Search
import fr.klso.gulpi.navigation.Settings
import fr.klso.gulpi.screens.HomeScreen
import fr.klso.gulpi.screens.ScanScreen
import fr.klso.gulpi.screens.SearchScreen
import fr.klso.gulpi.screens.SettingsScreen
import fr.klso.gulpi.ui.theme.GulpiTheme
import kotlinx.coroutines.launch

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App(this) }
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
//                this.datastore.edit { settings ->
//                    settings[booleanPreferencesKey("camera")] = isGranted
//                }
            }
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(ctx: Activity) {
    var currentScreen: Destination by remember { mutableStateOf(Home) }
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    GulpiTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                val destinations = listOf(Home, Search, Scan, Settings)
                ModalDrawerSheet() {
                    Text("Gulpi", modifier = Modifier.padding(16.dp))
                    Divider()
                    destinations.forEach { item ->
                        val selected = item.route == currentScreen.route

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    navController.navigate(item.route)
                                    currentScreen = item
                                },
                            colors = if (selected) CardDefaults.cardColors() else CardDefaults.elevatedCardColors(),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = "${item.name} Icon"
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(start = 24.dp),
                                    text = item.name,
                                )
                            }
                        }
                    }
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("AppBaer") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(Icons.Filled.Menu, contentDescription = null)
                            }
                        },
                        actions = {}
                    )
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Home.route,
                    modifier = Modifier.padding(padding)
                ) {
                    composable(route = Home.route) {
                        HomeScreen()
                    }
                    composable(route = Search.route) {
                        SearchScreen()
                    }
                    composable(route = Scan.route) {
                        ScanScreen()
                    }
                    composable(route = Settings.route) {
                        SettingsScreen()
                    }
                }
            }
        }
    }
}