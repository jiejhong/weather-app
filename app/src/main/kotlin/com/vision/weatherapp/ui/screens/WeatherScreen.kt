package com.vision.weatherapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vision.weatherapp.data.model.GeocodingResult
import com.vision.weatherapp.ui.viewmodel.WeatherUiState
import com.vision.weatherapp.ui.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Vision Weather") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 搜索框
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.searchCity(it) },
                onSearch = { focusManager.clearFocus() },
                active = uiState.searchResults.isNotEmpty(),
                onActiveChange = { if (!it) viewModel.searchCity("") },
                placeholder = { Text("搜索城市...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // 搜索结果列表
                LazyColumn {
                    items(uiState.searchResults) { result ->
                        CitySearchItem(
                            result = result,
                            onClick = {
                                viewModel.selectCity(result)
                                focusManager.clearFocus()
                            }
                        )
                    }
                }
            }
            
            // 天气内容
            when {
                uiState.isLoading -> {
                    LoadingContent()
                }
                uiState.errorMessage != null -> {
                    ErrorContent(
                        message = uiState.errorMessage!!,
                        onRetry = {
                            uiState.weatherResponse?.let {
                                viewModel.getWeather(it.latitude, it.longitude)
                            }
                        }
                    )
                }
                uiState.weatherResponse != null -> {
                    WeatherContent(
                        uiState = uiState,
                        onRefresh = {
                            uiState.weatherResponse?.let {
                                viewModel.getWeather(it.latitude, it.longitude)
                            }
                        }
                    )
                }
                else -> {
                    EmptyContent()
                }
            }
        }
    }
}

@Composable
fun CitySearchItem(
    result: GeocodingResult,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(result.name) },
        supportingContent = { Text("${result.country} ${result.admin1 ?: ""}") },
        leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Composable
fun WeatherContent(
    uiState: WeatherUiState,
    onRefresh: () -> Unit
) {
    val weather = uiState.weatherResponse?.currentWeather ?: return
    val cityName = uiState.searchQuery.ifBlank { "当前位置" }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 城市名称
        Text(
            text = cityName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // 时间
        Text(
            text = weather.time,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // 温度
        Text(
            text = "${weather.temperature.toInt()}°C",
            fontSize = 72.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 天气状况
        Text(
            text = getWeatherDescription(weather.weatherCode),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // 详细信息卡片
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                WeatherDetailRow(
                    icon = Icons.Default.Cloud,
                    label = "风速",
                    value = "${weather.windSpeed.toInt()} km/h"
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                WeatherDetailRow(
                    icon = Icons.Default.NorthEast,
                    label = "风向",
                    value = "${getWindDirection(weather.windDirection)}"
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                WeatherDetailRow(
                    icon = Icons.Default.LightMode,
                    label = "白天/夜晚",
                    value = if (weather.isDay == 1) "白天" else "夜晚"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 刷新按钮
        FilledTonalButton(
            onClick = onRefresh
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("刷新")
        }
    }
}

@Composable
fun WeatherDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("正在获取位置和天气...")
        }
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "获取天气失败",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("重试")
            }
        }
    }
}

@Composable
fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "搜索城市查看天气",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/**
 * 根据天气代码获取天气描述
 */
fun getWeatherDescription(code: Int): String {
    return when (code) {
        0 -> "晴朗"
        1, 2, 3 -> "多云"
        45, 48 -> "雾"
        51, 53, 55 -> "小到中雨"
        56, 57 -> "冻雨"
        61, 63, 65 -> "雨"
        66, 67 -> "冻雨"
        71, 73, 75 -> "雪"
        77 -> "阵雪"
        80, 81, 82 -> "阵雨"
        85, 86 -> "阵雪"
        95 -> "雷暴"
        96, 99 -> "雷暴+冰雹"
        else -> "未知"
    }
}

/**
 * 将风向角度转换为文字描述
 */
fun getWindDirection(degrees: Double): String {
    val directions = listOf("北", "东北", "东", "东南", "南", "西南", "西", "西北")
    val index = ((degrees + 22.5) / 45).toInt() % 8
    return directions[index]
}
