package com.jojo.aerocalculator.ui.metar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MetarScreen() {
    val viewModel = hiltViewModel<MetarViewModel>()

    var icao by remember { mutableStateOf("") }

    Column {
        ICAOTextField(
            icao = icao,
            onImeClicked = { viewModel.onFetch(icao) }
        ) { icao = it }
        Button(onClick = { viewModel.onFetch(icao) }) {
            Text(text = "Get Metar")
        }
        HorizontalDivider()
        Text(text = "Result")
        Text(viewModel.result.value)
        Text(text = "Metar decoder will be implemented soon")
    }
}

@Composable
fun ICAOTextField(
    icao: String,
    onImeClicked: KeyboardActionScope.() -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = icao,
        onValueChange = { if (it.length <= 4) onValueChange(it.uppercase()) },
        singleLine = true,
        keyboardActions = KeyboardActions(onSearch = onImeClicked),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            autoCorrect = false,
            imeAction = ImeAction.Search
        ),
        label = { Text(text = "Airport ICAO") }
    )
}