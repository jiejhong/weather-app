package com.weatherapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.weatherapp.data.model.WeatherUiState
import com.weatherapp.ui.components.ErrorMessage
import com.weatherapp.ui.components.SearchBar
import com.weatherapp.ui.components.WeatherCard
import com.weatherapp.ui.viewmodel.WeatherViewModel

/**
 * Main weather screen composable
 */
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // App title
        Text(
            text = "天气预报",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Search bar
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            onSearch = viewModel::onSearch,
            placeholder = "搜索城市（如：北京、上海、广州）"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Current location indicator
        if (uiState is WeatherUiState.Success) {
            val weatherData = (uiState as WeatherUiState.Success).weather
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = weatherData.cityName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Content based on state
        AnimatedContent(
            targetState = uiState,
            transitionSpec = {
                fadeIn() + slideInVertically() togetherWith
                fadeOut() + slideOutVertically()
            },
            modifier = Modifier.fillMaxWidth(),
            label = "weather_content"
        ) { state ->
            when (state) {
                is WeatherUiState.Loading -> {
                    LoadingContent(isSearching = isSearching)
                }
                is WeatherUiState.Success -> {
                    WeatherContent(weather = state.weather)
                }
                is WeatherUiState.Error -> {
                    ErrorContent(
                        message = state.message,
                        onRetry = { viewModel.loadWeatherByCity(searchQuery.ifBlank { "北京" }) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(
    isSearching: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (isSearching) "搜索中..." else "加载天气数据...",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun WeatherContent(
    weather: com.weatherapp.data.model.WeatherData
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main weather card
        WeatherCard(weather = weather)

        Spacer(modifier = Modifier.height(16.dp))

        // Additional info
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "温馨提醒",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = getWeatherTip(weather.weatherCode),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    ErrorMessage(
        message = message,
        onRetry = onRetry
    )
}

/**
 * Get weather tip based on weather code
 */
private fun getWeatherTip(weatherCode: Int): String = when (weatherCode) {
    0 -> "天气晴朗，适合外出活动。"
    in 1..3 -> "多云天气，注意防晒。"
    in 45..48 -> "有雾，出行注意安全。"
    in 51..65, in 80..82 -> "有雨，出门请带伞。"
    in 66..67 -> "有冻雨，路面湿滑，请小心驾驶。"
    in 71..77, in 85..86 -> "有雪，注意保暖。"
    in 95..99 -> "有雷暴，请避免户外活动。"
    else -> "请注意天气变化。"
}
