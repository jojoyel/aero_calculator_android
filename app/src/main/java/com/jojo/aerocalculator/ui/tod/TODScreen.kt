package com.jojo.aerocalculator.ui.tod

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jojo.aerocalculator.ui.composables.SectionCard
import com.jojo.aerocalculator.ui.composables.TextInfo

@Composable
fun TODScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp)) {
        SectionCard(modifier = Modifier.fillMaxWidth()) {
            TextInfo(
                text = "ΔFL",
                description = "The difference between your actual FL and your target FL"
            )
        }
        SectionCard {

        }
    }
}