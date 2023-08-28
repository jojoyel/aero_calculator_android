package com.jojo.aerocalculator.ui.flight_prep

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.aerocalculator.data.models.aircraft.Aircraft
import com.jojo.aerocalculator.data.models.aircraft.AircraftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightPrepViewModel @Inject constructor(private val aircraftRepository: AircraftRepository) :
    ViewModel() {

    var distance by mutableStateOf("")
        private set
    var altDistance by mutableStateOf("")
        private set

    private val _allAircraft = MutableStateFlow<List<Aircraft>>(emptyList())
    val allAircraft = _allAircraft.asStateFlow()

    var aircraft by mutableStateOf<Aircraft?>(null)
        private set

    val flightTime = snapshotFlow { distance }.combine(snapshotFlow { aircraft }) { d, a ->
        a?.let {
            a.baseFactor * (d.toFloatOrNull() ?: 0f)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0f)

    val altTime = snapshotFlow { altDistance }.combine(snapshotFlow { aircraft }) { d, a ->
        a?.let {
            a.baseFactor * (d.toFloatOrNull() ?: 0f)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0f)

    var taxiF by mutableStateOf("")
        private set
    var tripF by mutableStateOf("")
        private set
    var contingencyF by mutableStateOf("")
        private set
    var alternateF by mutableStateOf("")
        private set
    var finalF by mutableStateOf("")
        private set
    var additionalF by mutableStateOf("")
        private set
    var discretionaryF by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            val acList = aircraftRepository.getAllAircraft()

            _allAircraft.update { acList }
            aircraft = acList[0]
        }
    }

    fun onFieldChanged(field: String, value: String) {
        when (field) {
            "distance" -> distance = value
            "alt_distance" -> altDistance = value
            "taxi_fuel" -> taxiF = value
            "trip_fuel" -> tripF = value
            "contingency_fuel" -> contingencyF = value
            "alternate_fuel" -> alternateF = value
            "final_fuel" -> finalF = value
            "additional_fuel" -> additionalF = value
            "discretionary_fuel" -> discretionaryF = value
        }
    }

    fun onAircraftChanged(ICAO: String) {
        viewModelScope.launch {
            aircraftRepository.getByICAO(ICAO)?.let {
                aircraft = it
            }
        }
    }

    fun onAircraftChangeDetails(value: String, field: String) {
        aircraft =
            when (field) {
                "cruiseFF" -> value.toFloatOrNull()?.let { aircraft?.copy(cruiseFF = it) }
                    ?: run { aircraft }

                "cruisePwr" -> value.toIntOrNull()?.let { aircraft?.copy(cruisePwr = it) }
                    ?: run { aircraft }

                "holdFF" -> value.toFloatOrNull()?.let { aircraft?.copy(holdFF = it) }
                    ?: run { aircraft }

                "holdPwr" -> value.toIntOrNull()?.let { aircraft?.copy(holdPwr = it) }
                    ?: run { aircraft }

                "reset" -> allAircraft.value.first { it.ICAO == aircraft?.ICAO }

                else -> aircraft
            }
    }
}