package io.github.yuko1101.asterium.config

import gg.essential.api.utils.GuiUtil
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import io.github.yuko1101.asterium.Asterium
import java.io.File
import java.util.*

class AsteriumConfig : Vigilant(File("./Asterium/config.toml")) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        description = "ON/OFF",
        category = "System"
    )
    var isEnabled = true


    fun initAddons() {
        category("Addons") {
            println("[Asterium Addons] Loading...")
            println("[Asterium Addons] Addons: ${Asterium.addons.map { addonMetadata -> "${addonMetadata.name} v${addonMetadata.version}" }}")
            Asterium.addons.forEach{ addonMetaData ->
                println("[Asterium Addons] Loading ${addonMetaData.name}...")
                addonMetaData.addon.init()
                if (addonMetaData.addon.config() != null) {
                    addonMetaData.addon.config()!!.initialize()
                    button(addonMetaData.name, addonMetaData.description, addonMetaData.name, action = {
                        Objects.requireNonNull(addonMetaData.addon.config()!!.gui())?.let { GuiUtil.open(it) }
                    })
                }
            }
        }
        initialize()
    }
}