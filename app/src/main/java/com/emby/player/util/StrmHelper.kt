package com.emby.player.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object StrmHelper {
    private val client = OkHttpClient.Builder()
        .followRedirects(false)
        .build()

    suspend fun resolveStrmUrl(strmUrl: String): String = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(strmUrl).build()
            val response = client.newCall(request).execute()
            
            when (response.code) {
                302, 301, 307, 308 -> {
                    response.header("Location") ?: strmUrl
                }
                200 -> strmUrl
                else -> strmUrl
            }
        } catch (e: IOException) {
            strmUrl
        }
    }

    fun isStrmFile(url: String): Boolean {
        return url.endsWith(".strm", ignoreCase = true)
    }
}
