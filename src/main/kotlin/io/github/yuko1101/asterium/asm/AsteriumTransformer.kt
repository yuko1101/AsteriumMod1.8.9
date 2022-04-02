package io.github.yuko1101.asterium.asm

import dev.falsehonesty.asmhelper.BaseClassTransformer
import io.github.yuko1101.asterium.asm.transformers.insertReceivePacketEvent
import net.minecraft.launchwrapper.LaunchClassLoader
import java.util.*

class AsteriumTransformer : BaseClassTransformer() {
    companion object {
        /*
         * Key is srg name, value is deobf name
        */
        val methodMaps: WeakHashMap<String, String> = WeakHashMap()
        var madeTransformers = false
    }

    override fun setup(classLoader: LaunchClassLoader) {
        methodMaps + mapOf(
            "func_150254_d" to "getFormattedText",
            "func_145748_c_" to "getDisplayName",
            "func_177067_a" to "renderName"
        )
    }

    override fun makeTransformers() {
        if (!madeTransformers) {
            madeTransformers = true
            try {
//                addColoredNamesCheck()
//                injectSplashProgressTransformer()
//                changeRenderedName()
                insertReceivePacketEvent()
//                injectNullCheck()
//                commitArson()
//                injectContainerCheck()
//                injectScoreboardScoreRemover()
//                fixSBADungeonCheck()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}