package dev.xpple.betterconfig;

import dev.xpple.betterconfig.command.client.ConfigCommandClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.legacyfabric.fabric.api.command.v2.CommandRegistrar;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandManager;

@Environment(EnvType.CLIENT)
public class BetterConfigClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CommandRegistrar.EVENT.register(BetterConfigClient::registerCommands);
    }

    private static void registerCommands(CommandManager manager, boolean dedicated) {
        new ConfigCommandClient().register(manager);
    }
}
