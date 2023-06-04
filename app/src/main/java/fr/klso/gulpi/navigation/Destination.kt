package fr.klso.gulpi.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

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

object Search : Destination {
    override val route = "search"
    override val icon = Icons.Default.Search
    override val name = "Search"
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