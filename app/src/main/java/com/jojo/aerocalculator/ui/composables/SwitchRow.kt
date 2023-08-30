package com.jojo.aerocalculator.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun SwitchRow(
    modifier: Modifier = Modifier,
    checked: Boolean,
    enabled: Boolean = true,
    unselectedLabel: @Composable () -> Unit,
    selectedLabel: @Composable () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .toggleable(
                checked,
                enabled,
                role = Role.Switch,
                onValueChange = { onCheckedChange(it) })
            .padding(8.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        unselectedLabel()
        Switch(checked, onCheckedChange = null)
        selectedLabel()
    }
}