package com.example.translator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object TranslatorHelper {
    suspend fun translate(text: String, from: String = "en", to: String = "fa"): String {
        return withContext(Dispatchers.IO) {
            try {
                val encodedText = URLEncoder.encode(text, "UTF-8")
                val urlString = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=$from&tl=$to&dt=t&q=$encodedText"
                val url = URL(urlString)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"

                val response = conn.inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(response)
                val sentences = jsonArray.getJSONArray(0)
                var result = ""
                for (i in 0 until sentences.length()) {
                    result += sentences.getJSONArray(i).getString(0)
                }
                result
            } catch (e: Exception) {
                "خطا در ترجمه: ${e.localizedMessage}"
            }
        }
    }
}
