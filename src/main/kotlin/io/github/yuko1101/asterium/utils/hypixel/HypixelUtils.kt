package io.github.yuko1101.asterium.utils.hypixel

import io.github.yuko1101.asterium.Asterium
import net.minecraft.util.EnumChatFormatting
import kotlin.math.min

object HypixelUtils {
    fun getPlayerName(displayName: String): String {
        return EnumChatFormatting.getTextWithoutFormattingCodes(displayName.trim().split(" ")[min(2, displayName.trim().split(" ").size) - 1])
    }

    fun getAPIKey(): String {
        if (Asterium.API_KEY.size == 0) return ""
        Asterium.keyNumber++
        if (Asterium.keyNumber >= Asterium.API_KEY.size) {
            Asterium.keyNumber = 0
        }
        return Asterium.API_KEY[Asterium.keyNumber]
    }


}