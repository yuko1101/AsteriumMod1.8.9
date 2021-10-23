package io.github.yuko1101.asterium.commands

import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler
import gg.essential.api.commands.SubCommand
import gg.essential.api.utils.GuiUtil
import io.github.yuko1101.asterium.Asterium
import java.util.*

class AsteriumCommand : Command("asterium") {
    @DefaultHandler
    fun handle() {
        Objects.requireNonNull(Asterium.config.gui())?.let { GuiUtil.open(it) }
    }

    @SubCommand(value = "toggle", description = "Toggle your Asterium enabled or disabled.")
    fun refresh() {
        Asterium.config.isEnabled = !Asterium.config.isEnabled
    }
}