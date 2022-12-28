package io.github.yuko1101.asterium.features.addons.hud

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.FeaturedAddon
import io.github.yuko1101.asterium.features.addons.hud.position.AbsoluteScreenPosition
import io.github.yuko1101.asterium.features.addons.hud.position.ScreenPosition
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import kotlin.math.max

abstract class HUDFeature {
    abstract val name: String
    abstract val addon: FeaturedAddon

    abstract val height: Float
    abstract val width: Float

    open val defaultVisibility: Boolean = true

    open fun init() {}
    abstract fun render()
    open fun renderDummy() = render()



    val key: String
        get() = "$name-${addon.addonMetaData().uuid}"


    var visible: Boolean
        get() {
            if (!Asterium.hudManager.hudConfig.hasPath(listOf(key, "visible"))) {
                return defaultVisibility
            }
            return Asterium.hudManager.hudConfig.get(key).getValue("visible").asBoolean
        }
        set(value) {
            Asterium.hudManager.hudConfig.get(key).set("visible", value)
        }

    /**
     * Save scale, position, and state of the HUD.
     */
    fun save() {
        Asterium.hudManager.hudConfig.save(compact = false)
    }

    val mc: Minecraft by lazy {
        Asterium.mc
    }

    val font: FontRenderer by lazy {
        Asterium.mc.fontRendererObj
    }

    companion object {
        fun getLineOffset(lineNum: Int): Int {
            return (Asterium.mc.fontRendererObj.FONT_HEIGHT + 1) * lineNum
        }
    }

}