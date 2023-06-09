package fr.klso.gulpi.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface Destination {
    val route: String
    val icon: ImageVector
    val name: String
}

object Home : Destination {
    override val route = "home"
    override val icon = Icons.Default.Home
    override val name = "Home"
}

object SearchForm : Destination {
    override val route = "searchform"
    override val icon = Icons.Default.Search
    override val name = "Search"
}

object SearchResults : Destination {
    override val route = "searchresults"
    const val criteria = "criteria"
    val routeArgs = "${route}/{${criteria}}"
    val arguments = listOf(
        navArgument(criteria) { type = NavType.StringType }
    )
    override val icon = Icons.Default.Warning
    override val name = "Search results"
}

object Scan : Destination {
    override val route = "scan"
    override val icon = Icons.Default.Create
    override val name = "Scan"
}

object Settings : Destination {
    override val route = "settings"
    override val icon = Icons.Default.Settings
    override val name = "Settings"
}

object Inventory : Destination {
    override val route = "inventory"
    override val icon = Icons.Default.Build
    override val name = "Inventory"
}