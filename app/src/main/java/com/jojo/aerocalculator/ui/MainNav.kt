package com.jojo.aerocalculator.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jojo.aerocalculator.ui.flight_prep.FlightPrepScreen
import com.jojo.aerocalculator.ui.home.HomeScreen
import com.jojo.aerocalculator.ui.slope.SlopeScreen
import com.jojo.aerocalculator.ui.tod.TODScreen
import com.jojo.aerocalculator.ui.wind.WindScreen
import com.jojo.aerocalculator.util.Routes

@Composable
fun MainNav(navController: NavHostController, paddingValues: PaddingValues) {

    NavHost(
        navController,
        modifier = Modifier.padding(paddingValues),
        startDestination = Routes.Home.route
    ) {
        composable(Routes.Home.route) {
            HomeScreen(navController)
        }
        composable(Routes.FlightPrep.route) {
            FlightPrepScreen()
        }
        composable(Routes.TOD.route) {
            TODScreen(navController)
        }
        composable(Routes.Slope.route) {
            SlopeScreen()
        }
        composable(Routes.Wind.route) {
            WindScreen()
        }
    }
}