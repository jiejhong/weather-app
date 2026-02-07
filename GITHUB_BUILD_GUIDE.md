# Weather App - GitHub 构建指南

## 快速开始

### 1. 创建 GitHub 仓库
1. 访问 https://github.com/new
2. Repository name: `weather-app`
3. 选择 Public 或 Private
4. 不要初始化 README（我们已经有了）

### 2. 上传项目
```bash
cd D:\workspace\weather-app
git init
git add .
git commit -m "Initial commit: Weather App"
git remote add origin https://github.com/YOUR_USERNAME/weather-app.git
git push -u origin main
```

### 3. 自动构建
- 推送代码后，GitHub Actions 会自动运行
- 构建完成后，在 Actions 页面下载 APK
- 或者查看 Artifacts 部分

### 4. 下载 APK
1. 进入项目的 Actions 页面
2. 点击最新的 build 工作流
3. 在 Artifacts 部分下载 `weather-app-debug`
4. 解压得到 `app-debug.apk`

## APK 位置
构建成功后 APK 在：
```
app/build/outputs/apk/debug/app-debug.apk
```

## 复制到指定位置
```cmd
copy app\build\outputs\apk\debug\app-debug.apk D:\workspace\weather-app.apk
```
