package dev.xpple.betterconfig.mixin;

import dev.xpple.betterconfig.BetterConfigClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    @Inject(method = "requestAutocomplete", at = @At("HEAD"))
    private void stealPartialMessage(String partialMessage, String nextWord, CallbackInfo ci) {
        BetterConfigClient.setAutocompleteQuery(partialMessage);
    }
}
