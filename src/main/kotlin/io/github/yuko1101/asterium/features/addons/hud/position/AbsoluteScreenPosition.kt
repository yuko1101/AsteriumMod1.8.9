package io.github.yuko1101.asterium.features.addons.hud.position

import com.google.gson.JsonObject

class AbsoluteScreenPosition(override var x: Float, override var y: Float) : ScreenPosition() {

    override fun toJSON(): JsonObject {
        val json = JsonObject()
        json.addProperty("type", "absolute")
        json.addProperty("x", x)
        json.addProperty("y", y)
        return json
    }

}