package com.example.alchoholorgas.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.alchoholorgas.SharedPreferencesManager
import com.example.alchoholorgas.data.GasStation
import com.example.alchoholorgas.ui.components.PriceBar
import com.example.alchoholorgas.utils.LocationHelper
import org.json.JSONObject
@Composable
fun SaveGasStation(navController: NavHostController, gasPriceString: String, alcoholPriceString: String) {
    val context = LocalContext.current
    val preferencesManager: SharedPreferencesManager by remember { mutableStateOf(SharedPreferencesManager(context)) }
    val locationHelper = remember { LocationHelper(context) }

    var gasPrice by remember { mutableStateOf(gasPriceString) }
    var alcoholPrice by remember { mutableStateOf(alcoholPriceString) }
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gasStationCounter by remember { mutableStateOf(preferencesManager.loadInt("gas_station_counter", 0)) }

    // Fetch user's current location when the composable is launched
    LaunchedEffect(Unit) {
        locationHelper.getCurrentLocation(
            onSuccess = { latLng ->
                address = "Lat: ${latLng.latitude}, Lng: ${latLng.longitude}"
            },
            onFailure = { exception ->
                Toast.makeText(context, "Failed to get location: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        )
    ) {
        PriceBar(
            label = "Preço da gasolina",
            text = gasPrice,
            onTextChange = { gasPrice = it },
            modifier = Modifier.fillMaxWidth()
        )
        PriceBar(
            label = "Preço do álcool",
            text = alcoholPrice,
            onTextChange = { alcoholPrice = it },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            label = { Text("Nome do posto") },
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            label = { Text("Endereço do posto") },
            value = address,
            onValueChange = { address = it },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                Toast.makeText(context, "Salvando posto...", Toast.LENGTH_SHORT).show()

                gasStationCounter++
                val jsonGasStation: JSONObject = GasStation(
                    gasStationCounter,
                    name,
                    gasPrice.toDoubleOrNull() ?: 0.0,
                    alcoholPrice.toDoubleOrNull() ?: 0.0,
                    address
                ).toJSONObject()

                preferencesManager.saveString("gas_station_json_$gasStationCounter", jsonGasStation.toString())
                preferencesManager.saveInt("gas_station_counter", gasStationCounter)

                Toast.makeText(context, "Posto salvo com sucesso!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        ) {
            Text("Salvar posto")
        }
    }
}