package com.weatherapp.data.api

import com.weatherapp.data.model.GeocodingResponse
import com.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API service for Open-Meteo
 */
interface WeatherApiService {

    /**
     * Get current weather data
     */
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") vararg: String = arrayOf(
            "temperature_2m",
            "relative_humidity_2m",
            "weather_code",
            "wind_speed_10m"
        ),
        @Query("timezone") timezone: String = "auto"
    ): WeatherResponse

    /**
     * Search for cities by name
     */
    @GET("v1/search")
    suspend fun searchCities(
        @Query("name") cityName: String,
        @Query("count") count: Int = 5,
        @Query("language") language: String = "zh",
        @Query("format") format: String = "json"
    ): GeocodingResponse
}
