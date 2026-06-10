package com.emby.player.player

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

object StrmPlayer {
    
    private val client = OkHttpClient.Builder()
        .followRedirects(false)  // 不自动跟随重定向
        .build()

    /**
     * 解析 STRM 文件，支持：
     * 1. 302 重定向
     * 2. 直连 URL
     */
    fun parseStrmUrl(strmUrl: String): String {
        return try {
            val request = Request.Builder()
                .url(strmUrl)
                .head()
                .build()

            val response = client.newCall(request).execute()

            when (response.code) {
                302, 301, 307, 308 -> {
                    // 处理302重定向
                    response.header("Location") ?: strmUrl
                }
                200 -> {
                    // 直连URL
                    strmUrl
                }
                else -> strmUrl
            }
        } catch (e: IOException) {
            // 网络错误，返回原始URL
            strmUrl
        }
    }

    fun isStrmFile(url: String): Boolean {
        return url.endsWith(".strm", ignoreCase = true) || 
               url.contains("/strm/", ignoreCase = true)
    }
}
