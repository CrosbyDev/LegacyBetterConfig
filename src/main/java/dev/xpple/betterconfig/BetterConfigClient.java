package dev.xpple.betterconfig;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import dev.xpple.betterconfig.command.client.ConfigCommandClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.legacyfabric.fabric.api.command.v2.CommandRegistrar;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandManager;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandMapping;
import net.legacyfabric.fabric.impl.command.CommandWrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandRegistry;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class BetterConfigClient {
    private static final CommandRegistry commandManager = new CommandRegistry();
    private static String autocompleteQuery = "";

    public static void setAutocompleteQuery(String partialMessage) {
        autocompleteQuery = partialMessage;
    }

    public static List<String> getCommandSuggestions() {
        if (autocompleteQuery.startsWith("/")) return commandManager.getCompletions(MinecraftClient.getInstance().player, autocompleteQuery.substring(1), null);
        return ImmutableList.of();
    }

    public static void clientInitializationCallback() {
        CommandRegistrar.EVENT.register(BetterConfigClient::registerCommands);
    }

    public static void executeCommand(String string) {
        commandManager.execute(MinecraftClient.getInstance().player, string);
    }

    private static void registerCommands(CommandManager manager, boolean dedicated) {
        Optional<CommandMapping> mapping = new ConfigCommandClient().register(manager);
        commandManager.registerCommand(new CommandWrapper(mapping.orElseThrow(() -> new RuntimeException("Could not register /cconfig commands."))));
    }
}
