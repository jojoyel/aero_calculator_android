package com.jojo.aerocalculator.ui.metar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MetarScreen() {
    val viewModel = hiltViewModel<MetarViewModel>()

    var icao by remember { mutableStateOf("") }

    Column {
        Text(viewModel.result.value)
        OutlinedTextField(value = icao, onValueChange = { icao = it })
        Button(onClick = { viewModel.onFetch(icao) }) {
            Text(text = "Get Metar")
        }
    }
}