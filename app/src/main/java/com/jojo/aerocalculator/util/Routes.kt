package com.jojo.aerocalculator.util

sealed class Routes(val route: String) {

    object Home : Routes("home")
    object TOD : Routes("tod")
    object Slope : Routes("slope")
    object FlightPrep : Routes("flight_prep")

    override fun toString(): String {
        return this.route
    }
}