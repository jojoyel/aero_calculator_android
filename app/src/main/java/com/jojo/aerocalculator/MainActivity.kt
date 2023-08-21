package com.jojo.aerocalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.jojo.aerocalculator.ui.AeroCalculator
import com.jojo.aerocalculator.ui.theme.AeroCalculatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AeroCalculatorTheme {
                val winSizeClass = calculateWindowSizeClass(activity = this).widthSizeClass

                AeroCalculator(winSizeClass)
            }
        }
    }
}