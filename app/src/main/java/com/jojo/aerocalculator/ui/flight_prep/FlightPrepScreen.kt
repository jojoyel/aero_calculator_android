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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jojo.aerocalculator.R
import com.jojo.aerocalculator.data.models.aircraft.EngineSpeedType
import com.jojo.aerocalculator.tools.toFormattedTime
import com.jojo.aerocalculator.ui.composables.Dropdown
import com.jojo.aerocalculator.ui.composables.SectionCard
import com.jojo.aerocalculator.ui.composables.SwitchRow

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
                        text = { Text(stringResource(R.string.briefing)) },
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
                        SectionCard(title = stringResource(R.string.general_info)) {
                            Dropdown(
                                label = stringResource(R.string.aircraft),
                                data = allAircraft.map { it.ICAO }) { icao ->
                                viewModel.onAircraftChanged(icao)
                            }

                            HorizontalDivider(Modifier.fillMaxWidth())

                            OutlinedTextField(
                                value = viewModel.distance,
                                onValueChange = { viewModel.onFieldChanged("distance", it) },
                                label = { Text(stringResource(R.string.distance)) },
                                suffix = { Text(stringResource(R.string.unit_dist)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                maxLines = 1
                            )
                            OutlinedTextField(
                                value = flightTime.toFormattedTime(),
                                onValueChange = {},
                                readOnly = true,
                                label = {
                                    Text(stringResource(R.string.flight_time))
                                })

                            SwitchRow(
                                checked = viewModel.haveAlternate,
                                unselectedLabel = { Text(stringResource(R.string.action_using_alt_airport)) },
                            ) { viewModel.toggleHaveAlternate() }

                            AnimatedVisibility(viewModel.haveAlternate) {
                                Column {
                                    OutlinedTextField(
                                        value = viewModel.altDistance,
                                        onValueChange = {
                                            viewModel.onFieldChanged("alt_distance", it)
                                        },
                                        label = { Text(stringResource(R.string.alternate_distance)) },
                                        suffix = { Text(stringResource(R.string.unit_dist)) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        maxLines = 1
                                    )
                                    OutlinedTextField(
                                        value = altTime.toFormattedTime(),
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text(stringResource(R.string.alternate_flight_time)) })
                                }
                            }
                        }

                        SectionCard(title = "Aircraft information") { _ ->
                            viewModel.aircraft.let { ac ->
                                val speedTypeStringRes = when (ac.engineSpeedType) {
                                    EngineSpeedType.PERCENT -> R.string.unit_percent
                                    EngineSpeedType.RPM -> R.string.unit_rpm
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        stringResource(
                                            R.string.info_ac_model_engines,
                                            ac.model,
                                            ac.enginesNumber
                                        )
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Button(onClick = {
                                        viewModel.onAircraftChangeDetails("", "reset")
                                    }) {
                                        Text(stringResource(R.string.action_reset))
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(stringResource(R.string.cruise), Modifier.weight(.2f))
                                    OutlinedTextField(
                                        value = ac.cruiseFF.toString(),
                                        onValueChange = {
                                            viewModel.onAircraftChangeDetails(it, "cruiseFF")
                                        },
                                        maxLines = 1,
                                        suffix = { Text(stringResource(R.string.unit_cons_gal)) },
                                        label = { Text(stringResource(R.string.fuel_flow)) },
                                        modifier = Modifier.weight(.3f)
                                    )
                                    OutlinedTextField(
                                        value = ac.cruisePwr.toString(),
                                        onValueChange = {
                                            viewModel.onAircraftChangeDetails(it, "cruisePwr")
                                        },
                                        maxLines = 1,
                                        label = { Text(stringResource(R.string.power)) },
                                        suffix = { Text(stringResource(speedTypeStringRes)) },
                                        modifier = Modifier.weight(.3f)
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(stringResource(R.string.hold), Modifier.weight(.2f))
                                    OutlinedTextField(
                                        value = ac.holdFF.toString(),
                                        onValueChange = {
                                            viewModel.onAircraftChangeDetails(it, "holdFF")
                                        },
                                        maxLines = 1,
                                        suffix = { Text(stringResource(R.string.unit_cons_gal)) },
                                        label = { Text(stringResource(R.string.fuel_flow)) },
                                        modifier = Modifier.weight(.3f)
                                    )
                                    OutlinedTextField(
                                        value = ac.holdPwr.toString(),
                                        onValueChange = {
                                            viewModel.onAircraftChangeDetails(it, "holdPwr")
                                        },
                                        maxLines = 1,
                                        label = { Text(stringResource(R.string.power)) },
                                        suffix = { Text(stringResource(speedTypeStringRes)) },
                                        modifier = Modifier.weight(.3f)
                                    )
                                }
                            }
                        }

                        SectionCard(title = stringResource(R.string.fuel)) {
                            Column {
                                val tripF by viewModel.tripF.collectAsState()
                                val contingencyF by viewModel.contingencyF.collectAsState()
                                val alternateF by viewModel.alternateF.collectAsState()
                                val finalF by viewModel.finalF.collectAsState()

                                OutlinedTextField(
                                    value = viewModel.taxiF,
                                    onValueChange = { viewModel.onFieldChanged("taxi_fuel", it) },
                                    label = { Text(stringResource(R.string.fuel_taxi)) },
                                    suffix = { Text(stringResource(R.string.unit_gal)) })
                                OutlinedTextField(
                                    value = tripF.toString(),
                                    onValueChange = { },
                                    label = { Text(stringResource(R.string.fuel_trip)) },
                                    suffix = { Text(stringResource(R.string.unit_gal)) },
                                    readOnly = true
                                )
                                OutlinedTextField(
                                    value = contingencyF.toString(),
                                    onValueChange = {},
                                    label = { Text(stringResource(R.string.fuel_contingency)) },
                                    suffix = { Text(stringResource(R.string.unit_gal)) },
                                    readOnly = true
                                )
                                OutlinedTextField(
                                    value = alternateF.toString(),
                                    onValueChange = {},
                                    label = { Text(stringResource(R.string.fuel_alternate)) },
                                    suffix = { Text(stringResource(R.string.unit_gal)) })
                                OutlinedTextField(
                                    value = finalF.toString(),
                                    onValueChange = {},
                                    label = { Text(stringResource(R.string.fuel_final)) },
                                    suffix = { Text(stringResource(R.string.unit_gal)) })
                                OutlinedTextField(
                                    value = viewModel.additionalF,
                                    onValueChange = {
                                        viewModel.onFieldChanged("additional_fuel", it)
                                    },
                                    label = { Text(stringResource(R.string.fuel_additional)) },
                                    suffix = { Text(stringResource(R.string.unit_gal)) })
                                OutlinedTextField(
                                    value = viewModel.discretionaryF,
                                    onValueChange = {
                                        viewModel.onFieldChanged("discretionary_fuel", it)
                                    },
                                    label = { Text(stringResource(R.string.fuel_discretionary)) },
                                    suffix = { Text(stringResource(R.string.unit_gal)) })
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