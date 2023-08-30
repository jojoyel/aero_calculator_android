package com.jojo.aerocalculator.data.models.aircraft

class AircraftRepositoryImpl : AircraftRepository {
    private val aircraftList = listOf(
        Aircraft(
            "DA62",
            "Diamond",
            "Diamond DA-62",
            2,
            0.35f,
            14.8f,
            75,
            9.0f,
            40,
            EngineType.PISTON,
            EngineSpeedType.PERCENT
        ),
        Aircraft(
            "BE58",
            "Beechcraft",
            "Baron 58",
            2,
            .32f,
            13.2f,
            2300,
            13.2f,
            2300,
            EngineType.PISTON,
            EngineSpeedType.RPM
        )
    )

    override suspend fun getAllAircraft(): List<Aircraft> {
        return aircraftList
    }

    override suspend fun getByICAO(ICAO: String): Aircraft? {
        return aircraftList.firstOrNull { it.ICAO == ICAO }
    }
}