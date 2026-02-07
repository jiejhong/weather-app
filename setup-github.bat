@echo off
REM GitHub Automated Setup Script
REM This script will attempt to create repository and push code

echo ==========================================
echo   Vision Weather App - GitHub Setup
echo ==========================================
echo.

REM Try to get GitHub credentials from Windows Credential Manager
echo Checking for saved GitHub credentials...

REM Method 1: Try git credential
echo Please wait...
for /f "tokens=*" %%a in ('git credential fill^|echo protocol=https^&echo host=github.com 2^>nul') do set "%%a"

if defined username (
    echo Found username: %username%
    echo Please provide your GitHub Personal Access Token (with repo scope):
    set /p token="Token: "
) else (
    echo No saved credentials found.
    echo.
    echo Please create a GitHub Personal Access Token:
    echo 1. Go to: https://github.com/settings/tokens
    echo 2. Click "Generate new token (classic)"
    echo 3. Note: Vision Weather App
    echo 4. Select: repo (full control)
    echo 5. Copy the token
    echo.
    set /p token="Enter your GitHub Personal Access Token: "
)

if "%token%"=="" (
    echo Error: No token provided
    pause
    exit /b 1
)

echo.
echo Creating repository 'weather-app'...

REM Create repository via GitHub API
curl -X POST -H "Authorization: token %token%" -d "{\"name\":\"weather-app\",\"description\":\"Vision Weather App - Kotlin Jetpack Compose\",\"public\":true}" https://api.github.com/user/repos -o create-result.json

REM Check if repository was created
if exist create-result.json (
    echo Repository created or already exists!
    
    REM Add remote and push
    cd /d D:\workspace\weather-app
    git remote add origin https://github.com/jie.j.hong/weather-app.git 2>nul
    git push -u origin master
    
    echo.
    echo ==========================================
    echo   Setup Complete!
    echo ==========================================
    echo.
    echo GitHub Actions will now build the APK automatically.
    echo Check: https://github.com/jie.j.hong/weather-app/actions
) else (
    echo Error creating repository
)

pause
