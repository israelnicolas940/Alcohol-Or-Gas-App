package com.example.alchoholorgas.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

fun isGasBetter(gasPrice: Double, alcoholPrice: Double, utilizationRate: Double): Boolean {
    return (alcoholPrice > utilizationRate * gasPrice)
}

@Composable
fun CalculateButton(
    gasPrice: Double,
    alcoholPrice: Double,
    utilizationRate: Double,
    modifier: Modifier = Modifier,
    onResultChanged: (String) -> Unit // Callback to update the result
) {
    Button(
        onClick = {
            val bestFuel = if (isGasBetter(gasPrice, alcoholPrice, utilizationRate)) {
                "Gasolina"
            } else {
                "Álcool"
            }
            onResultChanged(bestFuel) // Update the result in the parent composable
        },
        modifier = modifier
    ) {
        Text("Calcular")
    }
}
