package com.example.alchoholorgas.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.json.JSONObject

@Composable
fun GasStationMenu(navController: NavHostController, gasStationID: Int) {

    val context = LocalContext.current
    val preferencesManager: SharedPreferencesManager by remember { mutableStateOf(SharedPreferencesManager(context)) }

    val gasStation: GasStation by remember {
        mutableStateOf(
            GasStation(JSONObject(preferencesManager.loadString("gas_station_json_$gasStationID", "")))
        )
    }

    var gasPrice by remember { mutableStateOf(gasStation.gasPrice) }
    var alcoholPrice by remember { mutableStateOf(gasStation.alcoholPrice) }
    var name by remember { mutableStateOf(gasStation.name) }
    var address by remember { mutableStateOf(gasStation.address) }

    var gasStationCounter by remember { mutableStateOf(preferencesManager.loadInt("gas_station_counter", 0)) }

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
            text = gasPrice.toString(),
            onTextChange = {
                gasStation.gasPrice = it.toDoubleOrNull() ?: 0.0
                gasPrice = gasStation.gasPrice
            },
            modifier = Modifier.fillMaxWidth()
        )
        PriceBar(
            label = "Preço do álcool",
            text = alcoholPrice.toString(),
            onTextChange = {
                gasStation.alcoholPrice = it.toDoubleOrNull() ?: 0.0
                alcoholPrice = gasStation.alcoholPrice
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            label = {
                Text("Nome do posto")
            },
            value = name,
            onValueChange = {
                gasStation.name = it
                name = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            label = {
                Text("Endereço do posto")
            },
            value = address,
            onValueChange = {
                gasStation.address = it
                address = it
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val jsonGasStation: JSONObject = gasStation.toJSONObject()

                preferencesManager.saveString("gas_station_json_$gasStationID", jsonGasStation.toString())

                Toast.makeText(context, "Alterações salvas com sucesso!", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        ) {
            Text("Salvar alterações")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.End
            )
        ) {
            FloatingActionButton(
                onClick = {
                    val googleMapsUri = Uri.parse("geo:geo:0,0?q=${gasStation.address}")
                    val googleMapsIntent = Intent(Intent.ACTION_VIEW, googleMapsUri)
                    googleMapsIntent.setPackage("com.google.android.apps.maps")
                    context.startActivity(googleMapsIntent)
                }
            ) {
                Icon(Icons.Filled.LocationOn, "Apagar Posto")
            }

            FloatingActionButton(
                onClick = {
                    if (gasStation.id < gasStationCounter) {
                        // Change the position of the last gas station to the position of the removed gas station.

                        val lastGasStationJSON: JSONObject = JSONObject(preferencesManager.loadString("gas_station_json_$gasStationCounter", ""))
                        lastGasStationJSON.put("id", gasStation.id)
                        preferencesManager.saveString("gas_station_json_${gasStation.id}", lastGasStationJSON.toString())

                        preferencesManager.removePreference("gas_station_json_$gasStationCounter")
                    } else {
                        preferencesManager.removePreference("gas_station_json_${gasStation.id}")
                    }

                    gasStationCounter--
                    preferencesManager.saveInt("gas_station_counter", gasStationCounter)

                    Toast.makeText(context, "Posto removido com sucesso!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.Filled.Delete, "Apagar Posto")
            }
        }
    }
}