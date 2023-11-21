package fr.klso.gulpi.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

private const val TAG = "SearchScreen"

@Composable
fun SearchScreen(
    navController: NavController,
    criteria: String?,
    viewModel: SearchViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    if (criteria != null) {
        viewModel.searchFromScan(criteria)
    }

    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            enabled = !state.isLoading,
            value = viewModel.textCriteria,
            onValueChange = { viewModel.textCriteria = it },
            label = { Text("Search") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                viewModel.search()
            }),
        )

        if (state.items.count > 0)
            Text(state.items.data[0].name)
    }
}