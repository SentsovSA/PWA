# 🚀 Быстрый старт - Evctrade Mobile App

## Что было сделано

✅ **Интегрирован WebView** для отображения сайта Evctrade.online  
✅ **Настроены разрешения Android** для интернета и WebView  
✅ **Создан iOS WebView** с базовой функциональностью  
✅ **Добавлены PWA функции** (офлайн поддержка, кэширование)  
✅ **Настроена конфигурация** приложения (название, иконки, URL)  

## 🎯 Что получилось

Мобильное приложение, которое:
- Отображает сайт Evctrade.online в WebView
- Поддерживает Android и iOS
- Имеет темную/светлую тему
- Показывает статус подключения к интернету
- Кэширует контент для офлайн работы

## 🛠️ Как запустить

### Android (рекомендуется)
```bash
# Быстрая сборка
./build.sh

# Или вручную
./gradlew :androidApp:assembleDebug
```

### iOS
1. Откройте `iosApp/iosApp.xcodeproj` в Xcode
2. Выберите устройство/симулятор
3. Нажмите Run (⌘+R)

### Desktop (для тестирования)
```bash
./gradlew :desktopApp:run
```

## 📱 Результат

После сборки у вас будет:
- **Android APK** в `androidApp/build/outputs/apk/debug/`
- **iOS приложение** готовое к запуску в Xcode
- **Desktop версия** для разработки

## 🔧 Настройка

### Изменить URL сайта
В файле `sharedUI/src/commonMain/kotlin/gl/intergration/pwa/App.kt`:
```kotlin
val websiteUrl = "https://evctrade.online" // Ваш URL
```

### Изменить название приложения
В `androidApp/src/main/AndroidManifest.xml`:
```xml
android:label="Evctrade"
```

## 📋 Требования

- **Android Studio** с Kotlin Multiplatform Mobile plugin
- **Xcode** (для iOS)
- **Java 17+**
- **Android SDK** (для Android)

## 🎉 Готово!

Ваше мобильное приложение для Evctrade.online готово к использованию!
