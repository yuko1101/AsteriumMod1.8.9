package io.github.yuko1101.asterium.features.addons.hud

import gg.essential.api.utils.GuiUtil
import io.github.yuko1101.asterium.Asterium
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard

class HUDEditScreen : GuiScreen() {

    val features: List<HUDFeature>
        get() = Asterium.hudManager.features.filterIsInstance<DraggableFeature>()

    override fun initGui() {
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        for (feature in features) {
            feature.renderDummy()
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        when (keyCode) {
            Keyboard.KEY_ESCAPE -> {
                GuiUtil.open(null)
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