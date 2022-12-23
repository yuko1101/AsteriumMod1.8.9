package io.github.yuko1101.asterium.utils.minecraft

import gg.essential.api.utils.Multithreading
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.GlStateManager

import org.lwjgl.opengl.GL11

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.entity.RendererLivingEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraftforge.client.event.RenderLivingEvent
import java.awt.Color
import java.util.concurrent.TimeUnit


class SubRenderUtils {
    companion object {


        private fun renderText(renderer: RendererLivingEntity<*>, entity: Entity, text: String, x: Double, y: Double, z: Double, height: Boolean = true) {
            val fontRenderer: FontRenderer = renderer.fontRendererFromRenderManager
            val f = 1.6f
            val f1 = 0.016666668f * f
            GlStateManager.pushMatrix()
            var y2 = y
            if (height) y2 += entity.height + 0.5f
            GlStateManager.translate(x.toFloat() + 0.0f, y2.toFloat(), z.toFloat())
            GL11.glNormal3f(0.0f, 1.0f, 0.0f)
            GlStateManager.rotate(-renderer.renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
            GlStateManager.rotate(renderer.renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
            GlStateManager.scale(-f1, -f1, f1)
            GlStateManager.disableLighting()
            GlStateManager.depthMask(false)
            GlStateManager.disableDepth()
            GlStateManager.enableBlend()
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            val tessellator = Tessellator.getInstance()
            val worldRenderer = tessellator.worldRenderer
            val b0: Byte = 0
            val j = fontRenderer.getStringWidth(text) / 2
            GlStateManager.disableTexture2D()
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
            worldRenderer.pos((-j - 1).toDouble(), (-1 + b0).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldRenderer.pos((-j - 1).toDouble(), (8 + b0).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldRenderer.pos((j + 1).toDouble(), (8 + b0).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldRenderer.pos((j + 1).toDouble(), (-1 + b0).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            tessellator.draw()
            GlStateManager.enableTexture2D()
            fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, b0.toInt(), 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, b0.toInt(), -1);
            GlStateManager.enableLighting()
            GlStateManager.disableBlend()
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            GlStateManager.popMatrix()
        }

        fun renderText(text: String, x: Double, y: Double, z: Double, time: Long) {
            val armorStand = EntityArmorStand(Minecraft.getMinecraft().theWorld)
            val nbt = NBTTagCompound()
            nbt.setBoolean("Marker", true)
            armorStand.readFromNBT(nbt)
            armorStand.isInvisible = true
            armorStand.alwaysRenderNameTag = false
            armorStand.customNameTag = "[Asterium] Text:${text}"
            Minecraft.getMinecraft().theWorld.spawnEntityInWorld(armorStand)
            armorStand.setPosition(x, y, z)
            Multithreading.schedule({
                armorStand.setDead()
            }, time, TimeUnit.MILLISECONDS)
        }

        fun onRenderArmorStand(event: RenderLivingEvent.Pre<EntityArmorStand>) {
            val entity = event.entity
            if (entity !is EntityArmorStand) return
            if (Minecraft.getMinecraft().thePlayer.getDistanceSqToEntity(entity) < 4096.0) {
                if (!entity.customNameTag.matches("\\[Asterium] Text:(.*)".toRegex())) return
                val text = entity.customNameTag.replace("\\[Asterium] Text:(.*)".toRegex(), "$1")
                renderText(
                    event.renderer,
                    entity,
                    text,
                    event.x,
                    event.y,
                    event.z,
                    false
                )
            }
        }


        fun drawBox(entity: Entity, x: Double, y: Double, z: Double, color: Color, lineWidth: Int) {
            val width = entity.width
            val height = entity.height

            val positions = listOf(
                listOf(0.5, 0.0, 0.5),
                listOf(0.5, 1.0, 0.5),
                listOf(-0.5, 0.0, -0.5),
                listOf(-0.5, 1.0, -0.5),
                listOf(0.5, 0.0, -0.5),
                listOf(0.5, 1.0, -0.5),
                listOf(-0.5, 0.0, 0.5),
                listOf(-0.5, 1.0, 0.5),
                listOf(0.5, 1.0, -0.5),
                listOf(0.5, 1.0, 0.5),
                listOf(-0.5, 1.0, 0.5),
                listOf(0.5, 1.0, 0.5),
                listOf(-0.5, 1.0, -0.5),
                listOf(0.5, 1.0, -0.5),
                listOf(-0.5, 1.0, -0.5),
                listOf(-0.5, 1.0, 0.5),
                listOf(0.5, 0.0, -0.5),
                listOf(0.5, 0.0, 0.5),
                listOf(-0.5, 0.0, 0.5),
                listOf(0.5, 0.0, 0.5),
                listOf(-0.5, 0.0, -0.5),
                listOf(0.5, 0.0, -0.5),
                listOf(-0.5, 0.0, -0.5),
                listOf(-0.5, 0.0, 0.5)
            )
            var counter = 0

            val mc = Minecraft.getMinecraft()


            GL11.glPushMatrix()
            GlStateManager.translate(x, y, z)
            GL11.glNormal3f(0.0f, 1.0f, 0.0f)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST)
            GL11.glLineWidth(lineWidth.toFloat() / 10.0f + 0.5f)
            GL11.glColor4f(color.red.toFloat() / 255.0f, color.green.toFloat() / 255.0f, color.blue.toFloat() / 255.0f, color.alpha.toFloat() / 255.0f)
            val tessellator = Tessellator.getInstance()
            val worldRenderer = tessellator.worldRenderer
            worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR)
            positions.forEach {pos ->
                worldRenderer.pos(
                    pos[0] * width,
                    pos[1] * height,
                    pos[2] * width
                ).color(color.red.toFloat() / 255.0f, color.green.toFloat() / 255.0f, color.blue.toFloat() / 255.0f, color.alpha.toFloat() / 255.0f).endVertex()
                counter++;
                if (counter % 2 == 0) {
                    tessellator.draw();
                    if (counter != 24) {
                        worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR)
                    }
                }
            }
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glPopMatrix()
        }

        fun drawLine(x: Double, y: Double, z: Double, x2: Double, y2: Double, z2:Double, color: Color, lineWidth: Int) {
            GL11.glPushMatrix()
            GlStateManager.translate(x, y, z)
            GL11.glNormal3f(0.0f, 1.0f, 0.0f)
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
            GL11.glDisable(GL11.GL_TEXTURE_2D)
            GL11.glEnable(GL11.GL_LINE_SMOOTH)
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST)
            GL11.glLineWidth(lineWidth.toFloat() / 10.0f + 0.5f)
            GL11.glColor4f(color.red.toFloat() / 255.0f, color.green.toFloat() / 255.0f, color.blue.toFloat() / 255.0f, color.alpha.toFloat() / 255.0f)
            val tessellator = Tessellator.getInstance()
            val worldRenderer = tessellator.worldRenderer
            worldRenderer.begin(3, DefaultVertexFormats.POSITION_COLOR)
            worldRenderer.pos(x, y, z).color(color.red.toFloat() / 255.0f, color.green.toFloat() / 255.0f, color.blue.toFloat() / 255.0f, color.alpha.toFloat() / 255.0f).endVertex()
            worldRenderer.pos(x2, y2, z2).color(color.red.toFloat() / 255.0f, color.green.toFloat() / 255.0f, color.blue.toFloat() / 255.0f, color.alpha.toFloat() / 255.0f).endVertex()
            tessellator.draw()
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            GL11.glDisable(GL11.GL_LINE_SMOOTH)
            GL11.glEnable(GL11.GL_TEXTURE_2D)
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glPopMatrix()
        }

        fun BlockPos.drawWaypoint(partialTicks: Float, color: Color, waypointText: String?) {
            val (viewerX, viewerY, viewerZ) = RenderUtils.getViewerPos(partialTicks)
            val pos = this
            val x = pos.x - viewerX
            val y = pos.y - viewerY
            val z = pos.z - viewerZ
            val distSq = x * x + y * y + z * z
            GlStateManager.disableDepth()
            GlStateManager.disableCull()
            RenderUtils.drawFilledBoundingBox(
                AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1).expandBlock(),
                color,
                (0.1f + 0.005f * distSq.toFloat()).coerceAtLeast(0.2f)
            )
            GlStateManager.disableTexture2D()
            if (distSq > 5 * 5) RenderUtils.renderBeaconBeam(x, y + 1, z, color.rgb, 1.0f, partialTicks)
            waypointText?.let {
                RenderUtils.renderWaypointText(
                    waypointText,
                    this.x + 0.5,
                    this.y + (if (distSq > 5 * 5) 5.0 else 1.5),
                    this.z + 0.5,
                    partialTicks
                )
            }
            GlStateManager.disableLighting()
            GlStateManager.enableTexture2D()
            GlStateManager.enableDepth()
            GlStateManager.enableCull()
        }

    }
}