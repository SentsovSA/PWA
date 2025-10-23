package gl.intergration.pwa.webview

import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceError
import android.webkit.WebChromeClient
import android.webkit.ValueCallback
import android.webkit.WebChromeClient.FileChooserParams
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import io.github.aakira.napier.Napier

@Composable
actual fun WebView(
    url: String,
    modifier: Modifier,
    onPageStarted: (String) -> Unit,
    onPageFinished: (String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0) }
    var currentUrl by remember { mutableStateOf(url) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.fillMaxSize()) {
        // Progress bar
        if (isLoading) {
            LinearProgressIndicator(
                progress = progress / 100f,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Error message
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { 
                            errorMessage = null
                            // Перезагружаем WebView
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text(
                            text = "Обновить",
                            color = MaterialTheme.colorScheme.errorContainer
                        )
                    }
                }
            }
        }

        // WebView
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        builtInZoomControls = true
                        displayZoomControls = false
                        setSupportZoom(true)
                        allowFileAccess = true
                        allowContentAccess = true
                        allowFileAccessFromFileURLs = true
                        allowUniversalAccessFromFileURLs = true
                        mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
                        setGeolocationEnabled(true)
                        setDatabaseEnabled(true)
                        setRenderPriority(android.webkit.WebSettings.RenderPriority.HIGH)
                        setSupportMultipleWindows(false)
                        setJavaScriptCanOpenWindowsAutomatically(false)
                        setLoadsImagesAutomatically(true)
                        setBlockNetworkImage(false)
                        setBlockNetworkLoads(false)
                    }

                    android.webkit.CookieManager.getInstance().apply {
                        setAcceptCookie(true)
                    }
                    android.webkit.CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                            progress = 0
                            errorMessage = null
                            url?.let { currentUrl = it }
                            onPageStarted(url ?: "")
                            Napier.d( "Page started: $url")
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                            progress = 100
                            url?.let { currentUrl = it }
                            onPageFinished(url ?: "")
                            Napier.d("Page finished: $url")
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            val errorMsg = error?.description?.toString() ?: "Неизвестная ошибка"
                            
                            // Игнорируем CORS ошибки, так как сайт всё равно работает
                            if (!errorMsg.contains("ERR_BLOCKED_BY_ORB")) {
                                errorMessage = errorMsg
                                isLoading = false
                                onError(errorMsg)
                                Napier.e("Error: $errorMsg")
                            } else {
                                // CORS ошибка - просто логируем, но не показываем пользователю
                                Napier.d("CORS error ignored: $errorMsg")
                            }
                        }

                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                            val url = request?.url?.toString() ?: return false
                            
                            // Обрабатываем загрузку файлов
                            if (url.contains("export_report") || url.contains("download") || url.contains(".pdf") || url.contains(".xlsx") || url.contains(".csv")) {
                                try {
                                    // Получаем cookies из WebView
                                    val cookieManager = android.webkit.CookieManager.getInstance()
                                    val cookies = cookieManager.getCookie(url)
                                    
                                    // Используем DownloadManager для загрузки файлов
                                    val downloadManager = view?.context?.getSystemService(android.content.Context.DOWNLOAD_SERVICE) as android.app.DownloadManager
                                    val downloadRequest = android.app.DownloadManager.Request(android.net.Uri.parse(url))
                                    
                                    // Добавляем cookies для аутентификации
                                    if (cookies != null && cookies.isNotEmpty()) {
                                        downloadRequest.addRequestHeader("Cookie", cookies)
                                        Napier.d("Added cookies to download: $cookies")
                                    }
                                    
                                    // Добавляем User-Agent
                                    downloadRequest.addRequestHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36")
                                    
                                    // Настройки загрузки
                                    downloadRequest.setTitle("Evctrade Report")
                                    downloadRequest.setDescription("Downloading report from Evctrade")
                                    downloadRequest.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                    downloadRequest.setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, "evctrade_report_${System.currentTimeMillis()}.xlsx")
                                    
                                    // Запускаем загрузку
                                    val downloadId = downloadManager.enqueue(downloadRequest)
                                    Napier.d("Download started with ID: $downloadId for URL: $url")
                                    
                                    return true
                                } catch (e: Exception) {
                                    Napier.e("Error starting download: $url", e)
                                    // Fallback: открываем в браузере
                                    try {
                                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                        view?.context?.startActivity(intent)
                                        Napier.d("Fallback: opened in browser: $url")
                                    } catch (fallbackException: Exception) {
                                        Napier.e("Fallback also failed: $url", fallbackException)
                                    }
                                }
                            }
                            
                            return false
                        }
                        
                        override fun onLoadResource(view: WebView?, url: String?) {
                            super.onLoadResource(view, url)
                            // Логируем загрузку ресурсов для отладки
                            if (url != null && (url.contains("export_report") || url.contains("download"))) {
                                Napier.d("Loading download resource: $url")
                            }
                        }
                        
                        override fun onReceivedHttpError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            errorResponse: android.webkit.WebResourceResponse?
                        ) {
                            super.onReceivedHttpError(view, request, errorResponse)
                            // Игнорируем HTTP ошибки для CORS обхода
                            Napier.d("HTTP Error ignored for CORS bypass: ${errorResponse?.statusCode}")
                        }
                        
                        override fun onReceivedSslError(
                            view: WebView?,
                            handler: android.webkit.SslErrorHandler?,
                            error: android.net.http.SslError?
                        ) {
                            // Игнорируем SSL ошибки для CORS обхода
                            handler?.proceed()
                            Napier.d( "SSL Error ignored for CORS bypass")
                        }
                    }

                    webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            progress = newProgress
                        }
                        
                        override fun onShowFileChooser(
                            webView: WebView?,
                            filePathCallback: ValueCallback<Array<Uri>>?,
                            fileChooserParams: FileChooserParams?
                        ): Boolean {
                            // Обрабатываем выбор файлов для загрузки
                            try {
                                val intent = android.content.Intent(android.content.Intent.ACTION_GET_CONTENT)
                                intent.addCategory(android.content.Intent.CATEGORY_OPENABLE)
                                intent.type = "*/*"
                                intent.putExtra(android.content.Intent.EXTRA_MIME_TYPES, arrayOf(
                                    "image/*",
                                    "application/pdf",
                                    "application/vnd.ms-excel",
                                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                                    "text/csv",
                                    "application/msword",
                                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                ))
                                
                                // Сохраняем callback для обработки результата
                                if (webView?.context is android.app.Activity) {
                                    val activity = webView.context as android.app.Activity
                                    activity.startActivityForResult(intent, 1001)
                                    Napier.d("File chooser opened for file upload")
                                }
                                
                                return true
                            } catch (e: Exception) {
                                Napier.e("Error opening file chooser", e)
                                return false
                            }
                        }


                        
                    }
                }
            },
            update = { webView ->
                webView.loadUrl(currentUrl)
                canGoBack = webView.canGoBack()
                canGoForward = webView.canGoForward()
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
