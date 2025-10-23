package gl.intergration.pwa

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gl.intergration.pwa.theme.AppTheme
import gl.intergration.pwa.theme.LocalThemeIsDark
import gl.intergration.pwa.webview.WebView
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    val websiteUrl = "https://evctrade.online"
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        // Top App Bar
        /*TopAppBar(
            title = { 
                Text("Evctrade.online")
            },
            actions = {
                var isDark by LocalThemeIsDark.current
                IconButton(onClick = { isDark = !isDark }) {
                    Text(if (isDark) "☀️" else "🌙")
                }
            }
        )*/
        
        // WebView
        WebView(
            url = websiteUrl,
            modifier = Modifier.fillMaxSize(),
            onPageStarted = { url ->
                // Сайт начал загружаться
            },
            onPageFinished = { url ->
                // Сайт загрузился
            },
            onError = { error ->
                // Игнорируем CORS ошибки, так как сайт всё равно работает
                if (!error.contains("ERR_BLOCKED_BY_ORB")) {
                    // Показываем только реальные ошибки
                }
            }
        )
    }
}
