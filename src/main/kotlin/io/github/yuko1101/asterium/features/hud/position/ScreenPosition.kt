package io.github.yuko1101.asterium.features.hud.position

import com.google.gson.JsonObject

abstract class ScreenPosition {
    abstract val x: Float
    abstract val y: Float

    abstract fun toJSON(): JsonObject

    companion object {
        fun getFromJSON(json: JsonObject): ScreenPosition {
            return when (json["type"].asString) {
                "absolute" -> {
                    val x = json["x"].asFloat
                    val y = json["y"].asFloat
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