package fr.klso.gulpi.widgets

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fr.klso.gulpi.R

@Preview
@Composable
fun TextFieldPopup(
    header: String = "TextPopupHeader",
    hint: String = "",
    value: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onCancel: () -> Unit = {},
    onOk: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf(value)
    }
    val focusRequester = remember { FocusRequester() }

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(hint) },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                label = { Text(text = header) },
                modifier = Modifier.focusRequester(focusRequester),
                keyboardOptions = keyboardOptions
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onOk(text) }
            ) {
                Text(stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}