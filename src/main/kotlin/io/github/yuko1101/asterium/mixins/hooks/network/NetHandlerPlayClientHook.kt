package io.github.yuko1101.asterium.mixins.hooks.network

import io.github.yuko1101.asterium.events.impl.PacketEvent
import net.minecraft.network.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun onSendPacket(packet: Packet<*>, ci: CallbackInfo) {
//    println("onSendPacket")
    if (PacketEvent.SendEvent(packet).postAndCatch()) ci.cancel()
}