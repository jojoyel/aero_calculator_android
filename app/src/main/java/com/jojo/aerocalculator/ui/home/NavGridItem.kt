package com.jojo.aerocalculator.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jojo.aerocalculator.R
import com.jojo.aerocalculator.util.Routes


enum class NavGridItem(
    val id: Int,
    @StringRes val stringName: Int,
    @DrawableRes val icon: Int,
    val route: String
) {
    TOD(0, R.string.nav_tod, R.drawable.ic_tod, Routes.TOD.route),
    SLOPE(1, R.string.nav_slope, R.drawable.ic_slope, Routes.Slope.route),
    WIND(2, R.string.nav_wind, R.drawable.ic_air, Routes.Wind.route)
}