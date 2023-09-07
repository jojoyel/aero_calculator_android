package com.jojo.aerocalculator.ui.wind

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
import com.jojo.aerocalculator.ui.composables.FbTextInfo
import com.jojo.aerocalculator.ui.composables.SectionCard
import com.jojo.aerocalculator.ui.composables.SwitchRow
import com.jojo.aerocalculator.ui.composables.VwTextInfo
import com.jojo.aerocalculator.util.calc.calcBaseFactor
import kotlin.math.roundToInt

@Composable
fun WindScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            stringResource(R.string.winds),
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        )
        MaxDrift(Modifier.fillMaxWidth())
        Drift(Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun MaxDrift(modifier: Modifier = Modifier) {
    SectionCard(title = "Max. drift", modifier = Modifier.then(modifier)) {
        var fb by remember { mutableStateOf("") }
        var vw by remember { mutableStateOf("") }

        var result by remember { mutableStateOf<Int?>(null) }

        OutlinedTextField(
            label = { FbTextInfo() },
            value = fb,
            onValueChange = { fb = it },
            maxLines = 1,
            placeholder = { Text("0.35") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        OutlinedTextField(
            label = { VwTextInfo() },
            value = vw,
            onValueChange = { vw = it },
            maxLines = 1,
            placeholder = { Text("5") },
            suffix = { Text(stringResource(R.string.unit_speed)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                val f = fb.toFloatOrNull() ?: return@Button
                val v = vw.toIntOrNull() ?: return@Button

                result = (f * v).roundToInt()

            }, enabled = fb.isNotEmpty() && vw.isNotEmpty()) {
                Text(stringResource(R.string.action_calc))
            }
            IconButton(onClick = { fb = ""; vw = ""; result = null }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.content_desc_reset)
                )
            }
        }
        OutlinedTextField(
            value = "${result ?: 0}",
            onValueChange = {},
            suffix = { Text(stringResource(R.string.unit_dir)) },
            readOnly = true
        )
    }
}

@Composable
fun Drift(modifier: Modifier = Modifier) {
    SectionCard(title = "Drift", modifier = Modifier.then(modifier)) {
        var vt by remember { mutableStateOf("") }

        var useGs by remember { mutableStateOf(false) }
        var fb by remember { mutableStateOf("") }
        var gs by remember { mutableStateOf("") }

        var result by remember { mutableStateOf<Int?>(null) }

        OutlinedTextField(
            label = { Text("Crosswind") },
            value = vt,
            onValueChange = { vt = it },
            maxLines = 1,
            placeholder = { Text("10") },
            suffix = { Text(stringResource(R.string.unit_dir)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        SwitchRow(
            checked = useGs,
            unselectedLabel = { Text("Use base factor") },
            selectedLabel = { Text("Use ground speed") },
            onCheckedChange = { useGs = !useGs })
        AnimatedVisibility(
            !useGs, enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            OutlinedTextField(
                label = { FbTextInfo() },
                value = fb,
                onValueChange = { fb = it },
                maxLines = 1,
                placeholder = { Text("0.35") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
        }
        AnimatedVisibility(
            visible = useGs, enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            OutlinedTextField(
                label = { Text("Vitesse sol actuelle") },
                value = gs,
                onValueChange = { gs = it },
                maxLines = 1,
                placeholder = { Text("160") },
                suffix = { Text(stringResource(R.string.unit_speed)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = {
                    val v = vt.toIntOrNull() ?: return@Button
                    val f = if (!useGs) fb.toFloatOrNull()
                        ?: return@Button else calcBaseFactor(gs.toIntOrNull() ?: return@Button)

                    result = (f * v).roundToInt()
                },
                enabled = if (useGs) vt.isNotEmpty() && gs.isNotEmpty() else fb.isNotEmpty() && vt.isNotEmpty()
            ) {
                Text(stringResource(R.string.action_calc))
            }
            IconButton(onClick = { fb = ""; vt = ""; result = null }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.content_desc_reset)
                )
            }
        }
        OutlinedTextField(
            value = "${result ?: 0}",
            onValueChange = {},
            suffix = { Text(stringResource(R.string.unit_dir)) },
            readOnly = true
        )
    }
}