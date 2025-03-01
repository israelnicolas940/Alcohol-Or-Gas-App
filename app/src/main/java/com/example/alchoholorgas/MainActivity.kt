package com.example.alchoholorgas

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val preferencesManager: SharedPreferencesManager = remember { SharedPreferencesManager(context) }
            var isDarkTheme = remember { mutableStateOf(preferencesManager.loadBoolean("is_dark_theme_checked", false)) }
            var is75Percent = remember { mutableStateOf(preferencesManager.loadBoolean("is_75_percent_checked", false)) }
            AlcoholOrGasTheme(darkTheme = isDarkTheme.value) {
                Surface(tonalElevation = 5.dp) {
                    AppCompose(isDarkTheme, is75Percent)
                }
            }
        }
    }
}

@Composable
fun AppCompose(isDarkTheme: MutableState<Boolean>, is75Percent: MutableState<Boolean>) {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current
    val preferencesManager: SharedPreferencesManager = remember { SharedPreferencesManager(context) }

    // Observing state from the ViewModel
    val gasPrice = viewModel.gasPriceMutable.value
    val alcoholPrice = viewModel.alcoholPriceMutable.value
    val bestFuelResult = viewModel.bestFuelResultMutable.value

    val contentDescriptionSwitchTheme = if (isDarkTheme.value) "Tema escuro" else "Tema claro"

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = if (isDarkTheme.value) "Dark Mode" else "Light Mode",
                style = MaterialTheme.typography.bodyLarge,
            )
            Switch(
                checked = isDarkTheme.value,
                onCheckedChange = {
                    isDarkTheme.value = it
                    preferencesManager.saveBoolean("is_dark_theme_checked", it)
                },
                modifier = Modifier.semantics {
                    this.contentDescription = contentDescriptionSwitchTheme
                }
            )
        }

        // App Title
        Text(
            text = "Gasolina ou Álcool?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
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

        val contentDescriptionSwitchUsage = if (is75Percent.value) "75% de aproveitamento" else "70% de aproveitamento"
        // Utilization Rate Toggle
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = if (is75Percent.value) "75%" else "70%",
            )
            Switch(
                checked = is75Percent.value,
                onCheckedChange = {
                    is75Percent.value = it
                    preferencesManager.saveBoolean("is_75_percent_checked", it)
                },
                modifier = Modifier.semantics {
                    this.contentDescription = contentDescriptionSwitchUsage
                }
            )
        }

        // Calculate Button
        CalculateButton(
            gasPrice = gasPrice.toDoubleOrNull() ?: 0.0,
            alcoholPrice = alcoholPrice.toDoubleOrNull() ?: 0.0,
            utilizationRate = if (is75Percent.value) 0.75 else 0.7,
            modifier = Modifier.fillMaxWidth(),
            onResultChanged = { result -> viewModel.bestFuelResultMutable.value = result }
        )

        // Display Best Fuel Result
        if (bestFuelResult.isNotEmpty()) {
            Text(
                text = "Melhor opção: $bestFuelResult",
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}