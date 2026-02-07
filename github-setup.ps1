#!/usr/bin/env pwsh
# GitHub Repository Creation and Push Script
# This script will create a GitHub repository and push the code

Write-Host "=== Vision Weather App - GitHub Setup ===" -ForegroundColor Green
Write-Host ""

# Check if gh (GitHub CLI) is installed
$ghInstalled = Get-Command gh -ErrorAction SilentlyContinue
if (-not $ghInstalled) {
    Write-Host "GitHub CLI not found. Trying direct API..." -ForegroundColor Yellow
    
    # Try to use git credential to get token
    Write-Host "Please provide your GitHub Personal Access Token (with repo scope)" -ForegroundColor Cyan
    $token = Read-Host -AsSecureString "Token"
    $tokenStr = [System.Runtime.InteropServices.Marshal]::PtrToStringUni(
        [System.Runtime.InteropServices.Marshal]::SecureStringToGlobalAllocUnicode($token)
    )
    
    # Create repository via API
    Write-Host "Creating repository 'weather-app'..." -ForegroundColor Yellow
    $headers = @{
        "Authorization" = "token $tokenStr"
        "Accept" = "application/vnd.github.v3+json"
    }
    $body = @{
        name = "weather-app"
        description = "Vision Weather App - Kotlin Jetpack Compose"
        auto_init = $false
    } | ConvertTo-Json
    
    try {
        $response = Invoke-RestMethod -Uri "https://api.github.com/user/repos" -Method Post -Headers $headers -Body $body
        Write-Host "Repository created successfully!" -ForegroundColor Green
        Write-Host "URL: $($response.html_url)" -ForegroundColor Cyan
        
        # Add remote and push
        Set-Location -Path "D:\workspace\weather-app"
        git remote add origin $response.clone_url
        git push -u origin master
        Write-Host "Code pushed successfully!" -ForegroundColor Green
        
    } catch {
        Write-Host "Error creating repository: $_" -ForegroundColor Red
        Write-Host $_.Exception.Response  -ForegroundColor Red
    }
}
else {
    # Use GitHub CLI
    Write-Host "Using GitHub CLI..." -ForegroundColor Yellow
    gh repo create weather-app --public --description "Vision Weather App - Kotlin Jetpack Compose"
    Set-Location -Path "D:\workspace\weather-app"
    git remote add origin https://github.com/jie.j.hong/weather-app.git
    git push -u origin master
}

Write-Host ""
Write-Host "Done!" -ForegroundColor Green
