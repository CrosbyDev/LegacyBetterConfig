package dev.xpple.betterconfig.mixin;

import dev.xpple.betterconfig.BetterConfigClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.*;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @ModifyArg(method = "onCommandSuggestions", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ChatScreen;setSuggestions([Ljava/lang/String;)V"))
    private String[] appendClientsideSuggestions(String[] serverSuggestions) {
        Set<String> suggestions = new HashSet<>(Arrays.asList(serverSuggestions));
        suggestions.addAll(BetterConfigClient.getCommandSuggestions());
        String[] unsortedSuggestions = suggestions.toArray(new String[0]);
        Arrays.sort(unsortedSuggestions);
        return unsortedSuggestions;
    }
}
