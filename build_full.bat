@echo off
chcp 65001 >nul
setlocal

set "JAVA_HOME=C:\Program Files\JetBrains\GoLand 2024.1.6\jbr"
set "ANDROID_HOME=C:\Android"
set "PATH=%JAVA_HOME%\bin;%ANDROID_HOME%\cmdline-tools\cmdline-tools\bin;%PATH%"

echo ===== 安装 Android SDK =====
echo.
echo 1. 接受协议...
yes | sdkmanager --licenses >nul 2>&1

echo 2. 安装 platforms;android-34...
sdkmanager "platforms;android-34" "build-tools;34.0.0"

echo.
echo 3. 创建 local.properties...
echo sdk.dir=%ANDROID_HOME% > "D:\workspace\weather-app\local.properties"

echo.
echo 4. 运行构建...
cd /d "D:\workspace\weather-app"
.\gradlew.bat assembleDebug --no-daemon

endlocal
pause
