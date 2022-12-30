package io.github.yuko1101.asterium.features.addons.hud.position

import kotlin.math.max
import kotlin.math.min

class ScreenArea(pos1: ScreenPosition, pos2: ScreenPosition) {
    val start = AbsoluteScreenPosition(min(pos1.x, pos2.x), min(pos1.y, pos2.y))
    val end = AbsoluteScreenPosition(max(pos1.x, pos2.x), max(pos1.y, pos2.y))

    fun containsPos(pos: ScreenPosition): Boolean = (pos.x in start.x..end.x && pos.y in start.y..end.y)
    fun overlapsArea(area: ScreenArea): Boolean = (max(start.x, area.start.x) < min(end.x, area.end.x) && max(start.y, area.start.y) < min(end.y, area.end.y))
}