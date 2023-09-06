package fr.klso.gulpi.ui.credentials

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.klso.gulpi.R
import fr.klso.gulpi.ui.theme.GulpiTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CredentialsScreen(viewModel: CredentialsViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()

    GulpiTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {}
                )
            }
        ) { padding ->
            Column(
                Modifier
                    .padding(padding)
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
                    enabled = !state.isLoading,
                    value = viewModel.appToken,
                    isError = state.isBadCredentials,
                    onValueChange = { viewModel.appToken = it },
                    label = { Text("App Token") }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    enabled = !state.isLoading,
                    value = viewModel.userToken,
                    isError = state.isBadCredentials,
                    onValueChange = { viewModel.userToken = it },
                    label = { Text("User Token") }
                )

                if (state.isBadCredentials) {
                    Text(
                        text = "Invalid credentials",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Red,
                    )
                }

                Button(onClick = {
                    viewModel.validateTokens()
                }, modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,) {
                    Icon(Icons.Default.ExitToApp, contentDescription = "Login")
                    Text("Login")
                }

                OutlinedButton(onClick = {
                    viewModel.goBack()
                }, modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading,) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Login")
                    Text("Back")
                }

                if (state.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CredentialsScreenPreview() {
    CredentialsScreen()
}