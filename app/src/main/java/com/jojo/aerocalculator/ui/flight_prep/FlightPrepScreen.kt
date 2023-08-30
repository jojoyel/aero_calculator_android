package com.jojo.aerocalculator.ui.flight_prep

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jojo.aerocalculator.R
import com.jojo.aerocalculator.tools.toFormattedTime
import com.jojo.aerocalculator.ui.composables.Dropdown
import com.jojo.aerocalculator.ui.composables.SectionCard

@Composable
fun FlightPrepScreen() {
    val viewModel = hiltViewModel<FlightPrepViewModel>()
    val allAircraft by viewModel.allAircraft.collectAsState()
    val flightTime by viewModel.flightTime.collectAsState()
    val altTime by viewModel.altTime.collectAsState()

    var isBriefingPage by remember { mutableStateOf(false) }

    Crossfade(targetState = isBriefingPage, label = "") { itIs ->
        when (itIs) {
            false -> {
                val scrollState = rememberScrollState()


                Scaffold(floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = { isBriefingPage = true },
                        text = { Text("Briefing") },
                        icon = { Icon(Icons.Default.Description, contentDescription = null) },
                        expanded = scrollState.value <= 10
                    )
                }) { paddingValues ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 24.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(12.dp))
                        SectionCard(title = "General information") {
                            Dropdown(
                                label = "Aircraft",
                                data = allAircraft.map { it.ICAO }) { icao ->
                                viewModel.onAircraftChanged(icao)
                            }

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
                                value = flightTime.toFormattedTime(),
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text("Flight time")
                                })

                            Row(modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .toggleable(
                                    viewModel.haveAlternate,
                                    role = Role.Switch
                                ) { viewModel.toggleHaveAlternate() }
                                .padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("Using alternate airport")
                                Spacer(Modifier.width(4.dp))
                                Switch(checked = viewModel.haveAlternate, onCheckedChange = null)
                            }

                            AnimatedVisibility(viewModel.haveAlternate) {
                                Column {
                                    OutlinedTextField(
                                        value = viewModel.altDistance,
                                        onValueChange = {
                                            viewModel.onFieldChanged(
                                                "alt_distance",
                                                it
                                            )
                                        },
                                        label = { Text("Alternate distance") },
                                        suffix = { Text(stringResource(R.string.unit_dist)) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        maxLines = 1
                                    )
                                    OutlinedTextField(
                                        value = altTime.toFormattedTime(),
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Alternate flight time") })
                                }
                            }
                        }

                        SectionCard(title = "Aircraft information") { _ ->
                            viewModel.aircraft.let { ac ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("${ac.model} - ${ac.enginesNumber} engines")
                                    Spacer(modifier = Modifier.weight(1f))
                                    Button(onClick = {
                                        viewModel.onAircraftChangeDetails(
                                            "",
                                            "reset"
                                        )
                                    }) {
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
                                        onValueChange = {
                                            viewModel.onAircraftChangeDetails(
                                                it,
                                                "cruiseFF"
                                            )
                                        },
                                        maxLines = 1,
                                        suffix = { Text(text = "Gal/h") },
                                        label = { Text(text = "Fuel flow") },
                                        modifier = Modifier.weight(.3f)
                                    )
                                    OutlinedTextField(
                                        value = ac.cruisePwr.toString(),
                                        onValueChange = {
                                            viewModel.onAircraftChangeDetails(
                                                it,
                                                "cruisePwr"
                                            )
                                        },
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
                                        onValueChange = {
                                            viewModel.onAircraftChangeDetails(
                                                it,
                                                "holdFF"
                                            )
                                        },
                                        maxLines = 1,
                                        suffix = { Text(text = "Gal/h") },
                                        label = { Text(text = "Fuel flow") },
                                        modifier = Modifier.weight(.3f)
                                    )
                                    OutlinedTextField(
                                        value = ac.holdPwr.toString(),
                                        onValueChange = {
                                            viewModel.onAircraftChangeDetails(
                                                it,
                                                "holdPwr"
                                            )
                                        },
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
                                val tripF by viewModel.tripF.collectAsState()
                                val contingencyF by viewModel.contingencyF.collectAsState()
                                val alternateF by viewModel.alternateF.collectAsState()
                                val finalF by viewModel.finalF.collectAsState()

                                OutlinedTextField(
                                    value = viewModel.taxiF,
                                    onValueChange = { viewModel.onFieldChanged("taxi_fuel", it) },
                                    label = { Text("Taxi") }, suffix = { Text(text = "Gal") })
                                OutlinedTextField(
                                    value = tripF.toString(),
                                    onValueChange = { },
                                    label = { Text("Trip") }, suffix = { Text(text = "Gal") },
                                    readOnly = true
                                )
                                OutlinedTextField(
                                    value = contingencyF.toString(),
                                    onValueChange = {},
                                    label = { Text("Contingency") },
                                    suffix = { Text(text = "Gal") },
                                    readOnly = true
                                )
                                OutlinedTextField(
                                    value = alternateF.toString(),
                                    onValueChange = {},
                                    label = { Text("Alternate") }, suffix = { Text(text = "Gal") })
                                OutlinedTextField(
                                    value = finalF.toString(),
                                    onValueChange = {},
                                    label = { Text("Final") }, suffix = { Text(text = "Gal") })
                                OutlinedTextField(
                                    value = viewModel.additionalF,
                                    onValueChange = {
                                        viewModel.onFieldChanged(
                                            "additional_fuel",
                                            it
                                        )
                                    },
                                    label = { Text("Additional") }, suffix = { Text(text = "Gal") })
                                OutlinedTextField(
                                    value = viewModel.discretionaryF,
                                    onValueChange = {
                                        viewModel.onFieldChanged(
                                            "discretionary_fuel",
                                            it
                                        )
                                    },
                                    label = { Text("Discretionary") },
                                    suffix = { Text(text = "Gal") })
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            true -> {
                BackHandler {
                    isBriefingPage = false
                }

                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Text("Aircraft : ${viewModel.aircraft.model}")
                    Text("Base factor : ${viewModel.aircraft.baseFactor}")
                    Text("Cruise power : ${viewModel.aircraft.cruisePwr} %")
                    Text("Holding power : ${viewModel.aircraft.holdPwr} %")
                    HorizontalDivider()
                    Text("Flight distance : ${viewModel.distance}")
                    Text("Flight time : ${flightTime.toFormattedTime()}")
                    if (viewModel.haveAlternate) {
                        Text("Alternate distance : ${viewModel.altDistance}")
                        Text("Alternate time : ${altTime.toFormattedTime()}")
                    }
                    HorizontalDivider()
                    Text("Fuel")
                    Text("Total fuel : ${viewModel.totalFuel() ?: 0} Gal")
                    Text("Endurance : ${viewModel.endurance()}")
                }
            }
        }
    }
}