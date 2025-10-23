package gl.intergration.pwa.webview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import io.github.aakira.napier.Napier
import platform.UIKit.*
import platform.WebKit.*
import platform.Foundation.*

@Composable
actual fun WebView(
    url: String,
    modifier: Modifier,
    onPageStarted: (String) -> Unit,
    onPageFinished: (String) -> Unit,
    onError: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(0) }
    var currentUrl by remember { mutableStateOf(url) }
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
                Text(
                    text = "Ошибка загрузки: $error",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // WebView для iOS
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Загрузка Evctrade.online...")
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Evctrade.online",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Мобильное приложение",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { 
                            // В реальном приложении здесь будет открытие в Safari
                            Napier.d( "Opening $currentUrl in Safari")
                        }
                    ) {
                        Text("Открыть в Safari")
                    }
                }
            }
        }
    }

    LaunchedEffect(url) {
        currentUrl = url
        isLoading = true
        progress = 0
        errorMessage = null
        onPageStarted(url)
        
        try {
            // Проверяем, является ли URL ссылкой на загрузку
            if (url.contains("export_report") || url.contains("download") || url.contains(".pdf") || url.contains(".xlsx") || url.contains(".csv")) {
                // Для iOS открываем ссылку в Safari для загрузки файлов
                val nsUrl = NSURL.URLWithString(url)
                if (nsUrl != null) {
                    platform.UIKit.UIApplication.sharedApplication.openURL(nsUrl)
                    Napier.d("iOS", "Opening download link in Safari: $url")
                }
            }
            
            // Симуляция загрузки
            kotlinx.coroutines.delay(1000)
            progress = 50
            kotlinx.coroutines.delay(1000)
            progress = 100
            isLoading = false
            onPageFinished(url)
        } catch (e: Exception) {
            errorMessage = e.message ?: "Неизвестная ошибка"
            isLoading = false
            onError(errorMessage!!)
        }
    }
}
