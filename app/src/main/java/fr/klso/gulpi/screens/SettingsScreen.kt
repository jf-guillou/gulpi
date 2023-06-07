package fr.klso.gulpi.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.alorma.compose.settings.ui.SettingsGroup
import fr.klso.gulpi.screens.settings.SettingsGlpiAppToken
import fr.klso.gulpi.screens.settings.SettingsGlpiUrl
import fr.klso.gulpi.screens.settings.SettingsGlpiUserToken

@Composable
fun SettingsScreen() {
    SettingsGroup(title = { Text("Server") }) {
        SettingsGlpiUrl()
        SettingsGlpiAppToken()
        SettingsGlpiUserToken()
    }
}
