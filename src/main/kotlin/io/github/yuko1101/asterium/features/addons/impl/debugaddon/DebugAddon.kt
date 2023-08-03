package io.github.yuko1101.asterium.features.addons.impl.debugaddon

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.AsteriumAddon
import java.util.*

class DebugAddon : AsteriumAddon() {
    override val addonMetaData = AddonMetaData(
        name ="Debug Addon",
        uuid = UUID.fromString("6d94fedf-1656-4824-a757-e67c2bcb09b5"),
        description = "Debug Addon for Asterium",
        version = "1.0.0",
        author = "yuko1101",
    )

    override fun onEnable(): String? {
        instance = this
        Asterium.hudManager.register(TestHud())
        Asterium.hudManager.register(TestHud2())

        return null
    }

    companion object {
        lateinit var instance: AsteriumAddon
    }
}