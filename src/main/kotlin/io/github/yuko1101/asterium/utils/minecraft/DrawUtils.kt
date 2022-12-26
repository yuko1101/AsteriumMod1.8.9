package io.github.yuko1101.asterium.utils.minecraft

import net.minecraft.client.Minecraft
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
}