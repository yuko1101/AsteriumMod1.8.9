package io.github.yuko1101.asterium.features.addons.impl.huds

import gg.essential.universal.ChatColor
import io.github.yuko1101.asterium.features.addons.FeaturedAddon
import io.github.yuko1101.asterium.features.addons.hud.DraggableFeature

class TestHud : DraggableFeature() {

    val text = "Test HUD"

    override val name: String
        get() = "testhud"
    override val addon: FeaturedAddon
        get() = Huds.instance
    override val height: Float
        get() = scale * font.FONT_HEIGHT
    override val width: Float
        get() = font.getStringWidth(text).toFloat() * scale

    override fun render() {
        font.drawStringWithShadow(text, position.x, position.y, ChatColor.GRAY.color!!.rgb)
    }

}