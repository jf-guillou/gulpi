package fr.klso.gulpi

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.klso.gulpi.data.AuthStore
import fr.klso.gulpi.navigation.Home
import fr.klso.gulpi.navigation.Scan
import fr.klso.gulpi.navigation.SearchForm
import fr.klso.gulpi.navigation.SearchResults
import fr.klso.gulpi.navigation.Settings
import fr.klso.gulpi.screens.HomeScreen
import fr.klso.gulpi.screens.ScanScreen
import fr.klso.gulpi.screens.SearchFormScreen
import fr.klso.gulpi.screens.SearchResultsScreen
import fr.klso.gulpi.screens.SettingsScreen
import fr.klso.gulpi.services.Glpi
import fr.klso.gulpi.ui.theme.GulpiTheme
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { App() }
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            }
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val store = AuthStore(LocalContext.current)
    val url = store.getUrl.collectAsState("").value
    val uri = Uri.parse(url)
    if (url.isNotEmpty() && uri != null && uri.scheme?.startsWith("http") == true) {
        Log.d(TAG, "Init GlpiApi : $url")
        Glpi.init(url)
    }

    val appToken = store.getAppToken.collectAsState("").value
    Log.d(TAG, "App token : $appToken")
    Glpi.appToken = appToken

    val sessionToken = store.getSessionToken.collectAsState("").value
    Log.d(TAG, "Session token : $sessionToken")
    Glpi.sessionToken = sessionToken

    if (Glpi.sessionToken.isEmpty() && Glpi.appToken.isNotEmpty()) {
        Log.d(TAG, "Missing session token")
        val userToken = store.getUserToken.collectAsState("").value
        Log.d(TAG, "User token : $userToken")
        if (userToken.isNotEmpty()) {
            LaunchedEffect(Unit) {
                val token = Glpi.initSession(userToken).sessionToken
                store.saveSessionToken(token)
                Glpi.sessionToken = token
                Log.d(TAG, token)
            }
        }
    }

    val destinations = listOf(Home, SearchForm, Scan, Settings)
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen =
        destinations.find { it.route == currentDestination?.route } ?: Home

    GulpiTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet() {
                    Text(stringResource(R.string.app_name), modifier = Modifier.padding(16.dp))
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
                                    navController.navigate(item.route) {
                                        launchSingleTop = true
                                    }
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
                        title = { Text(if (currentScreen == Home) stringResource(R.string.app_name) else currentScreen.name) },
                        navigationIcon = {
                            if (navController.previousBackStackEntry == null) {
                                IconButton(onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }) {
                                    Icon(Icons.Filled.Menu, contentDescription = null)
                                }
                            } else {
                                IconButton(onClick = {
                                    navController.popBackStack()
                                }) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = null)
                                }

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
                        HomeScreen(navController)
                    }
                    composable(route = SearchForm.route) {
                        SearchFormScreen(navController)
                    }
                    composable(
                        route = SearchResults.routeArgs, arguments = SearchResults.arguments
                    ) { navBackStackEntry ->
                        val criteria =
                            navBackStackEntry.arguments?.getString(SearchResults.criteria)

                        SearchResultsScreen(navController, criteria)
                    }
                    composable(route = Scan.route) {
                        ScanScreen(navController)
                    }
                    composable(route = Settings.route) {
                        SettingsScreen()
                    }
                }
            }
        }
    }
}