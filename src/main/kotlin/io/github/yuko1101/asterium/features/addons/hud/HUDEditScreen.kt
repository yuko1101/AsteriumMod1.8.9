package io.github.yuko1101.asterium.features.addons.hud

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.utils.minecraft.DrawUtils
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.input.Keyboard

class HUDEditScreen : GuiScreen() {

    val features: List<DraggableFeature>
        get() = Asterium.hudManager.features.filterIsInstance<DraggableFeature>()

    override fun initGui() {
        for (feature in features) {
            println("${feature.name}-${feature.addon.getAddonMetaData().uuid}: ${feature.position.x}, ${feature.position.y} scale: ${feature.scale}, visible: ${feature.visible}")
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        for (feature in features) {
            GlStateManager.pushMatrix()
            val scale = feature.scale
            GlStateManager.translate(feature.position.x, feature.position.y, 0f)
            GlStateManager.scale(scale.toDouble(), scale.toDouble(), 1.0)
            feature.renderDummy()
            DrawUtils.drawHollowRect(0F, 0F, feature.width, feature.height, 0.5F, 0xFFFFFFFF)
            GlStateManager.popMatrix()
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        when (keyCode) {
            Keyboard.KEY_ESCAPE -> {
                mc.displayGuiScreen(null)
                onGuiClosed()
            }
        }
    }

    override fun onGuiClosed() {
        Asterium.hudManager.hudConfig.save()
    }

    override fun doesGuiPauseGame(): Boolean {
        return true
    }
}