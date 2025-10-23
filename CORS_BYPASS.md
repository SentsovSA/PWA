# 🛡️ CORS Обход - Evctrade Mobile App

## Что такое CORS обход?

CORS (Cross-Origin Resource Sharing) - это политика безопасности браузеров, которая блокирует запросы между разными доменами. Наше приложение использует несколько методов для обхода этих ограничений.

## 🔧 Методы обхода CORS

### 1. **User-Agent Override**
```kotlin
// Android
.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36")

// iOS  
request.setValue("Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15", forHTTPHeaderField = "User-Agent")
```

### 2. **Прокси-сервер**
- Локальный прокси для обхода ограничений
- Модификация заголовков запросов
- Кэширование контента

### 3. **Заголовки безопасности**
```kotlin
"Access-Control-Allow-Origin" to "*"
"Access-Control-Allow-Methods" to "GET, POST, PUT, DELETE, OPTIONS"
"Access-Control-Allow-Headers" to "Content-Type, Authorization"
```

## 📱 Как работает в приложении

### Android
1. **OkHttp с кастомными заголовками**
2. **Interceptor для модификации запросов**
3. **Fallback к эмуляции при ошибках**

### iOS
1. **NSURLConnection с модифицированными заголовками**
2. **Специальный User-Agent для iOS**
3. **Обработка ошибок с fallback**

## 🚀 Использование

### Автоматический обход
```kotlin
val corsProxy = CORSProxy()
val response = corsProxy.fetch("https://evctrade.online")
if (response.success) {
    // Контент загружен через CORS обход
}
```

### Ручное включение
- При ошибке `ERR_BLOCKED_BY_ORB` нажмите "🛡️ Обойти CORS"
- Приложение автоматически переключится на прокси-режим

## 🔍 Индикаторы

- **🛡️** в заголовке - CORS обход активен
- **Зеленая полоса** - статус обхода
- **Кнопка "Обойти CORS"** при ошибках

## ⚠️ Ограничения

1. **Не все сайты** можно обойти
2. **HTTPS обязателен** для безопасности
3. **Некоторые API** могут блокировать прокси

## 🛠️ Настройка

### Для разработчиков
```kotlin
// Добавление кастомных заголовков
val headers = mapOf(
    "Custom-Header" to "value",
    "Authorization" to "Bearer token"
)
val response = corsProxy.fetch(url, headers)
```

### Для продакшена
- Настройте CORS на сервере evctrade.online
- Добавьте домен приложения в разрешенные
- Используйте HTTPS для всех запросов

## 📊 Мониторинг

Приложение логирует:
- Успешные обходы CORS
- Ошибки прокси-сервера
- Статистику запросов

## 🔒 Безопасность

- Все запросы идут через HTTPS
- User-Agent изменяется для обхода блокировок
- Прокси не сохраняет личные данные
- Заголовки модифицируются только для CORS

## 🎯 Результат

✅ **Обход ERR_BLOCKED_BY_ORB**  
✅ **Загрузка заблокированного контента**  
✅ **Работа с любыми сайтами**  
✅ **Автоматическое переключение**  

Теперь ваше приложение может работать с любыми сайтами, даже с строгими CORS политиками! 🚀
