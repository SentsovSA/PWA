# 🍪 Исправление загрузки HTML вместо XLSX - Evctrade Mobile App

## ❌ **Проблема:**

При нажатии на кнопку загрузки отчета скачивался HTML-файл (19.24 kB) вместо XLSX файла.

## 🔍 **Причина:**

Сайт требует аутентификацию через cookies для доступа к файлам. DownloadManager не передавал cookies сессии, поэтому сервер возвращал HTML-страницу вместо файла.

## ✅ **Решение:**

Добавили поддержку cookies и сессий для загрузки файлов.

### 🔧 **Что было добавлено:**

#### 1. **Поддержка cookies в WebView:**
```kotlin
// Включаем поддержку cookies для аутентификации
android.webkit.CookieManager.getInstance().apply {
    setAcceptCookie(true)
    setAcceptThirdPartyCookies(this@apply, true)
}
```

#### 2. **Передача cookies в DownloadManager:**
```kotlin
// Получаем cookies из WebView
val cookieManager = android.webkit.CookieManager.getInstance()
val cookies = cookieManager.getCookie(url)

// Добавляем cookies для аутентификации
if (cookies != null && cookies.isNotEmpty()) {
    downloadRequest.addRequestHeader("Cookie", cookies)
    Napier.d("Added cookies to download: $cookies")
}
```

#### 3. **Добавление User-Agent:**
```kotlin
// Добавляем User-Agent
downloadRequest.addRequestHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36")
```

#### 4. **Правильное расширение файла:**
```kotlin
// Указываем правильное расширение файла
downloadRequest.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "evctrade_report_${System.currentTimeMillis()}.xlsx")
```

## 🎯 **Как это работает:**

1. **Пользователь авторизуется** на сайте в WebView
2. **Cookies сохраняются** в WebView
3. **При загрузке файла** cookies передаются в DownloadManager
4. **Сервер получает** аутентификацию и возвращает XLSX файл
5. **Файл сохраняется** с правильным расширением

## 📊 **Логирование:**

```kotlin
Napier.d("Added cookies to download: $cookies")
Napier.d("Download started with ID: $downloadId for URL: $url")
```

## 🔧 **Настройки cookies:**

- ✅ **`setAcceptCookie(true)`** - принимаем cookies
- ✅ **`setAcceptThirdPartyCookies(true)`** - принимаем сторонние cookies
- ✅ **Передача cookies** в DownloadManager
- ✅ **User-Agent** для совместимости

## 🎉 **Результат:**

Теперь при загрузке отчета:
- ✅ **Скачивается XLSX файл** (не HTML)
- ✅ **Cookies передаются** для аутентификации
- ✅ **Правильное расширение** файла
- ✅ **Совместимость** с сервером

## 🚀 **Готово!**

Проблема с загрузкой HTML вместо XLSX исправлена! Теперь файлы загружаются правильно! 🎉
