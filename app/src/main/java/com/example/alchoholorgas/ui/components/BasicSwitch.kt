package com.example.alchoholorgas.ui.components

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun BasicSwitch(onChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    var isEnabled by remember { mutableStateOf(true) }

    Switch(
        checked = isEnabled,
        onCheckedChange = {
            isEnabled = it  // Update isEnabled with the new state
            onChange(it)    // Pass the new state to the callback
        },
        modifier = modifier
    )
}
