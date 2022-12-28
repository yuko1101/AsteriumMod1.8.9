package io.github.yuko1101.asterium.features.addons.hud

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.hud.position.AbsoluteScreenPosition
import io.github.yuko1101.asterium.features.addons.hud.position.ScreenPosition
import kotlin.math.max

abstract class DraggableFeature : HUDFeature() {
    open val defaultPosition: ScreenPosition = AbsoluteScreenPosition(0F, 0F)
    open val defaultScale: Int = 50

    var scale: Int
        get() {
            if (!Asterium.hudManager.hudConfig.has(key)) {
                return defaultScale
            }
            return Asterium.hudManager.hudConfig.get(key).getValue("scale").asInt
        }
        set(value) {
            Asterium.hudManager.hudConfig.get(key).set("scale", value)
        }

    var position: ScreenPosition
        get() {
            if (!Asterium.hudManager.hudConfig.has(key)) {
                return defaultPosition
            }
            val jsonData = Asterium.hudManager.hudConfig.get(key).getValue("position").asJsonObject
            return ScreenPosition.getFromJSON(jsonData)
        }
        set(value) {
            val jsonData = value.toJSON()
            Asterium.hudManager.hudConfig.get(key).set("position", jsonData)
        }

    /**
     * This method does not affect original position.
     */
    fun adjustBounds(): AbsoluteScreenPosition {
        val res = Asterium.scaledResolution
        val screenWidth = res.scaledWidth
        val screenHeight =  res.scaledHeight
        val absoluteX =
            0F.coerceAtLeast(position.x.coerceAtMost(max(screenWidth - width, 0F)))
        val absoluteY =
            0F.coerceAtLeast(position.y.coerceAtMost(max(screenHeight - height, 0F)))
        return AbsoluteScreenPosition(absoluteX, absoluteY)
    }
}