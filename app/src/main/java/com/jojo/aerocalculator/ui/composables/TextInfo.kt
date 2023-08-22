package com.jojo.aerocalculator.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.material3.rememberPlainTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.jojo.aerocalculator.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInfo(
    modifier: Modifier = Modifier,
    text: String,
    description: String,
    fontStyle: FontStyle? = null,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val scope = rememberCoroutineScope()
    val tooltipState = rememberPlainTooltipState()

    Row(
        modifier = Modifier.then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = text, fontStyle = fontStyle, fontSize = fontSize)
        PlainTooltipBox(
            tooltip = { Text(text = description) },
            tooltipState = tooltipState
        ) {
            Icon(
                Icons.Default.Help,
                contentDescription = stringResource(R.string.content_desc_symbol_info),
                modifier = Modifier.clickable(role = Role.Button) { scope.launch { tooltipState.show() } }
            )
        }
    }
}

@Composable
fun VzTextInfo() {
    TextInfo(text = "Vz", description = stringResource(R.string.desc_vz))
}

@Composable
fun DeltaFlTextInfo() {
    TextInfo(
        text = "ΔFL",
        description = stringResource(R.string.desc_deltafl)
    )
}

@Composable
fun FbTextInfo() {
    TextInfo(text = "Fb", description = stringResource(R.string.desc_fb))
}

@Composable
fun VwTextInfo() {
    TextInfo(text = "Vw", description = stringResource(R.string.desc_vw))
}