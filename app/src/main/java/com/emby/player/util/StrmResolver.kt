package com.emby.player.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

object StrmResolver {
    
    suspend fun resolveUrl(url: String, followRedirects: Boolean = true): String = withContext(Dispatchers.IO) {
        if (!url.endsWith(".strm", ignoreCase = true) && !followRedirects) {
            return@withContext url
        }
        
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = false
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.setRequestProperty("User-Agent", "EmbyPlayer/1.0")
            
            connection.connect()
            
            val responseCode = connection.responseCode
            
            // 处理 302 重定向
            if (responseCode in 300..399) {
                val redirectUrl = connection.getHeaderField("Location")
                connection.disconnect()
                return@withContext redirectUrl ?: url
            }
            
            connection.disconnect()
            return@withContext url
            
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext url
        }
    }
    
    fun isStrmFile(url: String): Boolean {
        return url.endsWith(".strm", ignoreCase = true)
    }
}
