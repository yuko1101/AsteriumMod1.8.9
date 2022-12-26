package io.github.yuko1101.asterium.features.addons.hud

import io.github.yuko1101.asterium.Asterium
import net.minecraft.client.gui.GuiScreen

class HUDEditScreen : GuiScreen() {
    override fun initGui() {
        val features = Asterium.hudManager.features
        for (feature in features) {

        }
    }
}