package io.github.yuko1101.asterium.utils.minecraft

import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent

object ChatLib {
    private val mc = Minecraft.getMinecraft()
    fun chat(text: String?) {
        if (text == null) return
        mc.thePlayer.addChatMessage(ChatComponentText(text))
    }

    fun say(text: String?) {
        if (text == null) return
        mc.thePlayer.sendChatMessage(text)
    }

    fun command(text: String?) {
        if (text == null) return
        mc.thePlayer.sendChatMessage("/$text")
    }
    fun chatComponent(component: IChatComponent?) {
        if (component == null) return
        mc.thePlayer.addChatComponentMessage(component)
    }
}