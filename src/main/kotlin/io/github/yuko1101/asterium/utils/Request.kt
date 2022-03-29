package io.github.yuko1101.asterium.utils

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.apache.commons.io.IOUtils
import java.net.URL

object Request {
    fun request(url: String): String? {
        return try {
            IOUtils.toString(URL(url))
        } catch (e: Exception) {
            null
        }
    }

    fun requestJSON(url: String): JsonElement? {
        return try {
            val res = IOUtils.toString(URL(url))
            JsonParser().parse(res)
        } catch (e: Exception) {
            null
        }
    }
}