package fr.klso.gulpi.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.klso.gulpi.R


@Preview
@Composable
fun CredentialsScreen() {
    var text by remember {
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
            style = MaterialTheme.typography.headlineLarge,
            text = "Gulpi"
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            value = "",
            onValueChange = { text = it },
            label = { Text("App Token") }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            value = "",
            onValueChange = { text = it },
            label = { Text("User Token") }
        )

        Button(onClick = { /*TODO*/ }, Modifier.fillMaxWidth()) {
            Icon(Icons.Default.ExitToApp, contentDescription = "Login")
            Text("Login")
        }

        OutlinedButton(onClick = { /*TODO*/ }, Modifier.fillMaxWidth()) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Login")
            Text("Back")
        }
    }
}