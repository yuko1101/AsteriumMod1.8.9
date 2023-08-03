package io.github.yuko1101.asterium.features.hud

import com.google.gson.JsonObject
import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.AsteriumAddon
import io.github.yuko1101.asterium.utils.ConfigFile
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import java.io.File

abstract class HUDFeature {
    abstract val name: String
    abstract val id: String
    abstract val addon: AsteriumAddon

    abstract val height: Float
    abstract val width: Float

    open val defaultVisibility: Boolean = true

    open fun init() {}
    abstract fun render()
    open fun renderDummy() = render()

    val config by lazy {
        ConfigFile(File(Asterium.fileManager.getAddonHUDDir(addon), "$id.json"), JsonObject())
    }

    var visible: Boolean
        get() {
            if (!config.has("visible")) {
                return defaultVisibility
            }
            return config.getValue("visible").asBoolean
        }
        set(value) {
            config.set("visible", value)
        }

    /**
     * Save scale, position, and state of the HUD.
     */
    fun save() {
        config.save(compact = false)
    }

    val mc: Minecraft by lazy {
        Minecraft.getMinecraft()
    }

    val font: FontRenderer by lazy {
        mc.fontRendererObj
    }

    companion object {
        fun getLineOffset(lineNum: Int): Int {
            return (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 1) * lineNum
        }
    }

}