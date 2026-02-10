package com.vision.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vision.weatherapp.data.model.GeocodingResult
import com.vision.weatherapp.data.model.WeatherResponse
import com.vision.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 天气 App UI 状态
 */
data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherResponse: WeatherResponse? = null,
    val errorMessage: String? = null,
    val searchResults: List<GeocodingResult> = emptyList(),
    val searchQuery: String = "",
    val isSearching: Boolean = false
)

/**
 * 天气 ViewModel
 */
class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    /**
     * 获取当前位置天气
     */
    fun getWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )
            
            repository.getWeather(latitude, longitude)
                .onSuccess { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weatherResponse = response
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "获取天气失败"
                    )
                }
        }
    }
    
    /**
     * 搜索城市
     */
    fun searchCity(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(
                searchResults = emptyList(),
                searchQuery = ""
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSearching = true,
                searchQuery = query
            )
            
            repository.searchCity(query)
                .onSuccess { results ->
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        searchResults = results
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        isSearching = false,
                        searchResults = emptyList()
                    )
                }
        }
    }
    
    /**
     * 选择城市后获取天气
     */
    fun selectCity(result: GeocodingResult) {
        getWeather(result.latitude, result.longitude)
        _uiState.value = _uiState.value.copy(
            searchResults = emptyList(),
            searchQuery = result.name
        )
    }
    
    /**
     * 清除错误
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
