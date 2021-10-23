package io.github.yuko1101.asterium.config

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File

object AsteriumConfig : Vigilant(File("./Asterium/config.toml")) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        description = "ON/OFF",
        category = "System"
    )
    var isEnabled = true
}