@echo off
REM GitHub 仓库创建脚本
REM 需要 GitHub Personal Access Token

set /p token="Enter your GitHub Personal Access Token: "

echo Creating repository...
curl -X POST -H "Authorization: token %token%" -d "{\"name\":\"weather-app\",\"description\":\"Vision Weather App - Kotlin Jetpack Compose\",\"public\":true}" https://api.github.com/user/repos

echo.
echo Repository created! Now push:
echo git remote add origin https://github.com/jie.j.hong/weather-app.git
echo git push -u origin master
pause
