package io.github.yuko1101.asterium.features.addons.hud

import com.google.gson.JsonObject
import io.github.yuko1101.asterium.utils.ConfigFile
import java.io.File

class HUDManager {
    private val _features: ArrayList<HUDFeature> = arrayListOf()

    val features: List<HUDFeature>
        get() = _features.toList()

    fun register(feature: HUDFeature) {
        if (!_features.contains(feature)) _features.add(feature)
    }

    fun unregsiter(feature: HUDFeature) {
        _features.remove(feature)
    }

    val hudConfig = ConfigFile(File("./asterium/hud.json"), JsonObject()).load()

}