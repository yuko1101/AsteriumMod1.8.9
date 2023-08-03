package io.github.yuko1101.asterium.events

import io.github.yuko1101.asterium.Asterium
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object EventEmitter {
    @SubscribeEvent
    fun onEvent(event: Event) {
        val listeners = Asterium.eventManager.getListeners(event::class.java)

        for (listener in listeners) {
            try {
                listener.target.invoke(listener.source, event)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}