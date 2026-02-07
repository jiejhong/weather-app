package com.weatherapp.data.model

/**
 * UI state for weather data
 */
sealed class WeatherUiState {
    data object Loading : WeatherUiState()
    data class Success(val weather: WeatherData) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

/**
 * Combined weather data for UI display
 */
data class WeatherData(
    val cityName: String,
    val temperature: Double,
    val humidity: Int,
    val windSpeed: Double,
    val weatherCode: Int,
    val description: String,
    val emoji: String
)
