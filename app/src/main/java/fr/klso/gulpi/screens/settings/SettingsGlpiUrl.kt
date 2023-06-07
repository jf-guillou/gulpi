package fr.klso.gulpi.screens.settings

import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import com.alorma.compose.settings.ui.SettingsMenuLink
import fr.klso.gulpi.R
import fr.klso.gulpi.data.AuthStore
import fr.klso.gulpi.widgets.TextFieldPopup
import kotlinx.coroutines.launch

@Composable
fun SettingsGlpiUrl() {
    var showTextFieldPopup by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val store = AuthStore(LocalContext.current)
    val url = store.getUrl.collectAsState(initial = "").value
    SettingsMenuLink(
        icon = {
            Icon(
                painter = painterResource(R.drawable.baseline_public_24),
                contentDescription = "URL"
            )
        },
        title = { Text(stringResource(R.string.glpi_url)) },
        subtitle = { Text(url.ifEmpty { stringResource(R.string.not_set) }) },
        onClick = { showTextFieldPopup = true },
    )

    if (!showTextFieldPopup) {
        return
    }

    TextFieldPopup(
        header = stringResource(R.string.glpi_url),
        value = url,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
        onCancel = { showTextFieldPopup = false },
        onOk = { _url ->
            scope.launch { store.saveUrl(_url) }
            showTextFieldPopup = false
        })
}
