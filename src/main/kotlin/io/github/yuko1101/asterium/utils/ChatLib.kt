package io.github.yuko1101.asterium.utils

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent

object ChatLib {
    private val mc = Minecraft.getMinecraft()
    fun chat(text: String?) {
        mc.thePlayer.addChatMessage(ChatComponentText(text))
    }

    fun say(text: String?) {
        mc.thePlayer.sendChatMessage(text)
    }

    fun command(text: String) {
        mc.thePlayer.sendChatMessage("/$text")
    }
    fun chatComponent(component: IChatComponent) {
        mc.thePlayer.addChatComponentMessage(component)
    }
}