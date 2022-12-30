package io.github.yuko1101.asterium.mixin.hooks.network

import io.github.yuko1101.asterium.events.impl.PacketEvent
import io.netty.channel.ChannelHandlerContext
import net.minecraft.network.Packet
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

fun onReceivePacket(context: ChannelHandlerContext, packet: Packet<*>, ci: CallbackInfo) {
    if (PacketEvent.ReceiveEvent(packet).postAndCatch()) ci.cancel()
}