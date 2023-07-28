package dev.xpple.betterconfig;

import dev.xpple.betterconfig.command.ConfigCommand;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.legacyfabric.fabric.api.command.v2.CommandRegistrar;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class BetterConfig implements DedicatedServerModInitializer {

    public static final String MOD_ID = "betterconfig";
    public static final Path MOD_PATH = FabricLoader.getInstance().getConfigDir();

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitializeServer() {
        CommandRegistrar.EVENT.register(BetterConfig::registerCommands);
    }

    private static void registerCommands(CommandManager manager, boolean dedicated) {
        new ConfigCommand().register(manager);
    }
}
