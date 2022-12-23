package io.github.yuko1101.asterium.utils.minecraft

import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import kotlin.math.pow
import kotlin.math.sqrt

object WorldUtils {

    @JvmStatic
    fun getDistance(start: Vec3, end: Vec3): Double {
        return sqrt((start.xCoord - end.xCoord).pow(2.0) + (start.yCoord - end.yCoord).pow(2.0) + (start.zCoord - end.zCoord).pow(2.0))
    }
    @JvmStatic
    fun getDistance(start: BlockPos, end: BlockPos): Double {
        return sqrt((start.x - end.x).toDouble().pow(2.0) + (start.y - end.y).toDouble().pow(2.0) + (start.z - end.z).toDouble().pow(2.0))
    }

    fun BlockPos.toVec3(): Vec3 {
        return Vec3(x.toDouble(), y.toDouble(), z.toDouble())
    }
}