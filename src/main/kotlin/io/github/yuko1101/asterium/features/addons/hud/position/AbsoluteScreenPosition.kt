package io.github.yuko1101.asterium.features.addons.hud.position

import com.google.gson.JsonObject

class AbsoluteScreenPosition(override var x: Int, override var y: Int) : ScreenPosition() {
    companion object {
        fun getFromJSON(json: JsonObject): AbsoluteScreenPosition {
            val x = json["x"].asInt
            val y = json["y"].asInt
            return AbsoluteScreenPosition(x, y)
        }
    }

}