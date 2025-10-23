package gl.intergration.pwa.webview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
expect fun WebView(
    url: String,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    onPageStarted: (String) -> Unit = {},
    onPageFinished: (String) -> Unit = {},
    onError: (String) -> Unit = {}
)

data class WebViewState(
    var isLoading: Boolean = false,
    var currentUrl: String = "",
    var canGoBack: Boolean = false,
    var canGoForward: Boolean = false
)

val LocalWebViewState = staticCompositionLocalOf { WebViewState() }
