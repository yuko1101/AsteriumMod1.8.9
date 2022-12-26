package io.github.yuko1101.asterium.features.addons.hud

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.FeaturedAddon
import io.github.yuko1101.asterium.features.addons.hud.position.ScreenPosition

abstract class HUDFeature {
    abstract val scale: Int
    abstract val name: String
    abstract val addon: FeaturedAddon

    abstract val defaultPosition: ScreenPosition

    fun getPosition(): ScreenPosition {
        val key = "$name-${addon.addonMetaData().uuid}"
        if (!Asterium.hudManager.positionConfig.has(key)) {
            return defaultPosition
        }
        val jsonData = Asterium.hudManager.positionConfig.getValue(key).asJsonObject
        return ScreenPosition.getFromJSON(jsonData)
    }

    fun savePosition() {
        val key = ""
    }

}