package io.github.yuko1101.asterium.features.addons

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.yuko1101.asterium.Asterium
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

    fun loadClassesInJar(): AddonFile? {
        val result = arrayListOf<AsteriumAddon>()
        val jarFile = JarFile(jarPath)

        val searchPaths = arrayListOf<String>()

        val addonMetaFileData: JsonObject
        val addonMetaFile: ZipEntry? = jarFile.getEntry("asterium.addon.json")
        if (addonMetaFile != null) {
            try {
                val inputStream = jarFile.getInputStream(addonMetaFile)
                val content = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
                val json = JsonParser().parse(content)
                addonMetaFileData = json.asJsonObject
                if (!addonMetaFileData.has("addons")) addonMetaFileData.add("addons", JsonArray())
                val paths = addonMetaFileData["addons"].asJsonArray.map { it.asJsonObject["path"].asString }
                searchPaths.addAll(paths)
            } catch (e: Exception) {
                Asterium.logger.error("Unknown error occurred while reading asterium.addon.json in $jarPath")
                e.printStackTrace()
                return null
            }
        } else {
            addonMetaFileData = JsonObject()
            addonMetaFileData.add("addons", JsonArray())
        }

        addonMetaFileData.addProperty("auto_generated", addonMetaFile == null)

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
            Asterium.logger.info("[Asterium Addons] Searching for $fileName (Extends ${clazz.superclass})")


            // AsteriumAddonの派生型であることを確認
            if (AsteriumAddon::class.java.isAssignableFrom(clazz)) {
                Asterium.logger.info("[Asterium Addons] Searching for $fileName (AsteriumAddon)")

                val constructor =  clazz.getConstructor()
                val instance = constructor.newInstance() as AsteriumAddon

                result.add(instance)
                loadedClasses.add("class " + fileName.substring(0, fileName.length - fileExt.length).replace('/', '.'))

                // アドオン情報のファイルの自動生成
                if (addonMetaFile == null) {
                    val addonData = JsonObject()
                    addonData.addProperty("path", fileName)
                    addonMetaFileData["addons"].asJsonArray.add(addonData)
                }
                Asterium.logger.info("[Asterium Addons] Addon $fileName found!")
            }

        }

        return if (result.isEmpty()) null
        else AddonFile(result.toList(), jarFile, addonMetaFileData, this).also { it.addons.forEach { addon -> addon.addonFile = it } }
    }

    fun unload() {
        urlClassLoader?.close()
        urlClassLoader = null
        System.gc()
    }

}