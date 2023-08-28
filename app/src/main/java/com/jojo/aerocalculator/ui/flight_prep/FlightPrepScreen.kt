package com.jojo.aerocalculator.ui.flight_prep

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
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jojo.aerocalculator.R
import com.jojo.aerocalculator.tools.toFormattedTime
import com.jojo.aerocalculator.ui.composables.SectionCard

@Composable
fun FlightPrepScreen() {
    val viewModel = hiltViewModel<FlightPrepViewModel>()
    val flightTime by viewModel.flightTime.collectAsState()
    val altTime by viewModel.altTime.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        SectionCard(title = "General information") {
            OutlinedTextField(
                value = viewModel.aircraft?.ICAO ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Aircraft") }, trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.ArrowForwardIos, contentDescription = null)
                    }
                }
            )

            HorizontalDivider(Modifier.fillMaxWidth())

            OutlinedTextField(
                value = viewModel.distance,
                onValueChange = { viewModel.onFieldChanged("distance", it) },
                label = { Text("Distance") },
                suffix = { Text(stringResource(R.string.unit_dist)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                maxLines = 1
            )

            OutlinedTextField(
                value = viewModel.altDistance,
                onValueChange = { viewModel.onFieldChanged("alt_distance", it) },
                label = { Text("Alternate distance") },
                suffix = { Text(stringResource(R.string.unit_dist)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                maxLines = 1
            )

            OutlinedTextField(
                value = flightTime?.toFormattedTime() ?: "00h 00min 00sec",
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Flight time")
                })

            OutlinedTextField(
                value = altTime?.toFormattedTime() ?: "00h 00min 00sec",
                onValueChange = {},
                readOnly = true,
                label = { Text("Alternate flight time") })
        }

        SectionCard(title = "Aircraft information") { _ ->
            viewModel.aircraft?.let { ac ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${ac.model} - ${ac.enginesNumber} engines")
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { viewModel.onAircraftChangeDetails("", "reset") }) {
                        Text("Reset")
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Cruise", Modifier.weight(.2f))
                    OutlinedTextField(
                        value = ac.cruiseFF.toString(),
                        onValueChange = { viewModel.onAircraftChangeDetails(it, "cruiseFF") },
                        maxLines = 1,
                        suffix = { Text(text = "Gal/h") },
                        label = { Text(text = "Fuel flow") },
                        modifier = Modifier.weight(.3f)
                    )
                    OutlinedTextField(
                        value = ac.cruisePwr.toString(),
                        onValueChange = { viewModel.onAircraftChangeDetails(it, "cruisePwr") },
                        maxLines = 1,
                        label = { Text(text = "Power") },
                        suffix = { Text(text = "%") },
                        modifier = Modifier.weight(.3f)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Hold", Modifier.weight(.2f))
                    OutlinedTextField(
                        value = ac.holdFF.toString(),
                        onValueChange = { viewModel.onAircraftChangeDetails(it, "holdFF") },
                        maxLines = 1,
                        suffix = { Text(text = "Gal/h") },
                        label = { Text(text = "Fuel flow") },
                        modifier = Modifier.weight(.3f)
                    )
                    OutlinedTextField(
                        value = ac.holdPwr.toString(),
                        onValueChange = { viewModel.onAircraftChangeDetails(it, "holdPwr") },
                        maxLines = 1,
                        label = { Text(text = "Power") },
                        suffix = { Text(text = "%") },
                        modifier = Modifier.weight(.3f)
                    )
                }
            }
        }

        SectionCard(title = "Fuel") {
            Column {
                OutlinedTextField(
                    value = viewModel.taxiF,
                    onValueChange = { viewModel.onFieldChanged("taxi_fuel", it) },
                    label = { Text("Taxi") }, suffix = { Text(text = "Gal") })
                OutlinedTextField(
                    value = viewModel.tripF,
                    onValueChange = { viewModel.onFieldChanged("trip_fuel", it) },
                    label = { Text("Trip") }, suffix = { Text(text = "Gal") })
                OutlinedTextField(
                    value = viewModel.contingencyF,
                    onValueChange = { viewModel.onFieldChanged("contingency_fuel", it) },
                    label = { Text("Contingency") }, suffix = { Text(text = "Gal") })
                OutlinedTextField(
                    value = viewModel.alternateF,
                    onValueChange = { viewModel.onFieldChanged("alternate_fuel", it) },
                    label = { Text("Alternate") }, suffix = { Text(text = "Gal") })
                OutlinedTextField(
                    value = viewModel.finalF,
                    onValueChange = { viewModel.onFieldChanged("final_fuel", it) },
                    label = { Text("Final") }, suffix = { Text(text = "Gal") })
                OutlinedTextField(
                    value = viewModel.additionalF,
                    onValueChange = { viewModel.onFieldChanged("additional_fuel", it) },
                    label = { Text("Additional") }, suffix = { Text(text = "Gal") })
                OutlinedTextField(
                    value = viewModel.discretionaryF,
                    onValueChange = { viewModel.onFieldChanged("discretionary_fuel", it) },
                    label = { Text("Discretionary") }, suffix = { Text(text = "Gal") })
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}