package io.github.yuko1101.asterium.mixin.transformers.network;

import io.github.yuko1101.asterium.mixin.hooks.network.NetworkManagerHookKt;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkManager.class, priority = 1001)
public abstract class MixinNetworkManager extends SimpleChannelInboundHandler<Packet<?>> {
    @Inject(method = "channelRead0*", at = @At("HEAD"), cancellable = true)
    private void onReceivePacket(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci) {
        NetworkManagerHookKt.onReceivePacket(context, packet, ci);
    }
}