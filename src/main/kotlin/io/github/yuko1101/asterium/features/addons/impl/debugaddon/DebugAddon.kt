package io.github.yuko1101.asterium.features.addons.impl.debugaddon

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.AddonMetaData
import io.github.yuko1101.asterium.features.addons.FeaturedAddon
import java.util.*

class DebugAddon : FeaturedAddon() {
    override fun addonMetaData(): AddonMetaData = AddonMetaData("Debug Addon", uuid = UUID.fromString("6d94fedf-1656-4824-a757-e67c2bcb09b5"), eventListeners = listOf(Listener()))

    override fun init() {
        instance = this
        Asterium.hudManager.register(TestHud())
        Asterium.hudManager.register(TestHud2())
    }

    companion object {
        lateinit var instance: FeaturedAddon
    }
}