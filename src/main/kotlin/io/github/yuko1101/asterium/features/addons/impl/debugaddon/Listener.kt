package io.github.yuko1101.asterium.features.addons.impl.debugaddon

import io.github.yuko1101.asterium.events.impl.PacketEvent
import io.github.yuko1101.asterium.features.addons.ExtraEventListener
import io.github.yuko1101.asterium.mixin.extensions.ExtensionC01PacketChatMessage
import net.minecraft.network.play.client.C01PacketChatMessage

class Listener : ExtraEventListener {
    override fun onPacketEventSendEvent(event: PacketEvent.SendEvent) {
        if (event.packet is C01PacketChatMessage) {
            val packet = event.packet as ExtensionC01PacketChatMessage
            packet.setMessage("a")
        }
    }
}