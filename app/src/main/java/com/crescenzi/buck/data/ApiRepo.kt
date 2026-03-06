package com.crescenzi.buck.data

import com.crescenzi.buck.core.LOG
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

class ApiRepo(private val apiKey:String) {

    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    // == SYNCHRONOUS STREAMING CALL - MUST BE CALLED OFF THE MAIN THREAD == //
    fun call(text: String, onToken: (String) -> Unit) {

        val json = """
            {
              "model": "command-a-03-2025",
              "stream": true,
              "messages": [{"role": "user", "content": "Riformatta breve e coinciso questo testo = $text"}]
            }
        """.trimIndent()

        val request = Request.Builder()
            .url("https://api.cohere.ai/v2/chat")
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(json.toRequestBody("application/json".toMediaType()))
            .build()

        LOG("API CALL START")
        try {
            val response = client.newCall(request).execute()
            LOG("API RESPONSE CODE: ${response.code}")
            response.use { res ->
                val body = res.body
                if (body == null) {
                    LOG("API BODY IS NULL")
                    return
                }
                BufferedReader(InputStreamReader(body.byteStream())).use { r ->
                    var l: String?
                    while (r.readLine().also { l = it } != null) {
                        val parsed = parseCohereLine(l!!) ?: continue
                        onToken(parsed)
                    }
                }
            }
            LOG("API CALL DONE")
        } catch (e: Exception) {
            LOG("API EXCEPTION: ${e.stackTraceToString()}")
        }
    }

    // == PARSE A SINGLE SSE LINE FROM COHERE STREAMING RESPONSE == //
    private fun parseCohereLine(line: String): String? {
        if (!line.startsWith("data: ")) return null
        return try {
            val jsonObject = JSONObject(line.removePrefix("data: ").trim())
            val content = jsonObject
                .optJSONObject("delta")
                ?.optJSONObject("message")
                ?.optJSONObject("content")
            content?.optString("text")
        } catch (_: Exception) {
            null
        }
    }
}
