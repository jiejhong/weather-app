package com.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.weatherapp.data.model.GeocodingResult
import com.weatherapp.data.model.WeatherData
import com.weatherapp.data.model.WeatherUiState
import com.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for weather screen
 */
class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<GeocodingResult>>(emptyList())
    val searchResults: StateFlow<List<GeocodingResult>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    init {
        // Load default weather (Beijing)
        loadWeatherByCity("北京")
    }

    /**
     * Load weather by city name
     */
    fun loadWeatherByCity(cityName: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            _isSearching.value = true

            // First search for the city
            val searchResult = repository.searchCity(cityName)
            
            if (searchResult.isSuccess) {
                val result = searchResult.getOrNull()
                if (result != null) {
                    // Then get weather for the found coordinates
                    val weatherResult = repository.getWeatherByLocation(
                        latitude = result.latitude,
                        longitude = result.longitude,
                        cityName = result.name
                    )
                    
                    if (weatherResult.isSuccess) {
                        _uiState.value = WeatherUiState.Success(weatherResult.getOrNull()!!)
                    } else {
                        _uiState.value = WeatherUiState.Error(
                            weatherResult.exceptionOrNull()?.message ?: "获取天气失败"
                        )
                    }
                } else {
                    _uiState.value = WeatherUiState.Error("未找到城市: $cityName")
                }
            } else {
                _uiState.value = WeatherUiState.Error(
                    searchResult.exceptionOrNull()?.message ?: "搜索城市失败"
                )
            }
            
            _isSearching.value = false
        }
    }

    /**
     * Update search query
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Handle search action
     */
    fun onSearch() {
        if (_searchQuery.value.isNotBlank()) {
            loadWeatherByCity(_searchQuery.value)
        }
    }

    /**
     * Factory for creating WeatherViewModel with dependencies
     */
    class Factory(
        private val repository: WeatherRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                return WeatherViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
