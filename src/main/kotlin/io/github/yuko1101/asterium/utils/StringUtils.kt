/*
 * Most of the code in this file is taken from Skytils
 * Link: https://github.com/Skytils/SkytilsMod/blob/e6fd3bf86cf98cb5b74221bf8a27cff38fcd0d40/src/main/kotlin/skytils/skytilsmod/utils/StringUtils.kt
 *
 * The license below is from the original code
 */

/*
 * Skytils - Hypixel Skyblock Quality of Life Mod
 * Copyright (C) 2022 Skytils
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package io.github.yuko1101.asterium.utils

import gg.essential.universal.wrappers.message.UTextComponent
import net.minecraft.util.EnumChatFormatting
import org.apache.commons.lang3.StringUtils as ApacheStringUtils

object StringUtils {
    fun CharSequence?.countMatches(subString: CharSequence): Int = ApacheStringUtils.countMatches(this, subString)

    fun String?.stripControlCodes(): String = UTextComponent.stripFormatting(this ?: "")

    fun CharSequence?.startsWithAny(vararg sequences: CharSequence?) = ApacheStringUtils.startsWithAny(this, *sequences)
    fun CharSequence.startsWithAny(sequences: Iterable<CharSequence>): Boolean = sequences.any { contains(it) }
    fun CharSequence?.containsAny(vararg sequences: CharSequence?): Boolean {
        if (this == null) return false
        return sequences.any { it != null && this.contains(it) }
    }

    fun String.toDashedUUID(): String {
        return buildString {
            append(this@toDashedUUID)
            insert(20, "-")
            insert(16, "-")
            insert(12, "-")
            insert(8, "-")
        }
    }

    fun String.toTitleCase(): String = this.lowercase().replaceFirstChar { c -> c.titlecase() }


    /** asterium original code */
    fun String.withoutColorCodes(): String = EnumChatFormatting.getTextWithoutFormattingCodes(this)

}