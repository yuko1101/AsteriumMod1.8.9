package io.github.yuko1101.asterium.features.addons

import io.github.yuko1101.asterium.utils.AddonClassLoader

class AddonCore(val addonMetaDataList: List<AddonMetaData>, val addonClassLoader: AddonClassLoader?) {
    val unloadable = addonClassLoader != null && addonMetaDataList.all { it.unloadable }

    fun unload() {
        if (unloadable) {
            addonClassLoader!!.unload()
            println("[Asterium Addons] Unloaded ${addonClassLoader.loadedClasses}")
        } else {
            throw Exception("This addons group is not unloadable. Addons: ${addonMetaDataList.map { it.name }}")
        }
    }
}