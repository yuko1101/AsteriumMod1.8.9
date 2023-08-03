package io.github.yuko1101.asterium.features.hud

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.hud.position.AbsoluteScreenPosition
import io.github.yuko1101.asterium.features.hud.position.ScreenPosition
import kotlin.math.max

abstract class DraggableFeature : HUDFeature() {
    open val defaultPosition: ScreenPosition = AbsoluteScreenPosition(0F, 0F)
    open val defaultScale: Float = 1F

    var scale: Float
        get() {
            if (!config.has("scale")) {
                return defaultScale
            }
            return config.getValue("scale").asFloat
        }
        set(value) {
            config.set("scale", value)
        }

    var position: ScreenPosition
        get() {
            if (!config.has("position")) {
                return defaultPosition
            }
            val jsonData = config.getValue("position").asJsonObject
            return ScreenPosition.getFromJSON(jsonData)
        }
        set(value) {
            val jsonData = value.toJSON()
            config.set("position", jsonData)
        }

    /**
     * This method does not affect original position.
     */
    fun adjustBounds(): AbsoluteScreenPosition {
        val res = Asterium.scaledResolution
        val screenWidth = res.scaledWidth
        val screenHeight =  res.scaledHeight
        val absoluteX =
            0F.coerceAtLeast(position.x.coerceAtMost(max(screenWidth - width * scale, 0F)))
        val absoluteY =
            0F.coerceAtLeast(position.y.coerceAtMost(max(screenHeight - height * scale, 0F)))
        return AbsoluteScreenPosition(absoluteX, absoluteY)
    }
}