package com.example.alchoholorgas.ui.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier


@Composable
fun PriceBar(label: String, text: String, onTextChange: (String) -> Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = text,
        onValueChange = { onTextChange(it) },  // Use the passed in setter function
        label = { Text(label) },
        modifier = modifier
    )
}