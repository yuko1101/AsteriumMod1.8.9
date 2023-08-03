package io.github.yuko1101.asterium.features.addons

import gg.essential.vigilance.Vigilant
import java.util.*

abstract class AsteriumAddon {

    abstract val addonMetaData: AddonMetaData

    /**
     * @return An error message that the addon could not be activated. Return null if it succeeds.
     */
    open fun onEnable(): String? {
        return null
    }

    /**
     * @return An error message that the addon could not be deactivated. Return null if it succeeds.
     */
    open fun onDisable(): String? {
        return null
    }

    open fun config(): Vigilant? {
        return null
    }

    var addonFile: AddonFile? = null

    val id: String
        get() = "${addonMetaData.name}-${addonMetaData.uuid}"

    class AddonMetaData(val name: String, val uuid: UUID, val description: String, val version: String, val author: String, val url: String?) {
        constructor(name: String, uuid: UUID, description: String, version: String, author: String) : this(name, uuid, description, version, author, null)

        var initialized: Boolean = false
    }
}