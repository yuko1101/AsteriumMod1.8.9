package io.github.yuko1101.asterium.features.addons.hud.position

import com.google.gson.JsonObject

abstract class ScreenPosition {
    abstract val x: Int
    abstract val y: Int

    companion object {
        fun getFromJSON(json: JsonObject): ScreenPosition {
            return when (json["type"].asString) {
                "absolute" -> {
                    val x = json["x"].asInt
                    val y = json["y"].asInt
                    AbsoluteScreenPosition(x, y)
                }
                "relative" -> {
                    val relativeX = json["relativeX"].asDouble
                    val relativeY = json["relativeY"].asDouble
                    RelativeScreenPosition(relativeX, relativeY)
                }
                else -> throw Exception("Position data corrupted.")
            }
        }
    }
}