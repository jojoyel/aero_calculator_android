package com.jojo.aerocalculator.ui.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties

@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    label: String,
    data: List<String>,
    enabled: Boolean = true,
    onClicked: (String) -> Unit
) {
    var selected by remember { mutableStateOf(data.firstOrNull() ?: "") }

    var expanded by remember { mutableStateOf(false) }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    Column {
        OutlinedTextField(value = selected, label = { Text(label) }, onValueChange = {},
            interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            expanded = !expanded
                        }
                    }
                }
            },
            modifier = Modifier.onGloballyPositioned { coordinates ->
                textfieldSize = coordinates.size.toSize()
            },
            trailingIcon = {
                IconButton(enabled = enabled, onClick = { expanded = !expanded }) {
                    Icon(
                        icon,
                        if (expanded) "Shrink"
                        else "Expand"
                    )
                }
            },
            enabled = enabled,
            readOnly = true
        )
        DropdownMenu(
            expanded = expanded,
            properties = PopupProperties(),
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            data.forEachIndexed { i, e ->
                DropdownMenuItem(text = {
                    Text(text = e)
                }, onClick = {
                    onClicked(e)
                    selected = e
                    expanded = !expanded
                })
            }
        }
    }
}