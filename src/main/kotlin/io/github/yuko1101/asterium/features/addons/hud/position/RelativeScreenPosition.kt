package io.github.yuko1101.asterium.features.addons.hud.position

import io.github.yuko1101.asterium.Asterium

class RelativeScreenPosition(val relativeX: Double, val relativeY: Double) : ScreenPosition() {

    private val resolution = Asterium.scaledResolution

    override val x: Int
        get() = (resolution.scaledWidth * relativeX).toInt()
    override val y: Int
        get() = (resolution.scaledHeight * relativeY).toInt()
}