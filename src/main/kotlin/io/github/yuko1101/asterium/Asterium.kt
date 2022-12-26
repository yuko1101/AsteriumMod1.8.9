package io.github.yuko1101.asterium

import gg.essential.api.EssentialAPI
import io.github.yuko1101.asterium.commands.AsteriumCommand
import io.github.yuko1101.asterium.config.AsteriumConfig
import io.github.yuko1101.asterium.features.ChatChannel
import io.github.yuko1101.asterium.features.addons.*
import io.github.yuko1101.asterium.features.addons.arrowPath.ArrowPath
import io.github.yuko1101.asterium.features.addons.hud.HUDManager
import io.github.yuko1101.asterium.listener.Listener
import io.github.yuko1101.asterium.utils.FileManager
import io.github.yuko1101.asterium.utils.minecraft.InventoryUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.multiplayer.ServerData
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod(modid = Asterium.MODID, version = Asterium.VERSION)
class Asterium {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        FileManager.init()

        AddonManager.addons.add(AddonCore(listOf(ArrowPath().getAddonMetaData()), null))
        AddonManager.refreshAddons()

        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(EventEmitter)
        MinecraftForge.EVENT_BUS.register(InventoryUtils)
        MinecraftForge.EVENT_BUS.register(Listener)
//        addons.forEach{ addonMetaData -> MinecraftForge.EVENT_BUS.register(addonMetaData.addon) }
        EssentialAPI.getCommandRegistry().registerCommand(AsteriumCommand())

        Runtime.getRuntime().addShutdownHook(object : Thread("ShutDown") {
            override fun run() {
                AddonManager.getAddonMetaDataList().forEach {
                    it.addon.shutdown()
                }
            }
        })

        EssentialAPI.getNotifications().push("Asterium Addons", "Loaded ${AddonManager.getAddonMetaDataList().size} addons!")
    }

    @Mod.EventHandler
    fun start(event: FMLServerStartingEvent) {

    }

    companion object {
        @JvmStatic
        val mc: Minecraft = Minecraft.getMinecraft()

        const val MODID = "asterium"
        const val VERSION = "0.2.0"

        @JvmStatic
        var config = AsteriumConfig()

        var API_KEY = arrayListOf<String>()
        var keyNumber = -1

        var scaledResolution = ScaledResolution(mc)

        @JvmStatic
        val hudManager = HUDManager()

        fun getServerIP(): String? {
            val server: ServerData? = Minecraft.getMinecraft().currentServerData
            return server?.serverIP
        }




        fun refresh() {
            AddonManager.refreshAddons()
        }

        fun refreshConfig() {
            config = AsteriumConfig()
            config.initAddons()
            config.initialize()
        }



    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Post) {
        scaledResolution = ScaledResolution(mc)
    }

    @SubscribeEvent
    fun onRenderText(event: RenderGameOverlayEvent.Text) {
        ChatChannel.onRender()
    }
}


