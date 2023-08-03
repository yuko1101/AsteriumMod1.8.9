package io.github.yuko1101.asterium.utils

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.features.addons.AsteriumAddon
import java.io.*

class FileManager {
    val rootDir: File by lazy {
        File(Asterium.MOD_ID).also {
            if (!it.exists()) it.mkdirs()
        }
    }
    val addonsDir: File by lazy {
        File(rootDir, "addons").also {
            if (!it.exists()) it.mkdir()
        }
    }

    fun getAddonDir(addon: AsteriumAddon, create: Boolean = true): File {
        return File(addonsDir, addon.id).also {
            if (!it.exists() && create) it.mkdir()
        }
    }

    fun getAddonHUDDir(addon: AsteriumAddon, create: Boolean = true): File {
        return File(getAddonDir(addon, create), "hud").also {
            if (!it.exists() && create) it.mkdir()
        }
    }
}