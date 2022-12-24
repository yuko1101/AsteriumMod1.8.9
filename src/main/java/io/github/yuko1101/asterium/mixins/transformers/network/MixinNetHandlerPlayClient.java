package io.github.yuko1101.asterium.mixins.transformers.network;

import io.github.yuko1101.asterium.mixins.hooks.network.NetHandlerPlayClientHookKt;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetHandlerPlayClient.class, priority = 1001)
public abstract class MixinNetHandlerPlayClient implements INetHandlerPlayClient {
    @Inject(method = "addToSendQueue", at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        NetHandlerPlayClientHookKt.onSendPacket(packet, ci);
    }
}