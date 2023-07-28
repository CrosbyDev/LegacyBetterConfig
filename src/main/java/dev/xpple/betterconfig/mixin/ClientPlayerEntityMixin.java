package dev.xpple.betterconfig.mixin;

import dev.xpple.betterconfig.BetterConfigClient;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.minecraft.entity.player.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements PermissibleCommandSource {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void interceptChatMessage(String string, CallbackInfo ci) {
        if (string.startsWith("/cconfig")) {
            BetterConfigClient.executeCommand(string);
            ci.cancel();
        }
    }

    @Override
    public boolean hasPermission(String perm) {
        return true;
    }
}
