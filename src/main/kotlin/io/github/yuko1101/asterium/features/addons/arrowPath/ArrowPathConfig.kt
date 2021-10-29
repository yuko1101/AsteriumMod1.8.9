package io.github.yuko1101.asterium.features.addons.arrowPath

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.awt.Color
import java.io.File

class ArrowPathConfig : Vigilant(File("./Asterium/Addons/ArrowPath.toml")) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Enabled",
        description = "このアドオンのON/OFFを切り替えれます",
        category = "System"
    )
    var isEnabled = true

    @Property(
        type = PropertyType.COLOR,
        name = "Color",
        description = "軌跡の線の色を変更できます",
        category = "System"
    )
    var color = Color.WHITE

    @Property(
        type = PropertyType.SLIDER,
        name = "Length",
        description = "軌跡の線の長さを調節できます",
        category = "System",
        max = 100,
        min = 1
    )
    var length = 10

    @Property(
        type = PropertyType.SLIDER,
        name = "Width",
        description = "軌跡の線の太さを調節できます",
        category = "System",
        max = 100,
        min = 1
    )
    var width = 10
}