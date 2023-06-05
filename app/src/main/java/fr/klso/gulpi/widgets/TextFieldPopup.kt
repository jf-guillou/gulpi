package fr.klso.gulpi.widgets

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TextFieldPopup(
    header: String = "header",
    hint: String = "textfield hint",
    onCancel: () -> Unit = {},
    onOk: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf(hint)
    }

    AlertDialog(
        onDismissRequest = { },
        title = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                label = { Text(text = header) },
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onOk(text) }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text("Cancel")
            }
        }
    )
}