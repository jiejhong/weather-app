package com.weatherapp

import com.weatherapp.data.model.WeatherData
import com.weatherapp.data.model.WeatherCodes
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for weather data models
 */
class WeatherModelTest {

    @Test
    fun `weather codes return correct descriptions`() {
        assertEquals("晴朗", WeatherCodes.getDescription(0))
        assertEquals("多云", WeatherCodes.getDescription(2))
        assertEquals("雨", WeatherCodes.getDescription(61))
        assertEquals("雪", WeatherCodes.getDescription(71))
        assertEquals("雷暴", WeatherCodes.getDescription(95))
    }

    @Test
    fun `weather codes return correct emojis`() {
        assertEquals("☀️", WeatherCodes.getEmoji(0))
        assertEquals("🌧️", WeatherCodes.getEmoji(61))
        assertEquals("❄️", WeatherCodes.getEmoji(71))
    }

    @Test
    fun `weather data is created correctly`() {
        val weather = WeatherData(
            cityName = "北京",
            temperature = 25.5,
            humidity = 60,
            windSpeed = 15.0,
            weatherCode = 0,
            description = "晴朗",
            emoji = "☀️"
        )

        assertEquals("北京", weather.cityName)
        assertEquals(25.5, weather.temperature, 0.01)
        assertEquals(60, weather.humidity)
        assertEquals(15.0, weather.windSpeed, 0.01)
        assertEquals(0, weather.weatherCode)
    }
}
