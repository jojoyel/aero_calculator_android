package com.jojo.aerocalculator.data.models.aircraft

interface AircraftRepository {

    suspend fun getAllAircraft(): List<Aircraft>

    suspend fun getByICAO(ICAO: String): Aircraft?
}