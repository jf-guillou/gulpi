package fr.klso.gulpi

import android.Manifest
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.klso.gulpi.navigation.Credentials
import fr.klso.gulpi.navigation.Home
import fr.klso.gulpi.navigation.Onboarding
import fr.klso.gulpi.navigation.Scan
import fr.klso.gulpi.navigation.Search
import fr.klso.gulpi.navigation.SearchFromScan
import fr.klso.gulpi.navigation.Settings
import fr.klso.gulpi.services.Glpi
import fr.klso.gulpi.ui.HomeScreen
import fr.klso.gulpi.ui.ScanScreen
import fr.klso.gulpi.ui.SettingsScreen
import fr.klso.gulpi.ui.credentials.CredentialsScreen
import fr.klso.gulpi.ui.onboarding.OnboardingScreen
import fr.klso.gulpi.ui.search.SearchScreen
import fr.klso.gulpi.ui.theme.GulpiTheme
import fr.klso.gulpi.utilities.exceptions.ApiSessionTokenInvalidException
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MainActivityScreen() }
        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            }
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(viewModel: MainActivityViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    if (!state.isReady) {
        // LoadingScreen here ?
        return
    }

    Log.d(TAG, "Init GlpiApi, url : ${state.url}")
    if (state.url.isEmpty()) {
        OnboardingScreen()
        return
    }
    Glpi.init(state.url)

    Log.d(TAG, "User token : ${state.userToken}")
    Log.d(TAG, "App token : ${state.appToken}")
    Log.d(TAG, "Session token : ${state.sessionToken}")

    if (state.userToken.isEmpty() || state.appToken.isEmpty()) {
        CredentialsScreen()
        return
    }

    LaunchedEffect(Unit) {
        try {
            Glpi.checkSession()
        } catch (e: ApiSessionTokenInvalidException) {
            Log.d(TAG, "Session token is invalid")
            viewModel.saveSessionToken("")
        }
    }

    if (state.sessionToken.isEmpty()) {
        Log.d(TAG, "Missing session token")
        LaunchedEffect(Unit) {
            Log.d(TAG, "initSession")
            val token = Glpi.initSession(state.userToken).sessionToken
            viewModel.saveSessionToken(token)
            Glpi.sessionToken = token
            Log.d(TAG, token)
        }
    }

    Glpi.appToken = state.appToken
    Glpi.sessionToken = state.sessionToken

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val destinations = listOf(Home, Scan, Search, Settings)
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen =
        destinations.find { it.route == currentDestination?.route } ?: Home

    GulpiTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = Glpi.usable,
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
                        title = {
                            if (currentScreen == Home) {
                                Text(stringResource(R.string.app_name))
                            } else {
                                Text(currentScreen.name)
                            }
                        },
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
                    composable(route = Search.route) {
                        SearchScreen(navController, criteria = null)
                    }
                    composable(
                        route = SearchFromScan.routeArgs, arguments = SearchFromScan.arguments
                    ) { navBackStackEntry ->
                        val criteria =
                            navBackStackEntry.arguments?.getString(SearchFromScan.criteria)

                        SearchScreen(navController, criteria = criteria)
                    }
                    composable(route = Scan.route) {
                        ScanScreen(navController)
                    }

                    composable(route = Settings.route) {
                        SettingsScreen()
                    }
                    composable(route = Credentials.route) {
                        CredentialsScreen()
                    }
                    composable(route = Onboarding.route) {
                        OnboardingScreen()
                    }
                }
            }
        }
    }
}