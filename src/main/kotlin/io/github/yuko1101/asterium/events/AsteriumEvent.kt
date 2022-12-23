package io.github.yuko1101.asterium.events

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.utils.minecraft.ChatLib
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event

abstract class AsteriumEvent : Event() {
    val eventName by lazy {
        this::class.simpleName
    }

    fun postAndCatch(): Boolean {
        return runCatching {
            MinecraftForge.EVENT_BUS.post(this)
        }.onFailure {
            it.printStackTrace()
            ChatLib.chat("§cAsterium ${Asterium.VERSION} caught an ${it::class.simpleName ?: "error"} at ${eventName}.")
        }.getOrDefault(isCanceled)
    }
}