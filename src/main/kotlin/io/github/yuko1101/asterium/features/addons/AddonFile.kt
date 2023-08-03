package io.github.yuko1101.asterium.features.addons

import com.google.gson.JsonObject
import java.util.jar.JarFile

class AddonFile(val addons: List<AsteriumAddon>, val file: JarFile, val addonFileMeta: JsonObject, val classLoader: AddonClassLoader) {
    val activeAddons = arrayListOf<AsteriumAddon>().apply { addAll(addons) }

    fun unload() {
        classLoader.unload()
    }
}