package io.github.yuko1101.asterium.mixins

import net.minecraft.launchwrapper.Launch
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class AsteriumMixinPlugin : IMixinConfigPlugin {

    val mixinPackage = "io.github.yuko1101.asterium.mixins.transformers"
    var deobfEnvironment = false

    override fun onLoad(mixinPackage: String) {
        deobfEnvironment = Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false) as Boolean
        if (deobfEnvironment) {
            println("We are in a deobfuscated environment, loading compatibility mixins.")
        }
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        if (!mixinClassName.startsWith(mixinPackage)) {
            println("Woah, how did mixin $mixinClassName for $targetClassName get here?")
            return false
        }
        if (mixinClassName.startsWith("$mixinPackage.deobfenv") && !deobfEnvironment) {
            println("Mixin $mixinClassName is for a deobfuscated environment, disabling.")
            return false
        }
        return true
    }

    override fun acceptTargets(myTargets: MutableSet<String>, otherTargets: MutableSet<String>) {

    }

    override fun getMixins(): MutableList<String>? {
        return null
    }

    override fun preApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {

    }

    override fun postApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo?
    ) {

    }

}