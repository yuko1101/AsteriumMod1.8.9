package io.github.yuko1101.asterium.features.addons

import gg.essential.vigilance.Vigilant
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.*

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