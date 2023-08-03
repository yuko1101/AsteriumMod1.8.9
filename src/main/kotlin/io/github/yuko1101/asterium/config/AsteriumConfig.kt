package io.github.yuko1101.asterium.config

import gg.essential.api.utils.GuiUtil
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.AddonManager
import java.io.File

class AsteriumConfig : Vigilant(File("./asterium/config.toml")) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        description = "ON/OFF",
        category = "System"
    )
    var isEnabled = true

    fun initAddons() {
        category("Addons") {
            val addons = Asterium.addonManager.registered.filterNot { it.addonMetaData.initialized }
            addons.forEach { it.addonMetaData.initialized = true }
            for (addon in addons) {
                Asterium.logger.info("[Asterium Addons] Initializing ${addon.addonMetaData.name} v${addon.addonMetaData.version}")
                addon.onEnable()
                if (addon.config() != null) {
                    addon.config()!!.initialize()
                    button(addon.addonMetaData.name, addon.addonMetaData.description, addon.addonMetaData.name, action = {
                        addon.config()!!.gui()?.let { GuiUtil.open(it) }
                    })
                }

            }
        }
        initialize()
    }
}