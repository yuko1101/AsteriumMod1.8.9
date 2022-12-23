package io.github.yuko1101.asterium.features.addons.arrowPath

import gg.essential.vigilance.Vigilant
import io.github.yuko1101.asterium.features.addons.AddonMetaData
import io.github.yuko1101.asterium.features.addons.FeaturedAddon
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11
import java.util.ArrayList

class ArrowPath : FeaturedAddon() {
    override fun init() {
        MinecraftForge.EVENT_BUS.register(this)
    }

    override fun addonMetaData(): AddonMetaData {
        return AddonMetaData("ArrowPath", "0.1.0", "矢の軌跡を表示します")
    }

    override fun config(): Vigilant {
        return config
    }

    companion object {
        val config = ArrowPathConfig()
    }

    private val arrowMap: MutableMap<EntityArrow, ArrayList<Vec3>> = mutableMapOf()
    private val removeList = arrayListOf<EntityArrow>()




    @SubscribeEvent
    fun renderWorld(e: RenderWorldLastEvent) {
        if (removeList.size > 0) {
            for (rl in removeList) {
                if (arrowMap.containsKey(rl)) arrowMap.remove(rl)
            }
            removeList.clear()
        }
        if (config.isEnabled) {
            Minecraft.getMinecraft().theWorld.loadedEntityList.stream().filter { entity: Entity -> entity is EntityArrow && !arrowMap.containsKey(entity) && !entity.isInvisible() && isMoving(entity) }.forEach { entity: Entity -> arrowMap[entity as EntityArrow] = arrayListOf(Vec3(entity.posX, entity.posY, entity.posZ)) }
//                println(arrowMap.map { (_, arrowPath) -> arrowPath.size })
            while (true) {
                for ((arrow, arrowPath) in arrowMap) {
                    if (!arrow.isInvisible && isMoving(arrow)) {
                        if (arrowPath.size > config.length) {
                            arrowPath.removeAt(0)
                        }
                    } else {
                        if (arrowPath.size > 1) {
                            arrowPath.removeAt(0)
                        }
                        if (arrowPath.size <= 1) {
                            removeList.add(arrow)
                            continue
                        }
                    }
                    val currentPos = Vec3(arrow.posX, arrow.posY, arrow.posZ)
                    if (Minecraft.getMinecraft().theWorld.loadedEntityList.find { entity -> entity.uniqueID == arrow.uniqueID } == null) {
                        removeList.add(arrow)
                        continue
                    }
                    if (arrowPath[arrowPath.size - 1].distanceTo(currentPos) >= 0.05000000074505806) {
                        arrowMap[arrow]!!.add(currentPos)
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
                    arrowPath.forEach { position ->
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