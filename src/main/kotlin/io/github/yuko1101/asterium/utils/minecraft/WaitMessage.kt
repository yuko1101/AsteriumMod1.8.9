package io.github.yuko1101.asterium.utils.minecraft

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class WaitMessage {
    companion object {
        private val storage = arrayListOf<Pair<Regex, (ClientChatReceivedEvent) -> Any?>>()

        fun register(regex: Regex, func: (ClientChatReceivedEvent) -> Any? ) {
            storage.add(Pair(regex, func))
        }

        @SubscribeEvent
        fun onChat(event: ClientChatReceivedEvent) {
            storage.filter { pair -> event.message.formattedText.matches(pair.first) }.forEach { pair ->
                pair.second(event)
                storage.remove(pair)
            }
        }

    }
}