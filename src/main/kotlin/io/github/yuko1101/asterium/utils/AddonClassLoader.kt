package io.github.yuko1101.asterium.utils

import com.google.gson.JsonParser
import io.github.yuko1101.asterium.features.addons.FeaturedAddon
import org.apache.commons.io.IOUtils
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.zip.ZipEntry


class AddonClassLoader(val jarPath: String) {
    var urlClassLoader: URLClassLoader? = URLClassLoader(
        arrayOf<URL>(File(jarPath).toURI().toURL()),
        this.javaClass.classLoader
    )

    var loadedClasses = arrayListOf<String>()

    fun loadClassesInJar(): List<FeaturedAddon> {
        val result = arrayListOf<FeaturedAddon>()
        val jarFile = JarFile(jarPath)

        val searchPaths = arrayListOf<String>()

        val addonMetaFile: ZipEntry? = jarFile.getEntry("asterium.addon.json")
        if (addonMetaFile != null) {
            try {
                val inputStream = jarFile.getInputStream(addonMetaFile)
                val content = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
                val json = JsonParser().parse(content)
                val paths = json.asJsonObject["addons"].asJsonArray.map { it.asJsonObject["path"].asString }
                searchPaths.addAll(paths)
            } catch (e: Exception) {
                println("Unknown error occurred while reading asterium.addon.json in $jarPath")
                e.printStackTrace()
                return result
            }
        }

        val entries: Enumeration<JarEntry> = jarFile.entries()
        while (entries.hasMoreElements()) {

            // ファイル要素に限って（＝ディレクトリをはじいて）スキャン
            val jarEntry: JarEntry = entries.nextElement()
            if (jarEntry.isDirectory) {
                continue
            }

            val fileName = jarEntry.name
            if (!searchPaths.contains(fileName) && !(searchPaths.isEmpty() && fileName.startsWith("asterium/"))) continue


            // classファイルに限定
            val fileExt = ".class"
            if (!fileName.endsWith(fileExt)) {
                continue
            }

//            val clazz: Class<*> = try {
//                urlClassLoader.loadClass(fileName.substring(0, fileName.length - 6).replace('/', '.'))
//            } catch (e: ClassNotFoundException) {
//                continue
//            }


            val clazz = Class.forName(fileName.substring(0, fileName.length - fileExt.length).replace('/', '.'), true, urlClassLoader)
            println("[Asterium Addons] Searching for $fileName (${clazz.superclass}, ${clazz.superclass.toString() == FeaturedAddon::class.toString()}, ${FeaturedAddon::class.java.isAssignableFrom(clazz)})")


            // FeaturedAddonの派生型であることを確認
            if (clazz.superclass.toString() == FeaturedAddon::class.toString()) {
                println("[Asterium Addons] Searching for $fileName (FeaturedAddon)")

                val constructor =  clazz.getConstructor()
                val instance = constructor.newInstance()

                result.add(instance as FeaturedAddon)
                loadedClasses.add("class " + fileName.substring(0, fileName.length - fileExt.length).replace('/', '.'))
                println("[Asterium Addons] Addon $fileName found!")
            }

        }
        return result
    }

    fun unload() {
        urlClassLoader?.close()
        urlClassLoader = null
        System.gc()
    }

}