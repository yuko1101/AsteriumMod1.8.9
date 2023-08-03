package io.github.yuko1101.asterium.features.addons

import java.util.*

class PersistentMemory(private val addon: AsteriumAddon) {
    
    companion object {
        val data = mutableMapOf<UUID, MutableMap<String, Any>>()
    }

    init {
        if (!data.containsKey(addon.addonMetaData.uuid)) data[addon.addonMetaData.uuid] = mutableMapOf()
    }

    private val addonMemory: MutableMap<String, Any>
        get() = data[addon.addonMetaData.uuid]!!

    /** @return Whether the value set successfully. */
    fun set(key: String, value: Any): Boolean {
        if (addonMemory.containsKey(key)) {
            return false
        }
        addonMemory[key] = value
        return true
    }

    /** @return Returns `true` if the key has already been set and overwrites the value. Returns `false` if the key has not been set. */
    fun overwrite(key: String, value: Any): Boolean {
        val hasKey = addonMemory.containsKey(key)
        addonMemory[key] = value
        return hasKey
    }

    fun get(key: String): Any? {
        return addonMemory[key]
    }

    fun has(key: String): Boolean {
        return addonMemory.containsKey(key)
    }


}