package dev.xpple.betterconfig;

import com.google.common.collect.ImmutableList;
import dev.xpple.betterconfig.api.ModConfigBuilder;
import dev.xpple.betterconfig.command.client.ConfigCommandClient;
import dev.xpple.betterconfig.test.Configs;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.legacyfabric.fabric.impl.command.CommandWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandRegistry;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BetterConfigClient implements ClientModInitializer {
    private static final CommandRegistry commandManager = new CommandRegistry();
    private static String autocompleteQuery = "";

    @Override
    public void onInitializeClient() {
        new ModConfigBuilder("testmodclient", Configs.class)
                .build();

        commandManager.registerCommand(new CommandWrapper(new ConfigCommandClient().register()));
    }

    public static void setAutocompleteQuery(String partialMessage) {
        autocompleteQuery = partialMessage;
    }

    public static List<String> getCommandSuggestions() {
        if (autocompleteQuery.startsWith("/")) {
            List<String> list = commandManager.getCompletions(MinecraftClient.getInstance().player, autocompleteQuery.substring(1), null);
            if (list == null) return ImmutableList.of();
            if (!autocompleteQuery.contains(" ")) list.replaceAll(s -> "/" + s);
            return list;
        }
        return ImmutableList.of();
    }

    public static void executeCommand(String string) {
        commandManager.execute(MinecraftClient.getInstance().player, string);
    }
}
