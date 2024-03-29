package io.github.yuko1101.asterium.commands

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import gg.essential.api.utils.GuiUtil
import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.hud.DraggableFeature
import io.github.yuko1101.asterium.features.hud.HUDEditScreen
import io.github.yuko1101.asterium.utils.minecraft.ChatLib
import java.util.*

@Suppress("unused")
class AsteriumCommand : Command("asterium") {

    @DefaultHandler
    fun handle() {
        Asterium.config.gui()?.let { GuiUtil.open(it) }
    }

    @SubCommand(value = "toggle", description = "Toggle your Asterium enabled or disabled.")
    fun toggle() {
        Asterium.config.isEnabled = !Asterium.config.isEnabled
    }

    @SubCommand(value = "refresh", description = "Refresh your Asterium functions including addons.")
    fun refresh() {
        Asterium.refresh()
        ChatLib.chat("リフレッシュしました！")
    }

    @SubCommand(value = "unload", description = "Unload all your Asterium Addons.")
    fun unload() {
        Asterium.addonManager.unloadAll()
        ChatLib.chat("アンロードしました！")
    }

    @SubCommand(value = "hud", description = "Open HUD edit screen.")
    fun openHUDEditScreen() {
        GuiUtil.open(HUDEditScreen(Asterium.hudManager.registered.filterIsInstance<DraggableFeature>().toList()))
    }
}