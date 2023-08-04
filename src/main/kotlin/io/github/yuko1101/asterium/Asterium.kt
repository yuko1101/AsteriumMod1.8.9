package io.github.yuko1101.asterium

import com.google.gson.JsonObject
import gg.essential.api.EssentialAPI
import io.github.yuko1101.asterium.commands.AsteriumCommand
import io.github.yuko1101.asterium.config.AsteriumConfig
import io.github.yuko1101.asterium.events.EventEmitter
import io.github.yuko1101.asterium.events.EventManager
import io.github.yuko1101.asterium.features.ChatChannel
import io.github.yuko1101.asterium.features.addons.*
import io.github.yuko1101.asterium.features.addons.impl.arrowPath.ArrowPath
import io.github.yuko1101.asterium.features.addons.impl.debugaddon.DebugAddon
import io.github.yuko1101.asterium.features.hud.HUDFeature
import io.github.yuko1101.asterium.listener.Listener
import io.github.yuko1101.asterium.utils.ConfigFile
import io.github.yuko1101.asterium.utils.FileManager
import io.github.yuko1101.asterium.utils.ObjectManager
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
import org.apache.logging.log4j.LogManager
import java.io.File

@Mod(modid = Asterium.MOD_ID, version = Asterium.VERSION)
class Asterium {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        addonManager.loadAll(listOf(ArrowPath(), DebugAddon()), updateConfig = false)
        addonManager.loadFromAddonDir(updateConfig = false)
        addonManager.updateAddonsConfig()

        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(EventEmitter)
        MinecraftForge.EVENT_BUS.register(InventoryUtils)
        MinecraftForge.EVENT_BUS.register(Listener)
        EssentialAPI.getCommandRegistry().registerCommand(AsteriumCommand())

        Runtime.getRuntime().addShutdownHook(object : Thread("Asterium shutdown") {
            override fun run() {
                logger.info("[Asterium] Shutdown")
                addonManager.unloadAll()
            }
        })

        EssentialAPI.getNotifications().push("Asterium Addons", "Loaded ${addonManager.registered.size} addons!")
    }

    @Mod.EventHandler
    fun start(event: FMLServerStartingEvent) {

    }

    companion object {
        @JvmStatic
        val mc: Minecraft = Minecraft.getMinecraft()

        const val MOD_ID = "asterium"
        const val VERSION = "0.2.0"

        var API_KEY = arrayListOf<String>()
        var keyNumber = -1

        var scaledResolution = ScaledResolution(mc)

        @JvmStatic
        val logger = LogManager.getLogger()

        @JvmStatic
        val hudManager = ObjectManager<HUDFeature>()
        @JvmStatic
        val fileManager = FileManager()
        @JvmStatic
        val addonManager = AddonManager()
        @JvmStatic
        val eventManager = EventManager()

        @JvmStatic
        var config = AsteriumConfig()
        @JvmStatic
        val configFile = ConfigFile(File(fileManager.rootDir, "data.json"), JsonObject())

        fun getServerIP(): String? {
            val server: ServerData? = Minecraft.getMinecraft().currentServerData
            return server?.serverIP
        }




        fun refresh() {
            addonManager.reloadAll()
        }

        fun refreshConfig() {
            config = AsteriumConfig()
            config.updateAddons()
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


