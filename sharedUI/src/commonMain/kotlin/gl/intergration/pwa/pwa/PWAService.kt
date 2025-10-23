package gl.intergration.pwa.pwa

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PWACache(
    val url: String,
    val content: String,
    val timestamp: Long,
    val version: String = "1.0"
)

expect class PWAService {
    suspend fun cacheUrl(url: String): Boolean
    suspend fun getCachedContent(url: String): String?
    suspend fun clearCache()
    suspend fun isOnline(): Boolean
}
