package io.github.yuko1101.asterium.utils

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse

object InventoryUtils {
    private val listeners = arrayListOf<Pair<(GuiContainer) -> Boolean, (Int, ItemStack) -> Boolean>>()
    private val temporaryListeners = arrayListOf<Pair<(GuiContainer) -> Boolean, (Int, ItemStack) -> Boolean>>()

    @Suppress("unused")
    fun onClick(guiCheck: (guiContainer: GuiContainer) -> Boolean, callback: (slot: Int, item: ItemStack) -> Boolean, removeOnGuiChange: Boolean = true) {
        if (removeOnGuiChange) {
            temporaryListeners.add(Pair(guiCheck, callback))
        } else {
            listeners.add(Pair(guiCheck, callback))
        }
    }

    @SubscribeEvent
    fun onMouseInputEvent(event: GuiScreenEvent.MouseInputEvent.Pre) {
        if (!Mouse.isButtonDown(0)) return
        if (event.gui is GuiContainer) {
            val allListeners = listeners + temporaryListeners
            for (listener in allListeners) {
                if (listener.first(event.gui as GuiContainer)) {
                    val slot = (event.gui as GuiContainer).slotUnderMouse
                    if (slot != null && slot.hasStack) {
                        listener.second(slot.slotIndex, slot.stack)
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