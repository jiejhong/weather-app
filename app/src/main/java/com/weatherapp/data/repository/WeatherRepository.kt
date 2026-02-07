package com.weatherapp.data.api

import com.weatherapp.data.model.GeocodingResult
import com.weatherapp.data.model.WeatherData
import com.weatherapp.data.model.WeatherResponse
import com.weatherapp.data.model.WeatherCodes

/**
 * Repository for weather data operations
 */
class WeatherRepository(
    private val apiService: WeatherApiService
) {

    companion object {
        // Open-Meteo API base URL
        const val BASE_URL = "https://api.open-meteo.com/"
    }

    /**
     * Get weather data for given coordinates
     */
    suspend fun getWeatherByLocation(
        latitude: Double,
        longitude: Double,
        cityName: String = "Unknown"
    ): Result<WeatherData> {
        return try {
            val response = apiService.getWeather(latitude, longitude)
            val current = response.current

            if (current != null) {
                Result.success(
                    WeatherData(
                        cityName = cityName,
                        temperature = current.temperature,
                        humidity = current.humidity,
                        windSpeed = current.windSpeed,
                        weatherCode = current.weatherCode,
                        description = WeatherCodes.getDescription(current.weatherCode),
                        emoji = WeatherCodes.getEmoji(current.weatherCode)
                    )
                )
            } else {
                Result.failure(Exception("No weather data available"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Search for cities and return first result with coordinates
     */
    suspend fun searchCity(cityName: String): Result<GeocodingResult?> {
        return try {
            val response = apiService.searchCities(cityName)
            val result = response.results?.firstOrNull()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
