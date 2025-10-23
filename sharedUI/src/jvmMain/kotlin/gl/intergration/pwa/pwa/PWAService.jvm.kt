package gl.intergration.pwa.pwa

actual class PWAService {
    actual suspend fun cacheUrl(url: String): Boolean {
        TODO("Not yet implemented")
    }

    actual suspend fun getCachedContent(url: String): String? {
        TODO("Not yet implemented")
    }

    actual suspend fun clearCache() {
    }

    actual suspend fun isOnline(): Boolean {
        TODO("Not yet implemented")
    }
}