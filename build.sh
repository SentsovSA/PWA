#!/bin/bash

# Скрипт для сборки мобильного приложения Evctrade

echo "🚀 Сборка мобильного приложения Evctrade..."

# Проверяем наличие Gradle Wrapper
if [ ! -f "./gradlew" ]; then
    echo "❌ Gradle Wrapper не найден!"
    exit 1
fi

# Делаем gradlew исполняемым
chmod +x ./gradlew

echo "📱 Сборка Android APK..."
./gradlew :androidApp:assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ Android APK успешно собран!"
    echo "📁 APK файл: androidApp/build/outputs/apk/debug/androidApp-debug.apk"
else
    echo "❌ Ошибка при сборке Android APK"
    exit 1
fi

echo "🍎 Для сборки iOS приложения:"
echo "   1. Откройте iosApp/iosApp.xcodeproj в Xcode"
echo "   2. Выберите устройство или симулятор"
echo "   3. Нажмите Run (⌘+R)"

echo "🖥️  Для запуска Desktop версии:"
echo "   ./gradlew :desktopApp:run"

echo "🎉 Сборка завершена!"
