package com.vision.weatherapp.data.repository

import com.vision.weatherapp.data.api.WeatherApi
import com.vision.weatherapp.data.model.GeocodingResult
import com.vision.weatherapp.data.model.WeatherResponse
import com.vision.weatherapp.data.network.NetworkModule

/**
 * 天气数据 Repository
 */
class WeatherRepository(
    private val api: WeatherApi = NetworkModule.weatherApi
) {
    
    /**
     * 获取天气数据
     */
    suspend fun getWeather(latitude: Double, longitude: Double): Result<WeatherResponse> {
        return try {
            val response = api.getWeather(latitude, longitude)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * 搜索城市
     */
    suspend fun searchCity(cityName: String): Result<List<GeocodingResult>> {
        return try {
            val response = api.searchCity(cityName)
            Result.success(response.results ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
