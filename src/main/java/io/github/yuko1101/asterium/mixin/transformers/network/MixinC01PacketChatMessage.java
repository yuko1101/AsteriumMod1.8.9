package io.github.yuko1101.asterium.mixin.transformers.network;

import io.github.yuko1101.asterium.mixin.extensions.ExtensionC01PacketChatMessage;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(C01PacketChatMessage.class)
public abstract class MixinC01PacketChatMessage implements ExtensionC01PacketChatMessage {

    @Shadow
    private String message;

    // TODO: avoid conflict with other mods
    @Override
    public void setMessage(String string) {
        if (string.length() > 100) {
            string = string.substring(0, 100);
        }

        this.message = string;
    }

}