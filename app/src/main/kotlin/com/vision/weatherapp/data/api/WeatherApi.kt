package com.vision.weatherapp.data.api

import com.vision.weatherapp.data.model.GeocodingResponse
import com.vision.weatherapp.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Open-Meteo 天气 API 接口
 */
interface WeatherApi {
    
    @GET("v1/forecast")
    suspend fun getWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current_weather") currentWeather: Boolean = true,
        @Query("temperature_unit") temperatureUnit: String = "celsius",
        @Query("windspeed_unit") windSpeedUnit: String = "kmh"
    ): WeatherResponse
    
    @GET("v1/geocoding")
    suspend fun searchCity(
        @Query("name") cityName: String,
        @Query("count") count: Int = 5,
        @Query("language") language: String = "zh",
        @Query("format") format: String = "json"
    ): GeocodingResponse
}
