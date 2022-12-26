package io.github.yuko1101.asterium.utils

import com.google.gson.*
import java.io.File

class ConfigFile(val file: File, val default: JsonObject, private val route: ArrayList<String> = arrayListOf(), private val storedData: JsonObject? = null) {
    var data: JsonObject = default
    fun save(compact: Boolean = true): ConfigFile {
        if (file.parentFile?.exists() == false) file.parentFile?.mkdirs()
        if (compact) file.writeText(data.toString())
        else file.writeText(GsonBuilder().serializeNulls().setPrettyPrinting().create()
            .toJson(data))
        return this
    }
    fun load(): ConfigFile {
        if (!file.exists()) {
            save()
        }
        try {
            data = JsonParser().parse(file.readText()).asJsonObject
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            save() //ファイルを変更前に戻す
        }
        return this
    }

    fun set(key: String, value: JsonElement): ConfigFile {
        getObjectFromPath().add(key, value)
        return this
    }
    fun set(key: String, value: String): ConfigFile {
        getObjectFromPath().addProperty(key, value)
        return this
    }
    fun set(key: String, value: Number): ConfigFile {
        getObjectFromPath().addProperty(key, value)
        return this
    }
    fun set(key: String, value: Boolean): ConfigFile {
        getObjectFromPath().addProperty(key, value)
        return this
    }
    fun set(value: JsonElement): ConfigFile {
        getPreObjectFromPath().add(route.last(), value)
        return this
    }
    fun set(value: String): ConfigFile {
        getPreObjectFromPath().addProperty(route.last(), value)
        return this
    }
    fun set(value: Number): ConfigFile {
        getPreObjectFromPath().addProperty(route.last(), value)
        return this
    }
    fun set(value: Boolean): ConfigFile {
        getPreObjectFromPath().addProperty(route.last(), value)
        return this
    }

    fun getValue(key: String): JsonElement {
        return getObjectFromPath().get(key)
    }
    fun getValue(): JsonElement {
        return getPreObjectFromPath().get(route.last())
    }

    fun get(key: String): ConfigFile {
        val newRoute = arrayListOf<String>()
        newRoute.addAll(route)
        newRoute.add(key)
        return ConfigFile(file, default, newRoute, data)
    }
    fun getPath(path: List<String>): ConfigFile {
        val newRoute = arrayListOf<String>()
        newRoute.addAll(route)
        newRoute.addAll(path)
        return ConfigFile(file, data, newRoute)
    }

    fun has(key: String): Boolean {
        return getObjectFromPath().has(key)
    }

    fun exists(): Boolean {
        return hasPath(route)
    }

    fun resetData(): ConfigFile {
        data = default
        return this
    }

    fun resetPath(): ConfigFile {
        route.clear()
        return this
    }

    private fun getObjectFromPath(): JsonObject {
        //println(route)
        //println(data)
        var obj = data
        for (i in 0 until route.size) {
            obj = obj.get(route[i]).asJsonObject
        }
        return obj
    }

    private fun getPreObjectFromPath(): JsonObject {
        //println(route)
        //println(data)
        var obj = data
        for (i in 0 until route.size - 1) {
            obj = obj.get(route[i]).asJsonObject
        }
        return obj
    }

    private fun hasPath(path: List<String>): Boolean {
        var obj = data
        for (i in path.indices) {
            if (!obj.has(path[i])) return false
            obj = obj.get(path[i]).asJsonObject
        }
        return true
    }

    companion object {
        fun File.asConfigFile(default: JsonObject): ConfigFile {
            return ConfigFile(this, default)
        }
    }
}