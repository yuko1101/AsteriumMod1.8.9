package io.github.yuko1101.asterium.events.impl

import io.github.yuko1101.asterium.events.AsteriumEvent
import net.minecraft.network.INetHandler
import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Cancelable

@Cancelable
data class MainReceivePacketEvent<T : Packet<U>, U : INetHandler>(val handler: U, val packet: T) : AsteriumEvent()