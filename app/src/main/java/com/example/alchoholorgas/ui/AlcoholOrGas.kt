package com.example.alchoholorgas.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.alchoholorgas.MainViewModel
import com.example.alchoholorgas.R
import com.example.alchoholorgas.SharedPreferencesManager
import com.example.alchoholorgas.ui.components.CalculateButton
import com.example.alchoholorgas.ui.components.PriceBar

@Composable
fun AlcoholOrGasApp(navController: NavHostController, isDarkTheme: MutableState<Boolean>, is75Percent: MutableState<Boolean>) {
    val viewModel: MainViewModel = viewModel()
    val context = LocalContext.current
    val preferencesManager: SharedPreferencesManager = remember { SharedPreferencesManager(context) }

    // Observing state from the ViewModel
    val gasPrice = viewModel.gasPriceMutable.value
    val alcoholPrice = viewModel.alcoholPriceMutable.value
    val bestFuelResult = viewModel.bestFuelResultMutable.value

    val contentDescriptionSwitchTheme = if (isDarkTheme.value) stringResource(R.string.dark_theme) else stringResource(
        R.string.clear_theme
    )

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
                text = if (isDarkTheme.value) stringResource(R.string.dark_theme) else stringResource(R.string.clear_theme),
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
            text = stringResource(R.string.gas_or_alcohol),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Input Price Bars
        PriceBar(
            label = stringResource(R.string.gas),
            text = gasPrice,
            onTextChange = { viewModel.gasPriceMutable.value = it },
            modifier = Modifier.fillMaxWidth()
        )
        PriceBar(
            label = stringResource(R.string.alcohol),
            text = alcoholPrice,
            onTextChange = { viewModel.alcoholPriceMutable.value = it },
            modifier = Modifier.fillMaxWidth()
        )

        val contentDescriptionSwitchUsage = if (is75Percent.value) stringResource(R.string.utilization_75) else stringResource(
            R.string.utilization_70
        )
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

        // Display Best Fuel Result
        if (bestFuelResult.isNotEmpty()) {
            Text(
                text = stringResource(R.string.best_option, bestFuelResult),
                modifier = Modifier.padding(top = 16.dp)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.End
            )
        ) {
            // Save gas station button
            FloatingActionButton(
                onClick = {
                    val gasPriceDouble: Double = gasPrice.toDoubleOrNull() ?: 0.0
                    val alcoholPriceDouble: Double = alcoholPrice.toDoubleOrNull() ?: 0.0
                    navController.navigate("saveGasStation/?gasPrice=$gasPriceDouble&alcoholPrice=$alcoholPriceDouble")
                }
            ) {
                Icon(Icons.Filled.Add, stringResource(R.string.insert_station))
            }

            // List gas stations button
            FloatingActionButton(
                onClick = {
                    navController.navigate("gasStationList")
                }
            ) {
                Icon(Icons.Filled.List, stringResource(R.string.list_stations))
            }
        }
    }
}