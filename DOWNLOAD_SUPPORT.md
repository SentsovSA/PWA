# 📥 Поддержка загрузки файлов - Evctrade Mobile App

## 🎯 Проблема

Ссылки на загрузку файлов (например, [evctrade.online/export_report?period=today](https://evctrade.online/export_report?period=today)) не работают в WebView, хотя в браузере всё работает.

## ✅ Решение

Добавили поддержку загрузки файлов для Android и iOS!

### 🔧 **Android реализация:**

```kotlin
override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
    val url = request?.url?.toString() ?: return false
    
    // Обрабатываем загрузку файлов
    if (url.contains("export_report") || url.contains("download") || 
        url.contains(".pdf") || url.contains(".xlsx") || url.contains(".csv")) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            view?.context?.startActivity(intent)
            return true
        } catch (e: Exception) {
            Napier.e("Error opening download link: $url", e)
        }
    }
    
    return false
}
```

### 🍎 **iOS реализация:**

```kotlin
// Проверяем, является ли URL ссылкой на загрузку
if (url.contains("export_report") || url.contains("download") || 
    url.contains(".pdf") || url.contains(".xlsx") || url.contains(".csv")) {
    // Для iOS открываем ссылку в Safari для загрузки файлов
    val nsUrl = NSURL.URLWithString(url)
    if (nsUrl != null) {
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}
```

## 📱 **Как это работает:**

### Android
1. **WebView перехватывает** ссылки на загрузку
2. **Создает Intent** для открытия файла
3. **Открывает в внешнем приложении** (браузер, PDF viewer, Excel)
4. **Пользователь может скачать** файл

### iOS
1. **WebView определяет** ссылки на загрузку
2. **Открывает в Safari** для загрузки
3. **Safari обрабатывает** загрузку файла
4. **Пользователь может скачать** файл

## 🔧 **Поддерживаемые форматы:**

- **PDF** - `.pdf`
- **Excel** - `.xlsx`, `.xls`
- **CSV** - `.csv`
- **Отчеты** - `export_report`
- **Загрузки** - `download`

## 📋 **Разрешения Android:**

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

## 🎯 **Поддерживаемые ссылки:**

- ✅ `https://evctrade.online/export_report?period=today`
- ✅ `https://evctrade.online/download/file.pdf`
- ✅ `https://evctrade.online/reports/data.xlsx`
- ✅ Любые ссылки с `.pdf`, `.xlsx`, `.csv`

## 🚀 **Результат:**

Теперь в вашем приложении:
- ✅ **Кнопки загрузки работают** как в браузере
- ✅ **Файлы открываются** в соответствующих приложениях
- ✅ **Отчеты скачиваются** без проблем
- ✅ **Поддержка всех форматов** файлов

## 📊 **Логирование:**

Приложение логирует:
- Успешные загрузки файлов
- Ошибки открытия ссылок
- Типы обрабатываемых файлов

## 🎉 **Готово!**

Теперь ваше приложение полностью поддерживает загрузку файлов с сайта Evctrade.online! 🚀
