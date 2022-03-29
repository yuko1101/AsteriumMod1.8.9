package io.github.yuko1101.asterium.features.addons

import io.github.yuko1101.asterium.Asterium

class AddonMetaData(val addon: FeaturedAddon, val name: String, val version: String = "1.0.0", val description: String = "No description.", val eventListeners: List<ExtraEventListener> = listOf(), val unloadable: Boolean = true) {
    var initialized = false
}