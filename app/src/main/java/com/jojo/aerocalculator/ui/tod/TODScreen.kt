package com.jojo.aerocalculator.ui.tod

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jojo.aerocalculator.R
import com.jojo.aerocalculator.ui.composables.DeltaFlTextInfo
import com.jojo.aerocalculator.ui.composables.SectionCard
import com.jojo.aerocalculator.ui.composables.VzTextInfo
import kotlin.math.roundToInt

@Composable
fun TODScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "TODs",
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        )
        DistanceTod(Modifier.fillMaxWidth())
        TimeTod(Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun DistanceTod(modifier: Modifier = Modifier) {
    SectionCard(modifier = Modifier.then(modifier), title = "Distance") {
        var deltaFl by remember { mutableStateOf("") }

        var useVz by remember { mutableStateOf(false) }
        var slope by remember { mutableStateOf("") }
        var vz by remember { mutableStateOf("") }
        var gs by remember { mutableStateOf("") }

        var result by remember { mutableStateOf<Float?>(null) }

        OutlinedTextField(
            label = { DeltaFlTextInfo() },
            value = deltaFl,
            onValueChange = { deltaFl = it },
            placeholder = { Text("50") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            maxLines = 1
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .toggleable(useVz, role = Role.Switch) { useVz = !useVz },
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Use slope")
            Switch(checked = useVz, onCheckedChange = null)
            Text("Use Vz")
        }
        AnimatedVisibility(
            !useVz,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            OutlinedTextField(
                label = { Text(stringResource(R.string.slope)) },
                value = slope,
                onValueChange = { slope = it },
                placeholder = { Text("3.0") },
                suffix = { Text(stringResource(R.string.unit_slope)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                maxLines = 1
            )
        }
        AnimatedVisibility(
            useVz,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = vz,
                    onValueChange = { vz = it },
                    maxLines = 1,
                    label = { VzTextInfo() },
                    suffix = { Text(stringResource(R.string.unit_vz)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = gs,
                    onValueChange = { gs = it },
                    label = { Text("Ground speed") },
                    maxLines = 1,
                    suffix = { Text(stringResource(R.string.unit_speed)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    val d = deltaFl.toIntOrNull() ?: return@Button

                    result = if (!useVz) {
                        val s = slope.toFloatOrNull() ?: return@Button

                        ((d / s) * 10.0).roundToInt() / 10.0f
                    } else {
                        val speed = gs.toIntOrNull() ?: return@Button
                        val rate = vz.toIntOrNull() ?: return@Button

                        ((speed * (d * 100)) / (60 * rate) * 10.0).roundToInt() / 10.0f
                    }
                },
                enabled = if (useVz) gs.isNotEmpty() && vz.isNotEmpty() else deltaFl.isNotEmpty() && slope.isNotEmpty()
            ) {
                Text(stringResource(R.string.action_calc))
            }
            IconButton(onClick = { deltaFl = ""; slope = ""; result = null }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.content_desc_reset)
                )
            }
        }
        OutlinedTextField(
            value = "${result ?: 0}",
            onValueChange = {},
            suffix = { Text(stringResource(R.string.unit_dist)) },
            readOnly = true
        )
    }
}

@Composable
fun TimeTod(modifier: Modifier = Modifier) {
    SectionCard(modifier = Modifier.then(modifier), title = "Time") {
        var deltaFl by remember { mutableStateOf("") }
        var vz by remember { mutableStateOf("") }

        var result by remember { mutableStateOf<String?>(null) }

        OutlinedTextField(
            label = { DeltaFlTextInfo() },
            value = deltaFl,
            onValueChange = { deltaFl = it },
            placeholder = { Text("50") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            maxLines = 1
        )
        OutlinedTextField(
            label = { VzTextInfo() },
            value = vz,
            onValueChange = { vz = it },
            placeholder = { Text("1000") },
            suffix = { Text(stringResource(R.string.unit_vz)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            maxLines = 1
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                val d = deltaFl.toIntOrNull() ?: return@Button
                val v = vz.toIntOrNull() ?: return@Button

                val calc =
                    ((((d * 100) / v) * 10.0).roundToInt() / 10.0f).toString().split(".")

                result = "${calc[0]}:${(calc[1].toIntOrNull() ?: 0) * 60}"
            }, enabled = deltaFl.isNotEmpty() && vz.isNotEmpty()) {
                Text(stringResource(R.string.action_calc))
            }
            IconButton(onClick = { deltaFl = ""; vz = ""; result = null }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.content_desc_reset)
                )
            }
        }
        OutlinedTextField(
            value = "${result ?: 0}",
            onValueChange = {},
            suffix = { Text("mins") },
            readOnly = true
        )
    }
}