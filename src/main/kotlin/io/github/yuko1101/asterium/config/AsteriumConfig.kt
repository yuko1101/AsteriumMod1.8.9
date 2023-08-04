package io.github.yuko1101.asterium.config

import gg.essential.api.utils.GuiUtil
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import io.github.yuko1101.asterium.Asterium
import java.io.File

class AsteriumConfig : Vigilant(File("./asterium/config.toml")) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        description = "ON/OFF",
        category = "System"
    )
    var isEnabled = true

    fun updateAddons() {
        category("Addons") {
            val addons = Asterium.addonManager.registered.filter { it.config != null }
            for (addon in addons) {
                button(addon.addonMetaData.name, addon.addonMetaData.description, addon.addonMetaData.name, action = {
                    addon.config!!.gui()?.let { GuiUtil.open(it) }
                })
            }
        }
        initialize()
    }
}