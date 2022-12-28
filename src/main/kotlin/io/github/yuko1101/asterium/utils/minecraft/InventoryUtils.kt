package io.github.yuko1101.asterium.utils.minecraft

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse

object InventoryUtils {
    private val listeners = arrayListOf<Triple<List<Int>, (GuiContainer) -> Boolean, (Int, ItemStack) -> Boolean>>()
    private val temporaryListeners = arrayListOf<Triple<List<Int>, (GuiContainer) -> Boolean, (Int, ItemStack) -> Boolean>>()

    @Suppress("unused")
    fun onClick(guiCheck: (guiContainer: GuiContainer) -> Boolean, callback: (slot: Int, item: ItemStack) -> Boolean, mouseButtons: List<Int> = listOf(0), removeOnGuiChange: Boolean = true) {
        if (removeOnGuiChange) {
            temporaryListeners.add(Triple(mouseButtons, guiCheck, callback))
        } else {
            listeners.add(Triple(mouseButtons, guiCheck, callback))
        }
    }

    @SubscribeEvent
    fun onMouseInputEvent(event: GuiScreenEvent.MouseInputEvent.Pre) {
//        println("Mouse ${Mouse.getEventButton()} is pressed")
        if (event.gui is GuiContainer) {
            val allListeners = listeners + temporaryListeners
            for (listener in allListeners) {

                // button check
                if (!listener.first.any { button -> Mouse.getEventButton() == button }) continue

                // gui check
                if (listener.second(event.gui as GuiContainer)) {
                    val slot = (event.gui as GuiContainer).slotUnderMouse
                    if (slot != null && slot.hasStack) {
                        listener.third(slot.slotIndex, slot.stack)
                    }
                }
            }
        }
    }
    @SubscribeEvent
    fun onGuiChanged(event: GuiOpenEvent) {
        temporaryListeners.clear()
    }
}