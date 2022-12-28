package io.github.yuko1101.asterium.features.addons.impl.huds

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.AddonMetaData
import io.github.yuko1101.asterium.features.addons.FeaturedAddon
import java.util.*

class Huds : FeaturedAddon() {
    override fun addonMetaData(): AddonMetaData = AddonMetaData("Huds", uuid = UUID.fromString("6d94fedf-1656-4824-a757-e67c2bcb09b5"))

    override fun init() {
        instance = this
        Asterium.hudManager.register(TestHud())
    }

    companion object {
        lateinit var instance: FeaturedAddon
    }
}