package com.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.weatherapp.ui.screens.WeatherScreen
import com.weatherapp.ui.theme.WeatherAppTheme
import com.weatherapp.ui.viewmodel.WeatherViewModel

/**
 * Main Activity for Weather App
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val application = application as WeatherApplication

        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: WeatherViewModel = viewModel(
                        factory = WeatherViewModel.Factory(application.weatherRepository)
                    )
                    WeatherScreen(viewModel = viewModel)
                }
            }
        }
    }
}
