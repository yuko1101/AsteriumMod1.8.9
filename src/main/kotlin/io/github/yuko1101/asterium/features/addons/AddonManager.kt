package io.github.yuko1101.asterium.features.addons

import io.github.yuko1101.asterium.Asterium
import io.github.yuko1101.asterium.config.AsteriumConfig
import io.github.yuko1101.asterium.utils.ObjectManager

class AddonManager : ObjectManager<AsteriumAddon>() {

    /**
     * @return Returns error message map
     */
    fun loadFromAddonDir(updateConfig: Boolean): Map<AsteriumAddon, String> {
        val errorMessageMap = mutableMapOf<AsteriumAddon, String>()
        val addonFiles = Asterium.fileManager.addonsDir.listFiles { file -> file.extension == "jar" } ?: return errorMessageMap
        addonFiles.forEach { file ->
            if (registered.any { it.addonFile?.classLoader?.jarPath == file.absolutePath }) {
                Asterium.logger.info("[Asterium Addons] Skipped loading $file because it has already loaded")
            } else {
                Asterium.logger.info("[Asterium Addons] Loading $file")
                val addonClassLoader = AddonClassLoader(file.absolutePath)
                val errorMap = loadAll(addonClassLoader.loadClassesInJar().let { it?.addons ?: emptyList() }, updateConfig = false)
                errorMessageMap.putAll(errorMap)
            }
        }
        if (updateConfig) updateAddonsConfig()
        return errorMessageMap
    }

    /**
     * @return An error message that the addon could not be activated. Return null if it succeeds.
     */
    fun load(addon: AsteriumAddon, updateConfig: Boolean): String? {
        val errorMessage = addon.onEnable()
        if (errorMessage == null) {
            register(addon)
            if (updateConfig) updateAddonsConfig()
        }
        return errorMessage
    }

    /**
     * @return Returns error message map
     */
    fun loadAll(addons: List<AsteriumAddon>, updateConfig: Boolean): Map<AsteriumAddon, String> {
        val errorMessageMap = mutableMapOf<AsteriumAddon, String>()
        for (addon in addons) {
            val errorMessage = load(addon, updateConfig = false)
            if (errorMessage != null) {
                errorMessageMap[addon] = errorMessage
            }
        }
        if (updateConfig) updateAddonsConfig()
        return errorMessageMap.toMap()
    }

    /**
     * @return An error message that the addon could not be deactivated. Return null if it succeeds.
     */
    fun unload(addon: AsteriumAddon): String? {
        val errorMessage = addon.onDisable()
        if (errorMessage == null) {
            unregister(addon)
            if (addon.addonFile != null) {
                addon.addonFile!!.activeAddons.remove(addon)
                if (addon.addonFile!!.activeAddons.isEmpty()) addon.addonFile!!.unload()
            }
        }
        return errorMessage
    }

    /**
     * @return Returns error message map.
     */
    fun unloadAll(): Map<AsteriumAddon, String> {
        Asterium.config = AsteriumConfig()
        val errorMessageMap = mutableMapOf<AsteriumAddon, String>()
        for (addon in registered) {
            val errorMessage = unload(addon)
            if (errorMessage != null) {
                errorMessageMap[addon] = errorMessage
            }
        }
        return errorMessageMap.toMap()
    }

    /**
     * @returns Pair of error message map. First is error message map of unload, second is error message map of load.
     */
    fun reloadAll(): Pair<Map<AsteriumAddon, String>, Map<AsteriumAddon, String>> {
        val unloadErrorMessageMap = unloadAll()
        val loadErrorMessageMap = loadFromAddonDir(updateConfig = true)

        return Pair(unloadErrorMessageMap, loadErrorMessageMap)
    }

    fun updateAddonsConfig() {
        Asterium.config.updateAddons()
    }
}