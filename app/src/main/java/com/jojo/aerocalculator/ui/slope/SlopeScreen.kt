package com.jojo.aerocalculator.ui.slope

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jojo.aerocalculator.R
import com.jojo.aerocalculator.ui.composables.SectionCard
import com.jojo.aerocalculator.ui.composables.VzTextInfo
import kotlin.math.roundToInt

@Composable
fun SlopeScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.slopes),
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        )
        VzSlope(Modifier.fillMaxWidth())
        PercentSlope(Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun VzSlope(modifier: Modifier = Modifier) {
    SectionCard(title = "Vz", modifier = Modifier.then(modifier)) {
        var gs by remember { mutableStateOf("") }
        var slope by remember { mutableStateOf("") }

        var result by remember { mutableStateOf<Int?>(null) }

        OutlinedTextField(
            label = { Text("Gs") },
            value = gs,
            onValueChange = { gs = it },
            maxLines = 1,
            placeholder = { Text("160") },
            suffix = { Text(stringResource(R.string.unit_speed)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            label = { Text(stringResource(R.string.slope)) },
            value = slope,
            onValueChange = { slope = it },
            maxLines = 1,
            placeholder = { Text("3.0") },
            suffix = { Text(stringResource(R.string.unit_slope)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                val s = gs.toIntOrNull() ?: return@Button
                val sl = slope.toFloatOrNull() ?: return@Button

                result = (s * sl).roundToInt()

            }, enabled = gs.isNotEmpty() && slope.isNotEmpty()) {
                Text(stringResource(R.string.action_calc))
            }
            IconButton(onClick = { slope = ""; gs = ""; result = null }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.content_desc_reset)
                )
            }
        }
        OutlinedTextField(
            value = "${result ?: 0}",
            onValueChange = {},
            suffix = { Text(stringResource(R.string.unit_vz)) },
            readOnly = true
        )
    }
}

@Composable
fun PercentSlope(modifier: Modifier = Modifier) {
    SectionCard(title = "%") {
        var vz by remember { mutableStateOf("") }
        var gs by remember { mutableStateOf("") }

        var result by remember { mutableStateOf<Float?>(null) }

        OutlinedTextField(
            label = { VzTextInfo() },
            value = vz,
            onValueChange = { vz = it },
            maxLines = 1,
            placeholder = { Text("500") },
            suffix = { Text(stringResource(R.string.unit_vz)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            label = { Text("Gs") },
            value = gs,
            onValueChange = { gs = it },
            maxLines = 1,
            placeholder = { Text("160") },
            suffix = { Text(stringResource(R.string.unit_speed)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                val s = gs.toIntOrNull() ?: return@Button
                val v = vz.toFloatOrNull() ?: return@Button

                result = ((v / s) * 10.0).roundToInt() / 10.0f
            }, enabled = gs.isNotEmpty() && vz.isNotEmpty()) {
                Text(stringResource(R.string.action_calc))
            }
            IconButton(onClick = { vz = ""; gs = ""; result = null }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.content_desc_reset)
                )
            }
        }
        OutlinedTextField(
            value = "${result ?: 0}",
            onValueChange = {},
            suffix = { Text(stringResource(R.string.unit_slope)) },
            readOnly = true
        )
    }
}