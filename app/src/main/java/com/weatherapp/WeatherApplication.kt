package com.weatherapp

import android.app.Application
import com.weatherapp.di.NetworkModule

/**
 * Application class for Weather App
 */
class WeatherApplication : Application() {

    // Lazy initialization of repository
    val weatherRepository by lazy {
        NetworkModule.weatherRepository
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: WeatherApplication
            private set
    }
}
