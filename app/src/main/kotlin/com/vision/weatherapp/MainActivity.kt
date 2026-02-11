package com.vision.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.vision.weatherapp.ui.screens.WeatherScreen
import com.vision.weatherapp.ui.theme.WeatherAppTheme
import com.vision.weatherapp.ui.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    
    private lateinit var viewModel: WeatherViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                getCurrentLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                getCurrentLocation()
            }
            else -> {
                // 用户拒绝权限，使用默认位置并提示
                Toast.makeText(this, "位置权限被拒绝，将使用默认位置", Toast.LENGTH_SHORT).show()
                getDefaultLocation()
            }
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        
        viewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen(viewModel = viewModel)
                }
            }
        }
        
        // 请求位置权限并获取天气
        checkLocationPermission()
    }
    
    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }
            else -> {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
    
    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val cancellationToken = CancellationTokenSource()
        
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationToken.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                // 获取成功，使用实际位置获取天气
                viewModel.getWeather(latitude = location.latitude, longitude = location.longitude)
            } else {
                // 无法获取位置，尝试获取最后已知位置
                getLastKnownLocation()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "获取位置失败: ${e.message}", Toast.LENGTH_SHORT).show()
            getDefaultLocation()
        }
    }
    
    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    viewModel.getWeather(latitude = location.latitude, longitude = location.longitude)
                } else {
                    getDefaultLocation()
                }
            }
            .addOnFailureListener {
                getDefaultLocation()
            }
    }
    
    private fun getDefaultLocation() {
        // 使用默认位置（上海）作为后备
        viewModel.getWeather(latitude = 31.2304, longitude = 121.4737)
    }
}
