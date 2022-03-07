package io.github.yuko1101.asterium.features.addons

import io.github.yuko1101.asterium.Asterium
import net.minecraft.client.Minecraft
import net.minecraft.entity.passive.EntityBat
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class EventEmitter {
    companion object {
        private val mc = Minecraft.getMinecraft()

        @SubscribeEvent
        fun onClientChatReceiveEvent(event: ClientChatReceivedEvent) {
            getListeners().forEach {listener -> listener.onClientChatReceiveEvent(event) }
        }
        @SubscribeEvent
        fun onTickEvent(event: TickEvent) {
            getListeners().forEach {listener -> listener.onTickEvent(event) }
        }
        @SubscribeEvent
        fun onPlayerTickEvent(event: TickEvent.PlayerTickEvent) {
            getListeners().forEach {listener -> listener.onPlayerTickEvent(event) }
        }
        @SubscribeEvent
        fun onRenderWorldLastEvent(event: RenderWorldLastEvent) {
            getListeners().forEach {listener -> listener.onRenderWorldLastEvent(event) }
        }
        @SubscribeEvent
        fun onRenderLivingEvent(event: RenderLivingEvent<*>) {
            getListeners().forEach {listener -> listener.onRenderLivingEvent(event) }
        }


        private fun getListeners(): List<ExtraEventListener> {
            val listeners = arrayListOf<ExtraEventListener>()
            AddonManager.getAddonMetaDataList().forEach { addonMetaData -> listeners.addAll(addonMetaData.eventListeners) }
            return listeners
        }
//        @SubscribeEvent
//        fun onRenderLivingEventBat(event: RenderLivingEvent<EntityBat>) {
//            Asterium.listeners.forEach { listener -> listener.onRenderLivingEventBat(event) }
//        }
    }



}