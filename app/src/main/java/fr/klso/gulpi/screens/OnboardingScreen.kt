package fr.klso.gulpi.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.klso.gulpi.R
import fr.klso.gulpi.services.Glpi

private const val TAG = "OnboardingScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun OnboardingScreen() {
    var url by remember {
        mutableStateOf("")
    }

    Column(
        Modifier
            .padding(horizontal = 30.dp, vertical = 50.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        Image(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.mipmap.ic_launcher),
            contentDescription = "Logo"
        )

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = typography.headlineLarge,
            text = "Gulpi"
        )

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Gulpi"
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            value = "",
            onValueChange = { url = it },
            label = { Text("Server Endpoint URL") }
        )

        Button(onClick = {
            validateServerUrl(url)
        }, Modifier.fillMaxWidth()) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            Text("Next")
        }
    }
}

fun validateServerUrl(url: String) {
    val uri = Uri.parse(url)
    if (url.isNotEmpty() && uri != null && uri.scheme?.startsWith("http") == true) {
        // TODO: ping api
        Log.d(TAG, "Init GlpiApi : $url")
        Glpi.init(url)
//        val res = Glpi.initSession("")
    }
}