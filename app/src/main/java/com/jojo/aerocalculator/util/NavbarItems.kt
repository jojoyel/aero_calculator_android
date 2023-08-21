package com.jojo.aerocalculator.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.jojo.aerocalculator.R

enum class NavbarItems(
    @StringRes val stringName: Int,
    val route: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    HOME(R.string.nav_home, Routes.Home.route, Icons.Default.Home, Icons.Filled.Home)
}