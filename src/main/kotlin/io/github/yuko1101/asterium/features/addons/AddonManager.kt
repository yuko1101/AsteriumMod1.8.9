package io.github.yuko1101.asterium.features.addons

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.arrowPath.ArrowPath
import io.github.yuko1101.asterium.utils.AddonClassLoader
import io.github.yuko1101.asterium.utils.FileManager

object AddonManager {
    var addons = arrayListOf<AddonCore>()

    fun getAddonMetaDataList(): List<AddonMetaData> {
        val addonMetaDataList = arrayListOf<AddonMetaData>()
        for (addon in addons) {
            addonMetaDataList.addAll(addon.addonMetaDataList)
        }
        return addonMetaDataList
    }

    fun refreshAddons() {
        unloadExternalAddons()
        loadExternalAddons()
        addons.add(AddonCore(listOf(ArrowPath().addonMetaData()), null))

        Asterium.refreshConfig()
    }

    fun unload() {
        unloadExternalAddons()
        Asterium.refreshConfig()
    }


    private fun unloadExternalAddons() {
        addons.forEach { addon -> addon.addonMetaDataList.forEach { it.addon.shutdown() } }
        addons.filter { addon -> shouldUnload(addon) }.forEach { addon ->
            addon.addonClassLoader?.unload()
            addon.addonClassLoader?.let { println("[Asterium Addons] Unloaded ${addon.addonClassLoader.loadedClasses}")}
        }
        addons.removeIf { it.addonClassLoader != null && it.addonClassLoader.urlClassLoader == null }
        addons.forEach { addon -> println("[Asterium Addons] Couldn't unload ${addon.addonMetaDataList.joinToString { it.name }}") }
    }

    private fun shouldUnload(addonCore: AddonCore): Boolean {
        return addonCore.addonMetaDataList.all { addonMetaData -> addonMetaData.unloadable }
    }


    private fun loadExternalAddons() {
        val addonFiles = FileManager.getAddonsDirectory().listFiles { file -> file.extension == "jar" } ?: return
        addonFiles.forEach { file ->
            if (addons.any { it.addonClassLoader?.jarPath == file.absolutePath }) {
                println("[Asterium Addons] Skipped loading $file because it has already loaded")
            } else {
                println("[Asterium Addons] Loading $file")
                val addonClassLoader = AddonClassLoader(file.absolutePath)
                addons.add(AddonCore(addonClassLoader.loadClassesInJar().map { featuredAddon: FeaturedAddon -> featuredAddon.addonMetaData() }, addonClassLoader))
            }

//            val load :URLClassLoader = URLClassLoader.newInstance(arrayOf<URL>(file.toURI().toURL()))
//            val cl = load.loadClass("asterium.${file.nameWithoutExtension.split("-").first()
//                .lowercase(Locale.getDefault())}.${file.nameWithoutExtension.split("-").first()}")
//            println("[Asterium Addons] Loading File from $file / asterium.${file.nameWithoutExtension.split("-").first()
//                .lowercase(Locale.getDefault())}.${file.nameWithoutExtension.split("-").first()}")
//            if (cl != null) {
//                println("Found!")
//                println(cl)
//                if () {
//                    println("Added!")
//                    addons.add(cl.addonMetaData())
//                }
//            }
        }
    }
}