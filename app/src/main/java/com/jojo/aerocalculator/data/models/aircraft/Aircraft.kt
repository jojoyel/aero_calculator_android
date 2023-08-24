package com.jojo.aerocalculator.data.models.aircraft

data class Aircraft(
    val ICAO: String,
    val manufacturer: String,
    val model: String,
    val enginesNumber: Int,
    val baseFactor: Float,
    val cruiseFF: Float,
    val cruisePwr: Int,
    val holdFF: Float,
    val holdPwr: Int,
    val engineType: EngineType
)

enum class EngineType {
    PISTON, TURBINE
}