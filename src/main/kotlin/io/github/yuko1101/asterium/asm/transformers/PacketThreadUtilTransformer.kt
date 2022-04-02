package io.github.yuko1101.asterium.asm.transformers

import dev.falsehonesty.asmhelper.dsl.instructions.InsnListBuilder
import dev.falsehonesty.asmhelper.dsl.instructions.JumpCondition
import dev.falsehonesty.asmhelper.dsl.modify
import io.github.yuko1101.asterium.events.impl.MainReceivePacketEvent
import net.minecraft.network.INetHandler
import net.minecraft.network.Packet
import org.objectweb.asm.Opcodes

fun insertReceivePacketEvent() = modify("net/minecraft/network/PacketThreadUtil$1") {
    classNode.visitField(
        Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL,
        "asterium\$handler",
        "Lnet/minecraft/network/INetHandler;",
        null,
        null
    ).visitEnd()
    classNode.visitField(
        Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL,
        "asterium\$packet",
        "Lnet/minecraft/network/Packet;",
        null,
        null
    ).visitEnd()
    classNode.methods.find { it.name == "<init>" }?.apply {
        instructions.insert(InsnListBuilder(this).apply {
            aload(0)
            aload(1)
            putField(classNode.name, "asterium\$packet", "Lnet/minecraft/network/Packet;")
            aload(0)
            aload(2)
            putField(classNode.name, "asterium\$handler", "Lnet/minecraft/network/INetHandler;")
        }.build())
    }
    findMethod("run", "()V").apply {
        instructions.insert(InsnListBuilder(this).apply {
            invokeStatic(
                "io/github/yuko1101/asterium/asm/transformers/PacketThreadUtilTransformer",
                "postEvent",
                "(Lnet/minecraft/network/INetHandler;Lnet/minecraft/network/Packet;)Z"
            ) {
                aload(0)
                getField(
                    "net/minecraft/network/PacketThreadUtil$1",
                    "asterium\$handler",
                    "Lnet/minecraft/network/INetHandler;"
                )
                aload(0)
                getField(
                    "net/minecraft/network/PacketThreadUtil$1",
                    "asterium\$packet",
                    "Lnet/minecraft/network/Packet;"
                )
            }
            ifClause(JumpCondition.EQUAL) {
                methodReturn()
            }
        }.build())
    }
}

@Suppress("unused")
object PacketThreadUtilTransformer {
    @JvmStatic
    fun postEvent(netHandler: INetHandler, packet: Packet<INetHandler>): Boolean {
        return MainReceivePacketEvent(
            netHandler,
            packet
        ).postAndCatch()
    }
}