@echo off
chcp 65001 >nul
setlocal

set "JAVA_HOME=C:\Program Files\JetBrains\GoLand 2024.1.6\jbr"
set "ANDROID_HOME=C:\Android\sdk"
set "PATH=%JAVA_HOME%\bin;%PATH%"

cd /d "D:\workspace\weather-app"
.\gradlew.bat assembleDebug --no-daemon

endlocal
pause
