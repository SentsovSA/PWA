# Evctrade Mobile App

Мобильное приложение для сайта Evctrade.online, построенное на Kotlin Multiplatform Mobile (KMM) с поддержкой Android и iOS.

## Возможности

- 🌐 **WebView интеграция** - отображение сайта Evctrade.online в нативном приложении
- 📱 **Кроссплатформенность** - поддержка Android и iOS
- 🌙 **Темная тема** - переключение между светлой и темной темой
- 📶 **Офлайн поддержка** - базовые PWA функции
- 🔄 **Кэширование** - автоматическое кэширование контента
- 🎨 **Material Design 3** - современный дизайн интерфейса

## Технологии

- **Kotlin Multiplatform Mobile (KMM)**
- **Jetpack Compose Multiplatform**
- **Material Design 3**
- **WebView** для отображения веб-контента
- **Ktor** для HTTP запросов
- **Napier** для логирования

## Сборка и запуск

### Android

1. Откройте проект в Android Studio
2. Запустите конфигурацию Android приложения
3. Или соберите APK:
   ```bash
   ./gradlew :androidApp:assembleDebug
   ```
   APK файл будет в `androidApp/build/outputs/apk/debug/androidApp-debug.apk`

### iOS

1. Откройте `iosApp/iosApp.xcodeproj` в Xcode
2. Запустите стандартную конфигурацию
3. Или используйте [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) для Android Studio

### Desktop (для разработки)

```bash
./gradlew :desktopApp:run
```

С hot reload:
```bash
./gradlew :desktopApp:hotRun --auto
```

## Конфигурация

### Изменение URL сайта

В файле `sharedUI/src/commonMain/kotlin/gl/intergration/pwa/App.kt`:
```kotlin
val websiteUrl = "https://evctrade.online" // Измените на нужный URL
```

### Настройка приложения

- **Название приложения**: `androidApp/src/main/AndroidManifest.xml`
- **Иконки**: `androidApp/src/main/res/mipmap-*/` и `iosApp/iosApp/Assets.xcassets/AppIcon.appiconset/`
- **Package ID**: `androidApp/build.gradle.kts`

## Структура проекта

```
├── androidApp/          # Android приложение
├── iosApp/             # iOS приложение  
├── desktopApp/         # Desktop приложение (для разработки)
├── sharedUI/           # Общий UI код
│   ├── commonMain/     # Общая логика
│   ├── androidMain/    # Android специфичный код
│   └── iosMain/        # iOS специфичный код
└── gradle/             # Конфигурация Gradle
```

## Разработка

### Добавление новых функций

1. **Общий код** - в `sharedUI/src/commonMain/`
2. **Android специфичный** - в `sharedUI/src/androidMain/`
3. **iOS специфичный** - в `sharedUI/src/iosMain/`

### WebView настройки

- **Android**: `sharedUI/src/androidMain/kotlin/gl/intergration/pwa/webview/WebView.android.kt`
- **iOS**: `sharedUI/src/iosMain/kotlin/gl/intergration/pwa/webview/WebView.ios.kt`

### PWA функции

- **Кэширование**: `sharedUI/src/commonMain/kotlin/gl/intergration/pwa/pwa/PWAService.kt`
- **Офлайн поддержка**: интегрирована в основное приложение

## Требования

- **Android**: API 23+ (Android 6.0+)
- **iOS**: iOS 13.0+
- **Kotlin**: 2.2.20+
- **Compose Multiplatform**: 1.9.0+

## Лицензия

Этот проект создан для демонстрации интеграции веб-сайта в мобильное приложение.

