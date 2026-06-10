package com.emby.player.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.InputStreamReader

object StrmParser {
    
    private val client = OkHttpClient.Builder()
        .followRedirects(false)
        .build()

    suspend fun parseStrmUrl(strmUrl: String): String = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(strmUrl).build()
        
        client.newCall(request).execute().use { response ->
            when (response.code) {
                302, 301 -> {
                    response.header("Location") ?: strmUrl
                }
                200 -> {
                    val body = response.body?.string() ?: return@withContext strmUrl
                    body.lines().firstOrNull { it.isNotBlank() && !it.startsWith("#") } ?: strmUrl
                }
                else -> strmUrl
            }
        }
    }

    fun isStrmFile(url: String): Boolean {
        return url.endsWith(".strm", ignoreCase = true)
    }

    suspend fun resolvePlayUrl(url: String): String {
        return if (isStrmFile(url)) {
            parseStrmUrl(url)
        } else {
            url
        }
    }
}
