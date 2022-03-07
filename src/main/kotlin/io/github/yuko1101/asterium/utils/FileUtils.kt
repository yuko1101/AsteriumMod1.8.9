package io.github.yuko1101.asterium.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.awt.Color
import java.io.File
import java.io.IOException

class FileUtils {
    companion object {
        fun getConfig(): JsonObject {
            return JsonParser().parse(FileManager.readFromJson(File(FileManager.getClientDirectory(), "config.json"), "{}")).asJsonObject
        }

        fun File.readAsJson() : JsonElement {
            return JsonParser().parse(FileManager.readFromJson(this, "{}"))
        }

        fun JsonObject.getValue(key: String, default: JsonElement): JsonElement {
            if (!this.has(key)) {
                this.add(key, default)
            }
            return this[key]
        }
        fun JsonObject.getValue(key: String, default: String): String {
            if (!this.has(key)) {
                this.addProperty(key, default)
            }
            return this[key].asString
        }
        fun JsonObject.getValue(key: String, default: Boolean): Boolean {
            if (!this.has(key)) {
                this.addProperty(key, default)
            }
            return this[key].asBoolean
        }
        fun JsonObject.getValue(key: String, default: Char): Char {
            if (!this.has(key)) {
                this.addProperty(key, default)
            }
            return this[key].asCharacter
        }
        fun JsonObject.getValue(key: String, default: Int): Int {
            if (!this.has(key)) {
                this.addProperty(key, default)
            }
            return this[key].asInt
        }


        fun File.getFromJson(key: String, default: JsonElement): JsonElement {
            return readFile(this, key, default)?: default
        }

        fun File.getFromJson(key: String, default: String): String {
            val data = readFile(this, key, default)?: return default
            return data.asString
        }
        fun File.getFromJson(key: String, default: Int): Int {
            val data = readFile(this, key, default)?: return default
            return data.asInt
        }
        fun File.getFromJson(key: String, default: Boolean): Boolean {
            val data = readFile(this, key, default)?: return default
            return data.asBoolean
        }
        fun File.getFromJson(key: String, default: Char): Char {
            val data = readFile(this, key, default)?: return default
            return data.asCharacter
        }
        fun File.getFromJson(key: String, default: ArrayList<String>): ArrayList<String> {
            val data = readFile(this, key, default)?: return default
            val array = arrayListOf<String>()
            data.asJsonArray.forEach { a -> array.add(a.asString)}
            return array
        }

        private fun readFile(file: File, key: String, default: Any): JsonElement? {
            try {
                if (!file.exists()) {
                    file.createNewFile()
                    FileManager.writeToFile(file, "{}")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            return if (file.exists() && file.isFile && file.canRead() && file.canWrite()) {
                val json = file.readAsJson().asJsonObject
                if (!json.has(key)) {
                    when (default) {
                        is JsonElement -> json.add(key, default)
                        is String -> json.addProperty(key, default)
                        is Int -> json.addProperty(key, default)
                        is Boolean -> json.addProperty(key, default)
                        is Char -> json.addProperty(key, default)
                        is ArrayList<*> -> {
                            json.add(key, JsonParser().parse(if (default.size > 0) "[\"" + default.joinToString("\", \"") + "\"]" else "[]"))
                        }
                        else -> return null
                    }
                    FileManager.writeToFile(file, json.toString())
                }
                json[key]
            } else {
                null
            }
        }

        fun File.setToJson(key: String, value: Any): File {
            try {
                if (!this.exists()) {
                    this.createNewFile()
                    FileManager.writeToFile(this, "{}")
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return this
            }
            if (this.exists() && this.isFile && this.canRead() && this.canWrite()) {
                val json = this.readAsJson().asJsonObject
                when (value) {
                    is JsonElement -> json.add(key, value)
                    is String -> json.addProperty(key, value)
                    is Int -> json.addProperty(key, value)
                    is Boolean -> json.addProperty(key, value)
                    is Char -> json.addProperty(key, value)
                    else -> return this
                }
                println(json.toString())
                FileManager.writeToFile(this, json.toString())
            }
            return this
        }

        fun Int.toLocaleString(): String {
            return "%,d".format(this)
        }
        fun Long.toLocaleString(): String {
            return "%,d".format(this)
        }

        fun JsonObject.toColor(): Color {
            return Color(this["red"].asInt, this["green"].asInt, this["blue"].asInt, this["alpha"].asInt)
        }
    }
}