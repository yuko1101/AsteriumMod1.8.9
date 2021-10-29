package io.github.yuko1101.asterium

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.Notifications
import io.github.yuko1101.asterium.commands.AsteriumCommand
import io.github.yuko1101.asterium.config.AsteriumConfig
import io.github.yuko1101.asterium.features.ChatChannel
import io.github.yuko1101.asterium.features.addons.AddonMetaData
import io.github.yuko1101.asterium.features.addons.EventEmitter
import io.github.yuko1101.asterium.features.addons.ExtraEventListener
import io.github.yuko1101.asterium.features.addons.FeaturedAddon
import io.github.yuko1101.asterium.features.addons.arrowPath.ArrowPath
import io.github.yuko1101.asterium.utils.AddonClassLoader
import io.github.yuko1101.asterium.utils.FileManager
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.multiplayer.ServerData
import net.minecraftforge.client.event.ClientChatReceivedEvent
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

        refreshAddons()

        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(EventEmitter)
//        addons.forEach{ addonMetaData -> MinecraftForge.EVENT_BUS.register(addonMetaData.addon) }
        EssentialAPI.getCommandRegistry().registerCommand(AsteriumCommand())

        Runtime.getRuntime().addShutdownHook(object : Thread("ShutDown") {
            override fun run() {
                addons.forEach { addonMetaData -> addonMetaData.addon.shutdown() }
            }
        })

        EssentialAPI.getNotifications().push("Asterium Addons", "Loaded ${addons.size} addons!")
    }

    @Mod.EventHandler
    fun start(event: FMLServerStartingEvent) {

    }

    companion object {
        private val mc: Minecraft = Minecraft.getMinecraft()

        const val MODID = "Asterium"
        const val VERSION = "0.2.0"
        var config = AsteriumConfig()

        var API_KEY = arrayListOf<String>()
        var keyNumber = -1

        var scaledResolution = ScaledResolution(mc)

        fun getIP(): String {
            val server: ServerData = Minecraft.getMinecraft().currentServerData ?: return ""
            return server.serverIP
        }

        var addons = arrayListOf<AddonMetaData>()
        var addonClassLoaders = arrayListOf<AddonClassLoader>()


        fun refresh() {
            refreshAddons()
        }

        private fun refreshAddons() {
            config = AsteriumConfig()

            unloadExternalAddons()
            loadExternalAddons()
            addons.add(ArrowPath().addonMetaData())

            config.initAddons()
            config.initialize()
        }

        fun unloadExternalAddons() {
            addonClassLoaders.filter { addonClassLoader -> shouldUnload(addonClassLoader) }.forEach { addonClassLoader -> addonClassLoader.unload();println("[Asterium Addons] Unloaded ${addonClassLoader.loadedClasses}") }
            addonClassLoaders = addonClassLoaders.filterNot { addonClassLoader -> shouldUnload(addonClassLoader) } as ArrayList<AddonClassLoader>
            addonClassLoaders.forEach { addonClassLoader -> println("[Asterium Addons] Skipped unloading ${addonClassLoader.loadedClasses}")}
            addons.forEach { addonMetaData -> addonMetaData.addon.shutdown() }
            addons = addons.filterNot { addonMetaData -> shouldAddonUnload(addonMetaData) } as ArrayList<AddonMetaData>
        }

        private fun shouldAddonUnload(addonMetaData: AddonMetaData): Boolean {
            return addonClassLoaders.all { addonClassLoader -> !addonClassLoader.loadedClasses.contains(addonMetaData.addon::class.toString()) }
        }

        private fun shouldUnload(addonClassLoader: AddonClassLoader): Boolean {
            return !(addons.filterNot { addonMetaData -> addonMetaData.unloadable }.map { addonMetaData: AddonMetaData -> addonMetaData.addon::class.toString() }.any{ path -> addonClassLoader.loadedClasses.contains(path) })
        }


        private fun loadExternalAddons() {
            val addonFiles = FileManager.getAddonsDirectory().listFiles { file -> file.extension == "jar" } ?: return
            addonFiles.forEach { file ->
                if (addonClassLoaders.any { addonClassLoader -> addonClassLoader.jarPath == file.absolutePath }) {
                    println("[Asterium Addons] Skipped loading $file because it has already loaded")
                } else {
                    println("[Asterium Addons] Loading $file")
                    val addonClassLoader = AddonClassLoader(file.absolutePath)
                    addons.addAll(addonClassLoader.loadClassesInJar().map { featuredAddon: FeaturedAddon -> featuredAddon.addonMetaData() })
                    addonClassLoaders.add(addonClassLoader)
                }

//            val load :URLClassLoader = URLClassLoader.newInstance(arrayOf<URL>(file.toURI().toURL()))
//            val cl = load.loadClass("asterium.${file.nameWithoutExtension.split("-").first()
//                .lowercase(Locale.getDefault())}.${file.nameWithoutExtension.split("-").first()}")
//            println("[Asterium Addons] Loading File from $file / asterium.${file.nameWithoutExtension.split("-").first()
//                .lowercase(Locale.getDefault())}.${file.nameWithoutExtension.split("-").first()}")
//            if (cl != null) {
//                println("Found!")
//                println(cl)
//                if () {
//                    println("Added!")
//                    addons.add(cl.addonMetaData())
//                }
//            }
            }
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

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        ChatChannel.onChat(event)
    }




}


