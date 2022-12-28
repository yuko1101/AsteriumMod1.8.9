package io.github.yuko1101.asterium.utils.minecraft

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import java.awt.Color


object DrawUtils {
    fun drawChromaString(string: String, x: Int, y: Int, shadow: Boolean) {
        val mc = Minecraft.getMinecraft()
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
            mc.fontRendererObj.drawString(textChar.toString(), xTmp.toFloat(), y.toFloat(), i, shadow)
            xTmp += mc.fontRendererObj.getStringWidth(textChar.toString())
        }
    }

    fun drawRect(
        x: Float,
        y: Float,
        u: Float,
        v: Float,
        uWidth: Float,
        vHeight: Float,
        width: Float,
        height: Float,
        tileWidth: Float,
        tileHeight: Float,
        linearTexture: Boolean
    ) {
        if (linearTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)
        }
        val f = 1.0f / tileWidth
        val f1 = 1.0f / tileHeight
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX)
        worldRenderer.pos(x.toDouble(), (y + height).toDouble(), 0.0)
            .tex((u * f).toDouble(), ((v + vHeight) * f1).toDouble()).endVertex()
        worldRenderer.pos((x + width).toDouble(), (y + height).toDouble(), 0.0)
            .tex(((u + uWidth) * f).toDouble(), ((v + vHeight) * f1).toDouble()).endVertex()
        worldRenderer.pos((x + width).toDouble(), y.toDouble(), 0.0)
            .tex(((u + uWidth) * f).toDouble(), (v * f1).toDouble()).endVertex()
        worldRenderer.pos(x.toDouble(), y.toDouble(), 0.0).tex((u * f).toDouble(), (v * f1).toDouble()).endVertex()
        tessellator.draw()
        if (linearTexture) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        }
    }
}