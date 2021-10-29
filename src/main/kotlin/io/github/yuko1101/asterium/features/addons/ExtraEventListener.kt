package io.github.yuko1101.asterium.features.addons

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

interface ExtraEventListener {

    fun onClientChatReceiveEvent(event: ClientChatReceivedEvent) {}
    fun onTickEvent(event: TickEvent) {}
    fun onPlayerTickEvent(event: TickEvent.PlayerTickEvent) {}
    fun onRenderWorldLastEvent(event: RenderWorldLastEvent) {}


}