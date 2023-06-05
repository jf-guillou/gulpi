package fr.klso.gulpi.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.alorma.compose.settings.ui.SettingsMenuLink
import fr.klso.gulpi.widgets.TextFieldPopup

@Composable
fun SettingsScreen() {
    var showTextFieldPopup by remember {
        mutableStateOf(false)
    }

    SettingsMenuLink(
        icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "Wifi") },
        title = { Text(text = "Hello") },
        subtitle = { Text(text = "This is a longer text") },
        onClick = { showTextFieldPopup = true },
    )

    if (!showTextFieldPopup) {
        return
    }

    TextFieldPopup(header = "GLPI url", onCancel = { showTextFieldPopup = false })
}
