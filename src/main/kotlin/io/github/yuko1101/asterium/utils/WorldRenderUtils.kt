package io.github.yuko1101.asterium.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.client.renderer.GlStateManager

import net.minecraft.client.renderer.WorldRenderer

import org.lwjgl.opengl.GL11

import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.entity.RendererLivingEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.client.event.RenderLivingEvent
import java.awt.Color


class WorldRenderUtils {
    companion object {


        fun renderText(renderer: RendererLivingEntity<*>, entity: Entity, text: String, x: Double, y: Double, z: Double, height: Boolean = true) {
            val fontrenderer: FontRenderer = renderer.fontRendererFromRenderManager
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
            val worldrenderer = tessellator.worldRenderer
            val b0: Byte = 0
            val j = fontrenderer.getStringWidth(text) / 2
            GlStateManager.disableTexture2D()
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
            worldrenderer.pos((-j - 1).toDouble(), (-1 + b0).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldrenderer.pos((-j - 1).toDouble(), (8 + b0).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldrenderer.pos((j + 1).toDouble(), (8 + b0).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            worldrenderer.pos((j + 1).toDouble(), (-1 + b0).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
            tessellator.draw()
            GlStateManager.enableTexture2D()
            fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, b0.toInt(), 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, b0.toInt(), -1);
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
            Thread(Runnable {
                Thread.sleep(time)
                armorStand.setDead()
            }).start()
        }

        fun onRender(event: RenderLivingEvent.Pre<EntityArmorStand>) {
            val entity = event.entity
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

            val positions = arrayListOf(
                arrayListOf(0.5, 0.0, 0.5),
                arrayListOf(0.5, 1.0, 0.5),
                arrayListOf(-0.5, 0.0, -0.5),
                arrayListOf(-0.5, 1.0, -0.5),
                arrayListOf(0.5, 0.0, -0.5),
                arrayListOf(0.5, 1.0, -0.5),
                arrayListOf(-0.5, 0.0, 0.5),
                arrayListOf(-0.5, 1.0, 0.5),
                arrayListOf(0.5, 1.0, -0.5),
                arrayListOf(0.5, 1.0, 0.5),
                arrayListOf(-0.5, 1.0, 0.5),
                arrayListOf(0.5, 1.0, 0.5),
                arrayListOf(-0.5, 1.0, -0.5),
                arrayListOf(0.5, 1.0, -0.5),
                arrayListOf(-0.5, 1.0, -0.5),
                arrayListOf(-0.5, 1.0, 0.5),
                arrayListOf(0.5, 0.0, -0.5),
                arrayListOf(0.5, 0.0, 0.5),
                arrayListOf(-0.5, 0.0, 0.5),
                arrayListOf(0.5, 0.0, 0.5),
                arrayListOf(-0.5, 0.0, -0.5),
                arrayListOf(0.5, 0.0, -0.5),
                arrayListOf(-0.5, 0.0, -0.5),
                arrayListOf(-0.5, 0.0, 0.5)
            )
            var counter = 0

            val mc = Minecraft.getMinecraft()


            GL11.glPushMatrix()
            GlStateManager.translate(x, y, z)
            GL11.glNormal3f(0.0f, 1.0f, 0.0f)
            GL11.glEnable(3042)
            GL11.glBlendFunc(770, 771)
            GL11.glDisable(3553)
            GL11.glEnable(2848)
            GL11.glHint(3154, 4354)
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
            GL11.glDisable(2848)
            GL11.glEnable(3553)
            GL11.glDisable(3042)
            GL11.glPopMatrix()
        }

        fun drawLine(worldRenderer: WorldRenderer, renderer: RendererLivingEntity<*>, x: Double, y: Double, z: Double, x2: Double, y2: Double, z2:Double) {

        }

    }
}