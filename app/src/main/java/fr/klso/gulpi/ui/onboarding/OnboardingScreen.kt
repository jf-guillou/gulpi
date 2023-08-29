package fr.klso.gulpi.ui.onboarding

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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.typography
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.klso.gulpi.R
import fr.klso.gulpi.ui.theme.GulpiTheme

private const val TAG = "OnboardingScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel = viewModel()) {
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
                    style = typography.headlineLarge,
                    text = "Gulpi"
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    enabled = !state.isLoading,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    value = viewModel.url,
                    onValueChange = { viewModel.url = it },
                    isError = state.isMalformedUrl || state.isEndpointInvalid,
                    label = { Text("Server Endpoint URL") }
                )

                if (state.isMalformedUrl) {
                    Text(
                        text = "Malformed URL",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Red,
                    )
                }

                if (state.isEndpointInvalid) {
                    Text(
                        text = "Invalid endpoint",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Color.Red,
                    )
                }

                Button(
                    onClick = {
                        viewModel.checkAndPingUrl()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    enabled = !state.isLoading,
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Next")
                    Text("Next")
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
fun OnboardingScreenPreview() {
    OnboardingScreen()
}