package io.github.yuko1101.asterium.listener

import io.github.yuko1101.asterium.features.ChatChannel
import io.github.yuko1101.asterium.utils.minecraft.SubRenderUtils
import io.github.yuko1101.asterium.utils.hypixel.skyblock.dungeon.DungeonMembers
import net.minecraft.entity.item.EntityArmorStand
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object Listener {
    @SubscribeEvent
    fun onRenderArmorStand(event: RenderLivingEvent.Pre<EntityArmorStand>) {
        SubRenderUtils.onRenderArmorStand(event)
    }

    @SubscribeEvent
    fun onClientChatReceived(event: ClientChatReceivedEvent) {
        DungeonMembers.onChat(event)
        ChatChannel.onChat(event)
    }

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        DungeonMembers.onTick(event)
    }
}