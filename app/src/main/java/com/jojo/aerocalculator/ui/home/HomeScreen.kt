@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.jojo.aerocalculator.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jojo.aerocalculator.R
import com.jojo.aerocalculator.util.Routes

@Composable
fun HomeScreen(navController: NavHostController) {
    NavGrid(modifier = Modifier.fillMaxSize()) {
        navController.navigate(it)
    }
}

@Composable
private fun NavGrid(modifier: Modifier = Modifier, onClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(140.dp),
        modifier = Modifier.then(modifier),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedCard(
                onClick = { onClick(Routes.FlightPrep.route) },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .4f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .semantics(true) {},
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                ) {
                    Icon(painterResource(R.drawable.ic_flight_prep), contentDescription = null)
                    Text(stringResource(R.string.nav_flight_prep))
                }
            }
        }
        items(NavGridItem.values(), key = { it.id }) {
            NavGridItem(
                item = it,
                onClick = { onClick(it.route) },
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}

@Composable
private fun NavGridItem(modifier: Modifier = Modifier, item: NavGridItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier.then(modifier),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f / 1f)
                .semantics(mergeDescendants = true) {}
                .padding(16.dp)
        ) {
            Icon(
                painterResource(item.icon),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(.3f)
            )
            Text(
                text = stringResource(item.stringName),
                fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 2,
            )
        }
    }
}