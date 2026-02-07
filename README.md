# Vision Weather App

一个使用 Kotlin + Jetpack Compose 开发的天气应用程序。

## 功能特点

- 显示实时天气信息（温度、湿度、风速等）
- 支持城市搜索
- 自动获取当前位置天气（默认上海）
- Material Design 3 现代UI设计
- 支持深色模式

## 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose
- **架构**: MVVM
- **网络**: Retrofit + Gson
- **异步**: Coroutines + Flow
- **API**: Open-Meteo (免费，无需API Key)

## 项目结构

```
weather-app/
├── app/
│   ├── src/main/
│   │   ├── kotlin/com/weatherapp/
│   │   │   ├── data/
│   │   │   │   ├── api/WeatherApiService.kt
│   │   │   │   ├── model/WeatherResponse.kt
│   │   │   │   └── repository/WeatherRepository.kt
│   │   │   ├── ui/
│   │   │   │   ├── screens/WeatherScreen.kt
│   │   │   │   ├── theme/Theme.kt
│   │   │   │   └── viewmodel/WeatherViewModel.kt
│   │   │   ├── MainActivity.kt
│   │   │   └── WeatherApplication.kt
│   │   └── res/
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## 快速开始

### 1. 安装依赖

确保已安装：
- JDK 17+
- Android SDK 34+

### 2. 构建项目

```bash
# Windows
gradlew.bat assembleDebug

# Linux/macOS
./gradlew assembleDebug
```

### 3. APK 位置

```
app/build/outputs/apk/debug/app-debug.apk
```

## API

使用 Open-Meteo 免费天气 API：
- 天气: `https://api.open-meteo.com/v1/forecast`
- 城市搜索: `https://geocoding-api.open-meteo.com/v1/search`

## 权限

- INTERNET: 网络访问
- ACCESS_FINE_LOCATION: 位置（可选）

---

**开发者**: Vision AI Assistant  
**日期**: 2026-02-07
