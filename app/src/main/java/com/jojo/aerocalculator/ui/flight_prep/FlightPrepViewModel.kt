package com.jojo.aerocalculator.ui.flight_prep

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jojo.aerocalculator.data.models.aircraft.Aircraft
import com.jojo.aerocalculator.data.models.aircraft.AircraftRepository
import com.jojo.aerocalculator.data.models.aircraft.EngineSpeedType
import com.jojo.aerocalculator.data.models.aircraft.EngineType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Float.max
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.round
import kotlin.math.roundToInt

@HiltViewModel
class FlightPrepViewModel @Inject constructor(private val aircraftRepository: AircraftRepository) :
    ViewModel() {

    var distance by mutableStateOf("")
        private set
    var altDistance by mutableStateOf("")
        private set

    var haveAlternate by mutableStateOf(true)
        private set

    private val _allAircraft = MutableStateFlow<List<Aircraft>>(emptyList())
    val allAircraft = _allAircraft.asStateFlow()

    var aircraft by mutableStateOf(
        Aircraft(
            "",
            "",
            "",
            0,
            0f,
            0f,
            0,
            0f,
            0,
            EngineType.PISTON,
            EngineSpeedType.RPM
        )
    )
        private set

    val flightTime = snapshotFlow { distance }.combine(snapshotFlow { aircraft }) { d, a ->
        a.baseFactor * (d.toFloatOrNull() ?: 0f)
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0f)

    val altTime = snapshotFlow { altDistance }.combine(snapshotFlow { aircraft }) { d, a ->
        a.baseFactor * (d.toFloatOrNull() ?: 0f)
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0f)

    var taxiF by mutableStateOf("1.0")
        private set

    var tripF = snapshotFlow { aircraft }.combine(flightTime) { a, time ->
        (((a.cruiseFF) * (time / 60f)) * 10).roundToInt() / 10f
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0f)

    var contingencyF = snapshotFlow { aircraft }.combine(tripF) { a, tripFuel ->
        (max(tripFuel * 0.05f, (5 * a.cruiseFF) / 60) * 10).roundToInt() / 10f
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0f)

    var alternateF = combine(
        snapshotFlow { aircraft },
        altTime,
        snapshotFlow { haveAlternate }
    ) { a, time, haveAlt ->
            if (haveAlt)
                ((a.cruiseFF) * (time / 60f) * 10).roundToInt() / 10f
            else
                ((a.holdFF * .5f) * 10).roundToInt() / 10f
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0f)

    var finalF = snapshotFlow { aircraft }.transform<Aircraft, Float> { a ->
        val f = if (a.engineType == EngineType.TURBINE)
            ((a.holdFF * .5f) * 10).roundToInt() / 10f
        else
            ((a.holdFF * .75f) * 10).roundToInt() / 10f

        emit(f)
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0f)

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

    fun totalFuel(): Float? {
        return taxiF.toFloatOrNull() +
                tripF.value +
                contingencyF.value +
                alternateF.value +
                finalF.value +
                additionalF.toFloatOrNull() +
                discretionaryF.toFloatOrNull()
    }

    fun endurance(): String {
        val total = totalFuel()
        val decimal = DecimalFormat("00")
        val result = (total ?: 0f) / aircraft.cruiseFF
        val splitedResult = result.toString().split(".")
        val minutes = ("0.${splitedResult.getOrElse(1) { "0" }}".toFloat()) * 60f

        return "${decimal.format(splitedResult[0].toInt())}${decimal.format(round(minutes))}"
    }

    fun onFieldChanged(field: String, value: String) {
        when (field) {
            "distance" -> distance = value
            "alt_distance" -> altDistance = value
            "taxi_fuel" -> taxiF = value
            "additional_fuel" -> additionalF = value
            "discretionary_fuel" -> discretionaryF = value
        }
    }

    fun toggleHaveAlternate() {
        haveAlternate = !haveAlternate
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
                "cruiseFF" -> value.toFloatOrNull()?.let { aircraft.copy(cruiseFF = it) }
                    ?: run { aircraft }

                "cruisePwr" -> value.toIntOrNull()?.let { aircraft.copy(cruisePwr = it) }
                    ?: run { aircraft }

                "holdFF" -> value.toFloatOrNull()?.let { aircraft.copy(holdFF = it) }
                    ?: run { aircraft }

                "holdPwr" -> value.toIntOrNull()?.let { aircraft.copy(holdPwr = it) }
                    ?: run { aircraft }

                "reset" -> allAircraft.value.first { it.ICAO == aircraft.ICAO }

                else -> aircraft
            }
    }
}

operator fun Float?.plus(other: Float?): Float? =
    if (this != null && other != null) this + other else this ?: other