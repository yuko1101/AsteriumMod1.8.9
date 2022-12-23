package io.github.yuko1101.asterium.utils.minecraft

import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.Scoreboard

object ScoreboardUtils {
    private val mc = Minecraft.getMinecraft()

    fun getSidebarLines() : List<String> {
        val sidebar = Minecraft.getMinecraft().theWorld.scoreboard.getObjectiveInDisplaySlot(1) ?: return emptyList()
        val scores = Minecraft.getMinecraft().theWorld.scoreboard.scores ?: return emptyList()
        return scores.filter { score -> score.objective.name == sidebar.name }.sortedBy { score -> score.scorePoints }.map { score -> getSuffixFromContainingTeam(Minecraft.getMinecraft().theWorld.scoreboard, score.playerName) }
    }
    private fun getSuffixFromContainingTeam(scoreboard: Scoreboard, member: String): String {
        var suffix: String? = null
        for (team in scoreboard.teams) {
            if (team.membershipCollection.contains(member)) {
                suffix = team.colorPrefix + team.colorSuffix
                break
            }
        }
        return suffix ?: ""
    }

    fun getSidebarTitle(): String? {
        val scoreboard = mc.theWorld?.scoreboard ?: return null
        val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return null
        return objective.displayName
    }

}