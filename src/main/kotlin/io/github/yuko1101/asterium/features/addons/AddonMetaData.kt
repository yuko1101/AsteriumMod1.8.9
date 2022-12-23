package io.github.yuko1101.asterium.features.addons

import java.util.*


class AddonMetaData(val name: String, val version: String = "1.0.0", val description: String = "No description.", val eventListeners: List<ExtraEventListener> = listOf(), val unloadable: Boolean = true, val uuid: UUID = UUID.randomUUID()) {
    var initialized = false
    lateinit var addon: FeaturedAddon
}