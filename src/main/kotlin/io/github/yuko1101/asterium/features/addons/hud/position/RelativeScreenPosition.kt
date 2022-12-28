package io.github.yuko1101.asterium.features.addons.hud.position

import com.google.gson.JsonObject
import io.github.yuko1101.asterium.Asterium

class RelativeScreenPosition(val relativeX: Double, val relativeY: Double) : ScreenPosition() {

    private val resolution = Asterium.scaledResolution

    override val x: Float
        get() = (resolution.scaledWidth * relativeX).toFloat()
    override val y: Float
        get() = (resolution.scaledHeight * relativeY).toFloat()

    override fun toJSON(): JsonObject {
        val json = JsonObject()
        json.addProperty("type", "relative")
        json.addProperty("relativeX", relativeX)
        json.addProperty("relativeY", relativeY)
        return json
    }
}