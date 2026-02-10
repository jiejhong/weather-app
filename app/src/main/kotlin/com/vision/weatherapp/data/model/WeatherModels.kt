package com.vision.weatherapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Open-Meteo Weather API 响应模型
 */
data class WeatherResponse(
    @SerializedName("latitude")
    val latitude: Double,
    
    @SerializedName("longitude")
    val longitude: Double,
    
    @SerializedName("timezone")
    val timezone: String,
    
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather?
)

data class CurrentWeather(
    @SerializedName("temperature")
    val temperature: Double,
    
    @SerializedName("windspeed")
    val windSpeed: Double,
    
    @SerializedName("winddirection")
    val windDirection: Double,
    
    @SerializedName("weathercode")
    val weatherCode: Int,
    
    @SerializedName("is_day")
    val isDay: Int,
    
    @SerializedName("time")
    val time: String
)

/**
 * Open-Meteo Geocoding API 响应模型
 */
data class GeocodingResponse(
    @SerializedName("results")
    val results: List<GeocodingResult>?
)

data class GeocodingResult(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("latitude")
    val latitude: Double,
    
    @SerializedName("longitude")
    val longitude: Double,
    
    @SerializedName("country")
    val country: String,
    
    @SerializedName("admin1")
    val admin1: String?
)
