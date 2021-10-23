package io.github.yuko1101.asterium

import gg.essential.api.EssentialAPI
import io.github.yuko1101.asterium.commands.AsteriumCommand
import io.github.yuko1101.asterium.config.AsteriumConfig
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent


@Mod(modid = Asterium.MODID, version = Asterium.VERSION)
class Asterium {
    private val mc: Minecraft = Minecraft.getMinecraft()


    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        config.initialize()

        MinecraftForge.EVENT_BUS.register(this)
        EssentialAPI.getCommandRegistry().registerCommand(AsteriumCommand())
    }

    @Mod.EventHandler
    fun start(event: FMLServerStartingEvent) {

    }


    companion object {
        const val MODID = "Asterium"
        const val VERSION = "0.2.0"
        val config = AsteriumConfig

    }

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
    }
}


