package com.example.alchoholorgas

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.alchoholorgas.ui.SaveGasStation
import com.example.alchoholorgas.ui.AlcoholOrGasApp
import com.example.alchoholorgas.ui.GasStationList
import com.example.alchoholorgas.utils.LocationHelper
import com.example.alchoholorgas.ui.GasStationMenu

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, proceed with location-related tasks
                println("Location permission granted")
            } else {
                // Permission denied
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show a rationale (e.g., a dialog or toast)
                    Toast.makeText(this, "Location permission is required to find nearby gas stations.", Toast.LENGTH_LONG).show()
                } else {
                    // User selected "Don't ask again" or permanently denied the permission
                    Toast.makeText(this, "Location permission denied. You can enable it in app settings.", Toast.LENGTH_LONG).show()
                }
            }
        }
    @Composable
    private fun ShowRationaleDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss, // Called when the user clicks outside the dialog or presses the back button
            title = {
                Text(text = "Location Permission Needed")
            },
            text = {
                Text("This app needs location permission to find nearby gas stations. Please grant the permission.")
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm // Trigger permission request again
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss // Dismiss the dialog
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    @Composable
    private fun RequestPermissionRationale(
        requestPermissionLauncher: ActivityResultLauncher<String>,
        onDismiss: () -> Unit
    ) {
        var showRationale by remember { mutableStateOf(true) }

        if (showRationale) {
            ShowRationaleDialog(
                onConfirm = {
                    // Request the permission again
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    showRationale = false // Hide the dialog
                },
                onDismiss = {
                    showRationale = false // Hide the dialog
                    onDismiss() // Notify the caller that the dialog was dismissed
                }
            )
        }
    }
    @Composable
    private fun MainContent(
        navController: NavHostController,
        preferencesManager: SharedPreferencesManager,
        isDarkTheme: MutableState<Boolean>,
        is75Percent: MutableState<Boolean>,
        requestPermissionLauncher: ActivityResultLauncher<String>,
        context: Context
    ) {
        var showRationale by remember { mutableStateOf(false) }

        // Check and request permission
        LaunchedEffect(Unit) {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                    println("Permission already granted")
                }
                ActivityCompat.shouldShowRequestPermissionRationale(
                    context as ComponentActivity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    // Show rationale dialog
                    showRationale = true
                }
                else -> {
                    // Request the permission
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }
        }

        // Show rationale dialog if needed
        if (showRationale) {
            RequestPermissionRationale(
                requestPermissionLauncher = requestPermissionLauncher,
                onDismiss = { showRationale = false }
            )
        }

        // Main UI
        AlcoholOrGasTheme(darkTheme = isDarkTheme.value) {
            Surface(tonalElevation = 5.dp) {
                NavHost(navController = navController, startDestination = "alcoholOrGas") {
                    composable("alcoholOrGas") {
                        AlcoholOrGasApp(navController, isDarkTheme, is75Percent)
                    }
                    composable("saveGasStation/?gasPrice={gasPrice}&alcoholPrice={alcoholPrice}") { backStackEntry ->
                        val gasPrice: String = backStackEntry.arguments?.getString("gasPrice") ?: "0.0"
                        val alcoholPrice: String = backStackEntry.arguments?.getString("alcoholPrice") ?: "0.0"
                        SaveGasStation(navController, gasPrice, alcoholPrice)
                    }
                    composable("gasStationList") {
                        GasStationList(navController)
                    }
                    composable("gasStationMenu/?id={gasStationID}") { backStackEntry ->
                        val gasStationID: Int = (backStackEntry.arguments?.getString("gasStationID") ?: "0").toInt()
                        GasStationMenu(navController, gasStationID)
                    }
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val navController: NavHostController = rememberNavController()
            val preferencesManager: SharedPreferencesManager = remember { SharedPreferencesManager(context) }
            var isDarkTheme = remember { mutableStateOf(preferencesManager.loadBoolean("is_dark_theme_checked", false)) }
            var is75Percent = remember { mutableStateOf(preferencesManager.loadBoolean("is_75_percent_checked", false)) }
            MainContent(
                navController = navController,
                preferencesManager = preferencesManager,
                isDarkTheme = isDarkTheme,
                is75Percent = is75Percent,
                requestPermissionLauncher = requestPermissionLauncher,
                context
            )
        }
    }
}
