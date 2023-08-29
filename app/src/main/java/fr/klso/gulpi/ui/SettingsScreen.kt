package fr.klso.gulpi.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.alorma.compose.settings.ui.SettingsGroup

@Composable
fun SettingsScreen() {
    SettingsGroup(title = { Text("Server") }) {
    }
}
