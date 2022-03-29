package io.github.yuko1101.asterium.utils.hypixel.skyblock.dungeon

import gg.essential.api.utils.Multithreading
import gg.essential.universal.UChat
import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.Asterium.Companion.mc
import io.github.yuko1101.asterium.features.addons.Intents
import io.github.yuko1101.asterium.utils.ChatLib
import io.github.yuko1101.asterium.utils.NumberUtil.romanToDecimal
import io.github.yuko1101.asterium.utils.StringUtils.withoutColorCodes
import io.github.yuko1101.asterium.utils.hypixel.HypixelLocations
import io.github.yuko1101.asterium.utils.minecraft.TabListUtils
import io.github.yuko1101.asterium.utils.minecraft.TabListUtils.text
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object DungeonMembers {
    val team = HashSet<DungeonTeammate>()
    val deads = HashSet<DungeonTeammate>()

    private val partyCountPattern = Regex("§r {9}§r§b§lParty §r§f\\((?<count>[1-5])\\)§r")
    private val classPattern =
        Regex("§r(?:§.)+(?:\\[.+] )?(?<name>\\w+?)(?:§.)* (?:§r(?:§[\\da-fklmno]){1,2}.+ )?§r§f\\(§r§d(?:(?<class>Archer|Berserk|Healer|Mage|Tank) (?<lvl>\\w+)|§r§7EMPTY)§r§f\\)§r")

    fun onChat(event: ClientChatReceivedEvent) {
        if (!Intents.dungeonTeammates) return
        if (!HypixelLocations.inDungeons) return
        if (event.message.formattedText.withoutColorCodes().startsWith("Dungeon starts in 1 second.")) {
            team.clear()
            Multithreading.runAsync {
                Thread.sleep(2000)
                attempts = 0
                fetchTeammates()
            }
        }
    }

    private var ticks = 0

    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!Intents.dungeonTeammates) return
        if (!HypixelLocations.inDungeons) return
        if (event.phase != TickEvent.Phase.START) return
        if (ticks % 2 == 0) {
            val tabEntries = TabListUtils.tabEntries
            for (teammate in team) {
                if (tabEntries.size <= teammate.tabEntryIndex) continue
                val entry = tabEntries[teammate.tabEntryIndex].second
                if (!entry.contains(teammate.playerName)) continue
                teammate.player = mc.theWorld.playerEntities.find {
                    it.name == teammate.playerName && it.uniqueID.version() == 4
                }
                teammate.dead = entry.endsWith("§r§cDEAD§r§f)§r")
                if (teammate.dead) {
                    if (deads.add(teammate)) {
                        teammate.deaths++
                    }
                } else {
                    deads.remove(teammate)
                }
            }
        }
        ticks++
        if (ticks == 4) ticks = 0
    }

    var attempts = 0
    private fun fetchTeammates() {
        attempts++
        ChatLib.chat("fetching teammates...")
        if (attempts > 5) {
            ChatLib.chat("failed to fetch teammates")
            return
        }
        if (team.isNotEmpty() || !HypixelLocations.inDungeons) return
        val tabEntries = TabListUtils.tabEntries

        if (tabEntries.isEmpty() || !tabEntries[0].second.contains("§r§b§lParty §r§f(")) {
            Multithreading.runAsync {
                Thread.sleep(1000)
                fetchTeammates()
            }
            return
        }

        val partyCount = partyCountPattern.find(tabEntries[0].second)?.groupValues?.get(1)?.toIntOrNull()
        if (partyCount == null) {
            println("Couldn't get party count")
            Multithreading.runAsync {
                Thread.sleep(1000)
                fetchTeammates()
            }
            return
        }
        println("There are $partyCount members in this party")
        for (i in 0 until partyCount) {
            val pos = 1 + i * 4
            val text = tabEntries[pos].second
            val matcher = classPattern.find(text)
            if (matcher == null) {
                println("Skipping over entry $text due to it not matching")
                continue
            }
            val name = matcher.groups["name"]!!.value
            if (matcher.groups["class"] != null) {
                val dungeonClass = matcher.groups["class"]!!.value
                val classLevel = matcher.groups["lvl"]!!.value.romanToDecimal()
                println("Parsed teammate $name, they are a $dungeonClass $classLevel")
                team.add(
                    DungeonTeammate(
                        name,
                        DungeonClass.getClassFromName(
                            dungeonClass
                        ), classLevel,
                        pos
                    )
                )
            } else {
                println("Parsed teammate $name with value EMPTY, $text")
                team.add(
                    DungeonTeammate(
                        name,
                        DungeonClass.EMPTY, 0,
                        pos
                    )
                )
            }
        }
        if (partyCount != team.size) {
            UChat.chat("§9§lAsterium §8» §cSomething isn't right! I expected $partyCount members but only got ${team.size}")
        }
        if (team.any { it.dungeonClass == DungeonClass.EMPTY }) {
            UChat.chat("§9§lAsterium §8» §cSomething isn't right! One or more of your party members has an empty class! Could the server be lagging?")
        }
//        CooldownTracker.updateCooldownReduction()
//        checkSpiritPet()
    }


    class DungeonTeammate(
        val playerName: String,
        val dungeonClass: DungeonClass,
        val classLevel: Int,
        val tabEntryIndex: Int
    ) {
        var player: EntityPlayer? = null
        var dead = false
        var deaths = 0

        fun canRender() = player != null && player!!.health > 0 && !dead
    }
    enum class DungeonClass {
        ARCHER,
        MAGE,
        BERSERK,
        TANK,
        HEALER,
        EMPTY;

        companion object {
            fun getClassFromName(name: String): DungeonClass {
                return when (name.uppercase()) {
                    "ARCHER" -> ARCHER
                    "MAGE" -> MAGE
                    "BERSERK" -> BERSERK
                    "TANK" -> TANK
                    "HEALER" -> HEALER
                    else -> EMPTY
                }
            }
        }
    }
}