package com.example.alchoholorgas.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.alchoholorgas.SharedPreferencesManager
import com.example.alchoholorgas.data.GasStation
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GasStationList(navController: NavHostController) {
    val context = LocalContext.current
    val preferencesManager: SharedPreferencesManager by remember { mutableStateOf(
        SharedPreferencesManager(context)
    ) }

    var gasStationCounter = remember { mutableStateOf(preferencesManager.loadInt("gas_station_counter", 0)) }
    var gasStationList = remember {
        mutableStateListOf<GasStation>().apply {
            for (i in 1..gasStationCounter.value) {
                add(GasStation(JSONObject(preferencesManager.loadString("gas_station_json_$i", ""))))
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Selecione um posto para ver/editar")
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(gasStationList.toList()) {
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                navController.navigate("gasStationMenu/?id=${it.id}")
                            }
                    ) {
                        Text(
                            text = it.name,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}