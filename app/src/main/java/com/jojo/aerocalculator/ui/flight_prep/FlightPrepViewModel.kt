package com.jojo.aerocalculator.ui.flight_prep

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

    private val _distance = MutableStateFlow("")
    val distance = _distance.asStateFlow()

    private val _altDistance = MutableStateFlow("")
    val altDistance = _altDistance.asStateFlow()

    private val _aircraft = MutableStateFlow<Aircraft?>(null)
    val aircraft = _aircraft.asStateFlow()

    val flightTime =
        _distance.combine(_aircraft) { d, a -> a?.let { a.baseFactor * (d.toFloatOrNull() ?: 0f) } }
            .stateIn(viewModelScope, SharingStarted.Lazily, 0.0f)
    val altTime =
        _altDistance.combine(_aircraft) { d, a ->
            a?.let {
                a.baseFactor * (d.toFloatOrNull() ?: 0f)
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0f)

    init {
        viewModelScope.launch {
            _aircraft.update { aircraftRepository.getAllAircraft()[0] }
        }
    }

    fun onDistanceChanged(d: String, alternate: Boolean = false) {
        if (alternate)
            _altDistance.update { d }
        else
            _distance.update { d }
    }

    fun onAircraftChanged(ICAO: String) {
        viewModelScope.launch {
            aircraftRepository.getByICAO(ICAO)?.let {
                _aircraft.update { it }
            }
        }
    }

    fun onAircraftChangeDetails(value: String, field: String) {
        _aircraft.update { ac ->
            when (field) {
                "cruiseFF" -> value.toFloatOrNull()?.let { ac?.copy(cruiseFF = it) }
                "cruisePwr" -> value.toIntOrNull()?.let { ac?.copy(cruisePwr = it) }
                "holdFF" -> value.toFloatOrNull()?.let { ac?.copy(holdFF = it) }
                "holdPwr" -> value.toIntOrNull()?.let { ac?.copy(holdPwr = it) }
                else -> ac
            }
        }
    }
}