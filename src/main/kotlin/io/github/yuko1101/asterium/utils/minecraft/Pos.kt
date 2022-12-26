package io.github.yuko1101.asterium.utils.minecraft

import net.minecraft.util.Vec3
import kotlin.math.cos
import kotlin.math.sin

class Pos(var x: Double, var y: Double, var z: Double, var angleX: Double = 0.0, var angleY: Double = 0.0) {
    fun addVector(vec: Vec3): Pos {
        x += vec.xCoord
        y += vec.yCoord
        z += vec.zCoord
        return this
    }
    fun setVector(vec: Vec3): Pos {
        x = vec.xCoord
        y = vec.yCoord
        z = vec.zCoord
        return this
    }
    fun addAngle(x: Double, y: Double): Pos {
        angleX += x
        angleY += y
        return this
    }
    fun setAngle(x: Double, y: Double): Pos {
        angleX = x
        angleY = y
        return this
    }
    fun forward(distance: Double): Pos {
        val radiansX = Math.toRadians(angleX)
        val sinX = sin(radiansX)
        val cosX = cos(radiansX)
        val radiansY = Math.toRadians(-angleY)
        val sinY = sin(radiansY)
        val cosY = cos(radiansY)
        val posX = -sinX * cosY
        val posY = sinY
        val posZ = cosX * cosY

        x += posX * distance
        y += posY * distance
        z += posZ * distance
        return this
    }
}