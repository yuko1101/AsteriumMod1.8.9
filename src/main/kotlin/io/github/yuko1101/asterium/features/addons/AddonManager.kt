package io.github.yuko1101.asterium.features.addons

import io.github.yuko1101.asterium.Asterium
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
        if (addons.isNotEmpty()) unloadExternalAddons()
        loadExternalAddons()

        Asterium.refreshConfig()
    }

    fun unload() {
        unloadExternalAddons()
        Asterium.refreshConfig()
    }


    private fun unloadExternalAddons() {
        addons.forEach { addon -> addon.addonMetaDataList.forEach { it.addon.shutdown() } }
        addons.forEach { if (it.unloadable) it.unload() }
        addons.removeIf { it.unloadable }
        addons.forEach { addon -> println("[Asterium Addons] Couldn't unload ${addon.addonMetaDataList.joinToString { it.name }}") }
    }

    private fun loadExternalAddons() {
        val addonFiles = FileManager.addonsDirectory.listFiles { file -> file.extension == "jar" } ?: return
        addonFiles.forEach { file ->
            if (addons.any { it.addonClassLoader?.jarPath == file.absolutePath }) {
                println("[Asterium Addons] Skipped loading $file because it has already loaded")
            } else {
                println("[Asterium Addons] Loading $file")
                val addonClassLoader = AddonClassLoader(file.absolutePath)
                addons.add(AddonCore(addonClassLoader.loadClassesInJar().map { featuredAddon: FeaturedAddon -> featuredAddon.getAddonMetaData() }, addonClassLoader))
            }
        }
    }
}