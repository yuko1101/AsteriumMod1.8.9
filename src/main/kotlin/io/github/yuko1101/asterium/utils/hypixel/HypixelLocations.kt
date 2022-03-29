package io.github.yuko1101.asterium.utils.hypixel

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.utils.StringUtils.withoutColorCodes
import io.github.yuko1101.asterium.utils.minecraft.ScoreboardUtils

object HypixelLocations {
    val isOnHypixel: Boolean
        get() = Asterium.getServerIP()?.lowercase()?.contains("hypixel") ?: false

    val inSkyblock: Boolean
        get() = isOnHypixel && ScoreboardUtils.getSidebarTitle()?.withoutColorCodes()?.contains("SKYBLOCK") ?: false

    val inCatacombs: Boolean
        get() = inSkyblock && ScoreboardUtils.getSidebarLines().any { line -> line.withoutColorCodes().contains("The Catacombs") } ?: false

    val inDungeons: Boolean
        get() = inSkyblock && (inCatacombs) // if new type of dungeons added, add here like this: inSkyblock && (inCatacombs || inNewType)

//    val inSkyblockHub: Boolean
//        get()
}