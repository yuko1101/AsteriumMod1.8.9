package io.github.yuko1101.asterium.utils

import com.google.gson.*
import java.io.File

open class ConfigFile(val file: File, val default: JsonObject, private val route: ArrayList<String> = arrayListOf()) {
    open var data: JsonObject = default
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
        getObjectFromRoute().add(key, value)
        return this
    }
    fun set(key: String, value: String): ConfigFile {
        getObjectFromRoute().addProperty(key, value)
        return this
    }
    fun set(key: String, value: Number): ConfigFile {
        getObjectFromRoute().addProperty(key, value)
        return this
    }
    fun set(key: String, value: Boolean): ConfigFile {
        getObjectFromRoute().addProperty(key, value)
        return this
    }
    fun set(value: JsonElement): ConfigFile {
        getPreObjectFromRoute().add(route.last(), value)
        return this
    }
    fun set(value: String): ConfigFile {
        getPreObjectFromRoute().addProperty(route.last(), value)
        return this
    }
    fun set(value: Number): ConfigFile {
        getPreObjectFromRoute().addProperty(route.last(), value)
        return this
    }
    fun set(value: Boolean): ConfigFile {
        getPreObjectFromRoute().addProperty(route.last(), value)
        return this
    }

    fun getValue(key: String): JsonElement {
        return getObjectFromRoute().get(key)
    }
    fun getValue(): JsonElement {
        return getPreObjectFromRoute().get(route.last())
    }

    open fun get(key: String): PathResolver {
        val newRoute = arrayListOf<String>()
        newRoute.addAll(route)
        newRoute.add(key)
        return PathResolver(this, newRoute)
    }
    open fun getPath(path: List<String>): PathResolver {
        val newRoute = arrayListOf<String>()
        newRoute.addAll(route)
        newRoute.addAll(path)
        return PathResolver(this, newRoute)
    }

    fun has(key: String): Boolean {
        return getObjectFromRoute().has(key)
    }

    fun hasPath(path: List<String>): Boolean {
        var obj: JsonElement = getObjectFromRoute()
        for (i in path.indices) {
            if (!obj.isJsonObject) return false
            if (!obj.asJsonObject.has(path[i])) return false
            obj = obj.asJsonObject.get(path[i])
        }
        return true
    }

    fun exists(): Boolean {
        return hasPathInData(route)
    }

    fun resetData(): ConfigFile {
        data = default
        return this
    }

    fun resetPath(): ConfigFile {
        route.clear()
        return this
    }

    private fun getObjectFromRoute(): JsonObject {
//        println(route)
//        println(data)
        var obj = data
        for (i in 0 until route.size) {
            obj = obj.get(route[i]).asJsonObject
        }
        return obj
    }

    private fun getPreObjectFromRoute(): JsonObject {
        //println(route)
        //println(data)
        var obj = data
        for (i in 0 until route.size - 1) {
            obj = obj.get(route[i]).asJsonObject
        }
        return obj
    }

    private fun hasPathInData(path: List<String>): Boolean {
        var obj = data
        for (i in path.indices) {
            if (!obj.has(path[i])) return false
            obj = obj.get(path[i]).asJsonObject
        }
        return true
    }

    class PathResolver(private val configFile: ConfigFile, val route: ArrayList<String>) : ConfigFile(configFile.file, configFile.default, route) {
        override var data: JsonObject
            get() = configFile.data
            set(value) {
                configFile.data = value
            }

        override fun get(key: String): PathResolver {
            val newRoute = arrayListOf<String>()
            newRoute.addAll(route)
            newRoute.add(key)
            return PathResolver(configFile, newRoute)
        }

        override fun getPath(path: List<String>): PathResolver {
            val newRoute = arrayListOf<String>()
            newRoute.addAll(route)
            newRoute.addAll(path)
            return PathResolver(configFile, newRoute)
        }
    }

    companion object {
        fun File.asConfigFile(default: JsonObject): ConfigFile {
            return ConfigFile(this, default)
        }
    }
}