package dev.xpple.betterconfig;

import dev.xpple.betterconfig.api.ModConfigBuilder;
import dev.xpple.betterconfig.command.ConfigCommand;
import dev.xpple.betterconfig.test.Configs;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.legacyfabric.fabric.api.command.v2.CommandRegistrar;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

public class BetterConfig implements ModInitializer {

    public static final String MOD_ID = "betterconfig";
    public static final Path MOD_PATH = FabricLoader.getInstance().getConfigDir();

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static void registerCommands(CommandManager manager, boolean dedicated) {
        new ConfigCommand().register(manager);
    }

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            BetterConfigClient.clientInitializationCallback();

            new ModConfigBuilder("testmodclient", Configs.class)
                    .build();
        } else {
            CommandRegistrar.EVENT.register(BetterConfig::registerCommands);

            new ModConfigBuilder("testmod", Configs.class)
                    .build();
        }
    }
}
