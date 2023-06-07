package fr.klso.gulpi.screens.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.alorma.compose.settings.ui.SettingsMenuLink
import fr.klso.gulpi.R
import fr.klso.gulpi.data.AuthStore
import fr.klso.gulpi.widgets.TextFieldPopup
import kotlinx.coroutines.launch

@Composable
fun SettingsGlpiAppToken() {
    var showTextFieldPopup by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val store = AuthStore(LocalContext.current)
    val appToken = store.getAppToken.collectAsState(initial = "").value
    SettingsMenuLink(
        icon = {
            Icon(Icons.Default.Lock,
                contentDescription = "Token"
            )
        },
        title = { Text(stringResource(R.string.app_token)) },
        subtitle = { Text(appToken.ifEmpty { stringResource(R.string.not_set) }) },
        onClick = { showTextFieldPopup = true },
    )

    if (!showTextFieldPopup) {
        return
    }

    TextFieldPopup(
        header = stringResource(R.string.app_token),
        value = appToken,
        onCancel = { showTextFieldPopup = false },
        onOk = { token ->
            scope.launch { store.saveAppToken(token) }
            showTextFieldPopup = false
        })
}
