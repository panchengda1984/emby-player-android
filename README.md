# Emby Player

一个 Android 平台的 Emby 第三方播放器，支持直连 Emby 服务器进行媒体浏览和播放。

## 功能特性

- ✅ Emby 服务器连接和认证
- ✅ 媒体库浏览（电影、剧集、音乐）
- ✅ 继续观看和最新添加
- ✅ 视频播放（ExoPlayer）
- ⏳ 详情页面
- ⏳ 字幕支持
- ⏳ 播放进度同步
- ⏳ 多服务器管理

## 技术栈

- **语言**: Kotlin
- **UI**: Jetpack Compose + Material3
- **架构**: MVVM + Repository
- **依赖注入**: Hilt
- **网络**: Retrofit + OkHttp
- **播放器**: ExoPlayer (Media3)
- **图片加载**: Coil
- **持久化**: DataStore

## 项目结构

```
app/src/main/java/com/emby/player/
├── data/
│   ├── api/              # Retrofit API 定义
│   ├── model/            # 数据模型
│   └── repository/       # 数据仓库
├── di/                   # Hilt 依赖注入模块
├── ui/
│   ├── navigation/       # 导航配置
│   ├── screen/           # UI 页面
│   │   ├── login/        # 登录页
│   │   └── home/         # 首页
│   └── theme/            # Material3 主题
├── EmbyPlayerApp.kt      # Application 入口
└── MainActivity.kt       # 主 Activity
```

## 快速开始

### 构建要求

- Android Studio Hedgehog | 2023.1.1+
- JDK 17
- Android SDK 34
- Gradle 8.2

### 构建步骤

```bash
# 克隆项目
cd emby-player

# 构建 Debug 版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug
```

## 使用说明

1. 启动应用，进入登录页面
2. 输入 Emby 服务器地址（如 `http://192.168.1.100:8096`）
3. 输入用户名和密码
4. 登录成功后进入主页，浏览媒体库

## Emby API 参考

本项目使用 Emby Server REST API：

- 认证：`POST /Users/AuthenticateByName`
- 获取媒体项：`GET /Users/{userId}/Items`
- 继续观看：`GET /Users/{userId}/Items/Resume`
- 最新添加：`GET /Users/{userId}/Items/Latest`
- 播放信息：`GET /Items/{itemId}/PlaybackInfo`

## 待实现功能

- [x] 详情页面（剧情、演员、评分等）
- [x] 视频播放器集成
- [x] 播放进度上报
- [x] 数据持久化（登录信息）
- [ ] 字幕选择和切换
- [ ] 收藏和评分
- [ ] 搜索功能
- [ ] 多服务器切换
- [ ] 离线下载
- [ ] 设置页面
- [ ] 剧集列表和选集
- [ ] 播放历史记录

## 许可证

MIT License
