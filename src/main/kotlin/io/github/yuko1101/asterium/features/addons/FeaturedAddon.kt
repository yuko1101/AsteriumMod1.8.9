package io.github.yuko1101.asterium.features.addons

import gg.essential.vigilance.Vigilant

abstract class FeaturedAddon {

    //triggers at FMLInitializationEvent
    open fun init() {}

    open fun shutdown() {}

    abstract fun addonMetaData(): AddonMetaData

    fun getAddonMetaData(): AddonMetaData {
        return addonMetaData().apply { addon = this@FeaturedAddon }
    }

    open fun config(): Vigilant? {
        return null
    }
}