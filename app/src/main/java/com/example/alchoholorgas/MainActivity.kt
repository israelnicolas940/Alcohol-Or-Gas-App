package com.example.alchoholorgas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import com.example.alchoholorgas.ui.components.PriceBar
import com.example.alchoholorgas.ui.theme.AlcoholOrGasTheme
import com.example.alchoholorgas.ui.components.CalculateButton
import androidx.compose.runtime.Composable
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlcoholOrGasTheme {
                Surface(tonalElevation = 5.dp) {
                    AppCompose()
                }
            }
        }
    }
}

@Preview
@Composable
fun AppCompose() {
    val viewModel: MainViewModel = viewModel()
    // Observing state from the ViewModel
    val is75Percent = viewModel.is75PercentMutable.value
    val gasPrice = viewModel.gasPriceMutable.value
    val alcoholPrice = viewModel.alcoholPriceMutable.value
    val bestFuelResult = viewModel.bestFuelResultMutable.value
    val isDarkTheme = viewModel.isDarkThemeMutable.value

    // Get current theme colors
    val themeColors = if (isDarkTheme) viewModel.darkThemeColors.value else viewModel.lightThemeColors.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(themeColors.surface))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Theme Toggle Switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = if (isDarkTheme) "Dark Mode" else "Light Mode",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(themeColors.onSurface)
            )
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { viewModel.isDarkThemeMutable.value = it }
            )
        }

        // App Title
        Text(
            text = "Gasolina ou Álcool?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(themeColors.onSurface),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Input Price Bars
        PriceBar(
            label = "Gasolina",
            text = gasPrice,
            onTextChange = { viewModel.gasPriceMutable.value = it },
            modifier = Modifier.fillMaxWidth()
        )
        PriceBar(
            label = "Álcool",
            text = alcoholPrice,
            onTextChange = { viewModel.alcoholPriceMutable.value = it },
            modifier = Modifier.fillMaxWidth()
        )

        // Utilization Rate Toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = if (is75Percent) "75%" else "70%",
                color = Color(themeColors.onSurface)
            )
            Switch(
                checked = is75Percent,
                onCheckedChange = { viewModel.is75PercentMutable.value = it }
            )
        }

        // Calculate Button
        CalculateButton(
            gasPrice = gasPrice.toDoubleOrNull() ?: 0.0,
            alcoholPrice = alcoholPrice.toDoubleOrNull() ?: 0.0,
            utilizationRate = if (is75Percent) 0.75 else 0.7,
            modifier = Modifier.fillMaxWidth(),
            onResultChanged = { result -> viewModel.bestFuelResultMutable.value = result }
        )

        // Display Best Fuel Result
        if (bestFuelResult.isNotEmpty()) {
            Text(
                text = "Melhor opção: $bestFuelResult",
                color = Color(themeColors.onSurface),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

