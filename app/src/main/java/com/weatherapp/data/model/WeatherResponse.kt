package com.weatherapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Response from Open-Meteo Weather API
 */
data class WeatherResponse(
    @SerializedName("latitude")
    val latitude: Double,
    
    @SerializedName("longitude")
    val longitude: Double,
    
    @SerializedName("timezone")
    val timezone: String,
    
    @SerializedName("current")
    val current: CurrentWeather?
)

data class CurrentWeather(
    @SerializedName("time")
    val time: String,
    
    @SerializedName("temperature_2m")
    val temperature: Double,
    
    @SerializedName("relative_humidity_2m")
    val humidity: Int,
    
    @SerializedName("weather_code")
    val weatherCode: Int,
    
    @SerializedName("wind_speed_10m")
    val windSpeed: Double
)

/**
 * Weather code mapping to description
 * Based on WMO Weather interpretation codes
 */
object WeatherCodes {
    fun getDescription(code: Int): String = when (code) {
        0 -> "晴朗"
        1, 2, 3 -> "多云"
        45, 48 -> "雾"
        51, 53, 55 -> "小雨"
        56, 57 -> "冻毛毛雨"
        61, 63, 65 -> "雨"
        66, 67 -> "冻雨"
        71, 73, 75 -> "雪"
        77 -> "雪粒"
        80, 81, 82 -> "阵雨"
        85, 86 -> "阵雪"
        95 -> "雷暴"
        96, 99 -> "雷暴伴有冰雹"
        else -> "未知"
    }

    fun getEmoji(code: Int): String = when (code) {
        0 -> "☀️"
        1, 2, 3 -> "⛅"
        45, 48 -> "🌫️"
        51, 53, 55 -> "🌦️"
        56, 57 -> "🌨️"
        61, 63, 65 -> "🌧️"
        66, 67 -> "🌨️"
        71, 73, 75 -> "❄️"
        77 -> "🌨️"
        80, 81, 82 -> "🌦️"
        85, 86 -> "🌨️"
        95 -> "⛈️"
        96, 99 -> "🌩️"
        else -> "🌡️"
    }
}
