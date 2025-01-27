package com.example.alchoholorgas

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

class MainViewModel : ViewModel() {

    // State for dark theme toggle
    private val _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: State<Boolean> = _isDarkTheme

    // State for 75% or 70% toggle
    private val _is75Percent = mutableStateOf(false)
    val is75Percent: State<Boolean> = _is75Percent

    // State for gas price input
    private val _gasPrice = mutableStateOf("")
    val gasPrice: State<String> = _gasPrice

    // State for alcohol price input
    private val _alcoholPrice = mutableStateOf("")
    val alcoholPrice: State<String> = _alcoholPrice

    // State for the best fuel result
    private val _bestFuelResult = mutableStateOf("")
    val bestFuelResult: State<String> = _bestFuelResult

    // State for light and dark theme colors
    private val _lightThemeColors = mutableStateOf(
        ThemeColors(
            surface = 0xFFFFFFFF.toInt(), // White
            onSurface = 0xFF000000.toInt() // Black
        )
    )
    private val _darkThemeColors = mutableStateOf(
        ThemeColors(
            surface = 0xFF000000.toInt(), // Black
            onSurface = 0xFFFFFFFF.toInt() // White
        )
    )

    val lightThemeColors: State<ThemeColors> = _lightThemeColors
    val darkThemeColors: State<ThemeColors> = _darkThemeColors

    // Expose mutable states for input updates
    var isDarkThemeMutable = _isDarkTheme
    var is75PercentMutable = _is75Percent
    var gasPriceMutable = _gasPrice
    var alcoholPriceMutable = _alcoholPrice
    var bestFuelResultMutable = _bestFuelResult
}

// Data class for theme colors
data class ThemeColors(
    val surface: Int,
    val onSurface: Int
)
