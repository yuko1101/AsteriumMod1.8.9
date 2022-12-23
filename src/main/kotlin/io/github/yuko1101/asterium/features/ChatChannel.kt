package io.github.yuko1101.asterium.features

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.utils.FileManager
import io.github.yuko1101.asterium.utils.FileUtils.Companion.getFromJson
import io.github.yuko1101.asterium.utils.FileUtils.Companion.setToJson
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.GuiChat
import net.minecraftforge.client.event.ClientChatReceivedEvent
import java.io.File

class ChatChannel {
    companion object {

        private var chatChannel = ""
        private var lastPrivatePlayer = ""

        private val font: FontRenderer = Minecraft.getMinecraft().fontRendererObj

        private const val x = 3
        private var y = Asterium.scaledResolution.scaledHeight - font.FONT_HEIGHT * 2 - 5

        fun onRender() {

            lastPrivatePlayer =
                File(FileManager.clientDirectory, "data.json").getFromJson("lastPrivatePlayer", lastPrivatePlayer)
            chatChannel = File(FileManager.clientDirectory, "data.json").getFromJson("lastChatChannel", chatChannel)
            y = Asterium.scaledResolution.scaledHeight - font.FONT_HEIGHT * 2 - 5
            val screen = Minecraft.getMinecraft().currentScreen
            if (screen !is GuiChat) return
            screen.drawString(font, when (chatChannel) {
                "party" -> "§9[Party]"
                "guild" -> "§2[Guild]"
                "officer" -> "§3[Officer]"
                "all" -> "§c[All]"
                "private" -> "§d[Private] §8> §r$lastPrivatePlayer"
                else -> ""
            }, x, y, 0xFFFFFF)
        }

        fun onChat(event: ClientChatReceivedEvent) {
            val preChatChannel = chatChannel
            val preLastPrivatePlayer = lastPrivatePlayer
            chatChannel = when (event.message.formattedText) {
                "§aYou are now in the §r§6ALL§r§a channel§r" -> "all"
                "§cThe conversation you were in expired and you have been moved back to the ALL channel.§r" -> "all"
                "§cYou are not in a party and were moved to the ALL channel.§r" -> "all"
                "§aYou are now in the §r§6GUILD§r§a channel§r" -> "guild"
                "§aYou are now in the §r§6OFFICER§r§a channel§r" -> "officer"
                "§aYou are now in the §r§6PARTY§r§a channel§r" -> "party"
                else -> chatChannel
            }
            if (event.message.formattedText.matches("§aOpened a chat conversation with (.*)§a for the next 5 minutes. Use §r§b/chat a§r§a to leave§r".toRegex())) {
                lastPrivatePlayer = event.message.formattedText.replace("§aOpened a chat conversation with (.*)§a for the next 5 minutes. Use §r§b/chat a§r§a to leave§r".toRegex(), "$1")
                chatChannel = "private"
            }
            if (preChatChannel != chatChannel) File(FileManager.clientDirectory, "data.json").setToJson("lastChatChannel", chatChannel)
            if (preLastPrivatePlayer != lastPrivatePlayer) File(FileManager.clientDirectory, "data.json").setToJson("lastPrivatePlayer", lastPrivatePlayer)
        }
    }
}