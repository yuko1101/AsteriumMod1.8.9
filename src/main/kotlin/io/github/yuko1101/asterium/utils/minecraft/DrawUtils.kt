package io.github.yuko1101.asterium.utils.minecraft

import io.github.yuko1101.asterium.Asterium
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color


object DrawUtils {

    private val mc = Asterium.mc
    private val tessellator = Tessellator.getInstance()
    private val worldRenderer = tessellator.worldRenderer

    fun drawChromaString(string: String, x: Float, y: Float, shadow: Boolean) {
        var xTmp = x

        var formatMode = false
        val formatList = arrayListOf<Char>()

        for (textChar in string.toCharArray()) {
            if (textChar == '§') {
                formatMode = true
                continue
            }
            if (formatMode) {
                formatMode = false
                when (textChar) {
                    'k', 'l', 'm', 'n', 'o' -> formatList.add(textChar)
                    else -> formatList.clear()
                }
                continue
            }
            val l = System.currentTimeMillis() - (xTmp * 10 - y * 10)
            val i = Color.HSBtoRGB(l % 2000.0f.toInt() / 2000.0f, 0.8f, 0.8f)
            mc.fontRendererObj.drawString(textChar.toString(), xTmp, y, i, shadow)
            xTmp += mc.fontRendererObj.getStringWidth(textChar.toString())
        }
    }

    fun drawHollowRect(x1: Float, y1: Float, x2: Float, y2: Float, width: Float, color: Long) {
        drawLine(x1, y1, x1, y2, width, color)
        drawLine(x1, y1, x2, y1, width, color)
        drawLine(x1, y2, x2, y2, width, color)
        drawLine(x2, y1, x2, y2, width, color)
    }

    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * Modified
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     * @author Moulberry (Modified)
     */
    fun drawLine(sx: Float, sy: Float, ex: Float, ey: Float, width: Float, color: Long) {
        val f = (color shr 24 and 255).toFloat() / 255.0f
        val f1 = (color shr 16 and 255).toFloat() / 255.0f
        val f2 = (color shr 8 and 255).toFloat() / 255.0f
        val f3 = (color and 255).toFloat() / 255.0f
        GlStateManager.pushMatrix()
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(f1, f2, f3, f)
        GL11.glLineWidth(width)
        GL11.glBegin(GL11.GL_LINES)
        GL11.glVertex2d(sx.toDouble(), sy.toDouble())
        GL11.glVertex2d(ex.toDouble(), ey.toDouble())
        GL11.glEnd()
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.popMatrix()
    }

    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * Modified
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     * @author Moulberry (Modified)
     */
    fun drawDottedLine(sx: Float, sy: Float, ex: Float, ey: Float, width: Float, factor: Int, color: Long) {
        GlStateManager.pushMatrix()
        GL11.glLineStipple(factor, 0xAAAA.toShort())
        GL11.glEnable(GL11.GL_LINE_STIPPLE)
        drawLine(sx, sy, ex, ey, width, color)
        GL11.glDisable(GL11.GL_LINE_STIPPLE)
        GlStateManager.popMatrix()
    }

    /**
     * Taken from NotEnoughUpdates under Creative Commons Attribution-NonCommercial 3.0
     * Modified
     * https://github.com/Moulberry/NotEnoughUpdates/blob/master/LICENSE
     * @author Moulberry (Modified)
     */
    fun drawRect(left: Float, top: Float, right: Float, bottom: Float, color: Long) {
        val f3 = (color shr 24 and 255).toFloat() / 255.0f
        val f = (color shr 16 and 255).toFloat() / 255.0f
        val f1 = (color shr 8 and 255).toFloat() / 255.0f
        val f2 = (color and 255).toFloat() / 255.0f
        val tessellator = Tessellator.getInstance()
        GlStateManager.disableTexture2D()
        GlStateManager.color(f, f1, f2, f3)
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(left.toDouble(), bottom.toDouble(), 0.0).endVertex()
        worldRenderer.pos(right.toDouble(), bottom.toDouble(), 0.0).endVertex()
        worldRenderer.pos(right.toDouble(), top.toDouble(), 0.0).endVertex()
        worldRenderer.pos(left.toDouble(), top.toDouble(), 0.0).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
    }
}