package io.github.yuko1101.asterium.features.addons.impl.debugaddon

import gg.essential.universal.ChatColor
import io.github.yuko1101.asterium.features.addons.AsteriumAddon
import io.github.yuko1101.asterium.features.hud.DraggableFeature

class TestHud2 : DraggableFeature() {

    val text = "Test HUD2"

    override val name: String
        get() = "testhud2"
    override val id: String
        get() = name
    override val addon: AsteriumAddon
        get() = DebugAddon.instance
    override val height: Float
        get() = font.FONT_HEIGHT.toFloat()
    override val width: Float
        get() = font.getStringWidth(text).toFloat()

    override fun render() {
        font.drawStringWithShadow(text, 0F, 0F, ChatColor.GRAY.color!!.rgb)
    }

}