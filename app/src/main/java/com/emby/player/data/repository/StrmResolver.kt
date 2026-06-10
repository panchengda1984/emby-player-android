package com.emby.player.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StrmResolver @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    suspend fun resolveUrl(url: String): String = withContext(Dispatchers.IO) {
        if (url.endsWith(".strm", ignoreCase = true)) {
            // STRM 文件，读取内容
            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()
            response.body?.string()?.trim() ?: url
        } else if (url.contains("redirect", ignoreCase = true) || url.contains("302")) {
            // 302 重定向，跟随重定向获取真实地址
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "EmbyPlayer/1.0")
                .build()
            val response = okHttpClient.newCall(request).execute()
            response.request.url.toString()
        } else {
            url
        }
    }
}
