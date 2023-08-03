package io.github.yuko1101.asterium.features.addons.impl.arrowPath

import gg.essential.vigilance.Vigilant
import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.events.EventTrigger
import io.github.yuko1101.asterium.features.addons.AsteriumAddon
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11
import java.util.*
import kotlin.collections.ArrayList

/**
 * Repo: https://github.com/asbyth/ArrowTrail (Not available)
 * No licence provided
 *
 * More Info: https://github.com/HyperiumClient/Hyperium-Repo/blob/master/installer/addons.json#L3-L13
 * @author asbyth, aycy (Modified by yuko1101)
 */
class ArrowPath : AsteriumAddon() {
    override fun onEnable(): String? {
        Asterium.eventManager.register(this)
        return null
    }

    override fun onDisable(): String? {
        return null
    }

    override val addonMetaData = AddonMetaData(
        name = "ArrowPath",
        uuid = UUID.fromString("63efd81f-9a4b-4958-9fd5-1167af552b84"),
        description = "矢の軌跡を表示します",
        version = "0.1.0",
        author = "yuko1101"
    )
    override fun config(): Vigilant {
        return config
    }

    companion object {
        val config = ArrowPathConfig()
    }

    private val arrows = arrayListOf<Pair<EntityArrow, ArrayList<Pair<Vec3, Long>>>>()
    private val removeList = arrayListOf<EntityArrow>()


    @EventTrigger
    fun onRenderWorldLastEvent(event: RenderWorldLastEvent) {
        if (removeList.size > 0) {
            for (rl in removeList) {
                if (arrows.map { it.first }.contains(rl)) arrows.removeIf { it.first == rl }
            }
            removeList.clear()
        }
        if (config.isEnabled) {
            Minecraft.getMinecraft().theWorld.loadedEntityList.filter { entity: Entity -> entity is EntityArrow && !arrows.map { it.first }.contains(entity) && !entity.isInvisible() && isMoving(entity) }.forEach { entity: Entity -> arrows.add(Pair(entity as EntityArrow, arrayListOf(Pair(Vec3(entity.posX, entity.posY, entity.posZ), System.currentTimeMillis())))) }
//                println(arrowMap.map { (_, arrowPath) -> arrowPath.size })
            while (true) {
                for ((arrow, arrowPath) in arrows) {
                    val arrowExists = Minecraft.getMinecraft().theWorld.loadedEntityList.find { entity -> entity.uniqueID == arrow.uniqueID } != null
                    if (arrowExists && !arrow.isInvisible && isMoving(arrow)) {
                        if (arrowPath.size > config.length) {
                            arrowPath.removeAt(0)
                        }
                    } else {
                        // 矢が消えた、透明になった、止まった場合、それぞれのarrow pathについて作成から一定時間が過ぎたらそれぞれ削除する
                        if (arrowPath.size > 1 && System.currentTimeMillis() - arrowPath.first().second > 500) {
                            arrowPath.removeAt(0)
                        }
                        if (arrowPath.size <= 1) {
                            removeList.add(arrow)
                            continue
                        }
                    }
                    val currentPos = Vec3(arrow.posX, arrow.posY, arrow.posZ)

                    if (arrowPath[arrowPath.size - 1].first.distanceTo(currentPos) >= 0.05000000074505806 && arrowExists) {
                        arrowPath.add(Pair(currentPos, System.currentTimeMillis()))
                    }
                    GL11.glPushMatrix()
                    GL11.glEnable(3042)
                    GL11.glBlendFunc(770, 771)
                    GL11.glDisable(3553)
                    GL11.glEnable(2848)
                    GL11.glHint(3154, 4354)
                    GL11.glLineWidth(config.width.toFloat() / 10.0f + 0.5f)
                    val color = config.color
                    GL11.glColor4f(color.red.toFloat() / 255.0f, color.green.toFloat() / 255.0f, color.blue.toFloat() / 255.0f, color.alpha.toFloat() / 255.0f)
                    val tessellator = Tessellator.getInstance()
                    val worldRenderer = tessellator.worldRenderer
                    worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR)
                    arrowPath.map { it.first }.forEach { position ->
                        val xPos = position.xCoord - Minecraft.getMinecraft().renderManager.viewerPosX
                        val yPos = position.yCoord - Minecraft.getMinecraft().renderManager.viewerPosY
                        val zPos = position.zCoord - Minecraft.getMinecraft().renderManager.viewerPosZ
                        worldRenderer.pos(xPos, yPos, zPos).color(color.red.toFloat() / 255.0f, color.green.toFloat() / 255.0f, color.blue.toFloat() / 255.0f, color.alpha.toFloat() / 255.0f).endVertex()
                    }
                    tessellator.draw()
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
                    GL11.glDisable(2848)
                    GL11.glEnable(3553)
                    GL11.glDisable(3042)
                    GL11.glPopMatrix()
                }
                return
            }
        }

    }

    private fun isMoving(arrow: Entity): Boolean {
        return arrow.prevPosX != arrow.posX && arrow.prevPosY != arrow.posY && arrow.prevPosZ != arrow.posZ
    }
}