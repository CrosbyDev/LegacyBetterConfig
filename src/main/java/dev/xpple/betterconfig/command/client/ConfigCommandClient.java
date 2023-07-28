package dev.xpple.betterconfig.command.client;

import dev.xpple.betterconfig.command.ConfigCommandHelper;
import dev.xpple.betterconfig.impl.ModConfigImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandManager;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandNotFoundException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class ConfigCommandClient extends ConfigCommandHelper<PermissibleCommandSource> {
    public void register(CommandManager manager) {
        manager.register(this.create().build(), "cconfig");
    }

    @Override
    protected CommandResult comment(PermissibleCommandSource source, String config, String comment) {
        source.sendMessage(new TranslatableText("betterconfig.commands.config.comment", config));
        source.sendMessage(new LiteralText(comment));
        return CommandResult.success();
    }

    @Override
    protected CommandResult get(PermissibleCommandSource source, ModConfigImpl modConfig, String config) {
        source.sendMessage(new TranslatableText("betterconfig.commands.config.get", config, modConfig.asString(config)));
        return CommandResult.success();
    }

    @Override
    protected CommandResult reset(PermissibleCommandSource source, ModConfigImpl modConfig, String config) {
        modConfig.reset(config);
        source.sendMessage(new TranslatableText("betterconfig.commands.config.reset", config, modConfig.asString(config)));
        return CommandResult.success();
    }

    @Override
    protected CommandResult set(PermissibleCommandSource source, ModConfigImpl modConfig, String config, Object value) throws CommandNotFoundException {
        modConfig.set(config, value);
        source.sendMessage(new TranslatableText("betterconfig.commands.config.set", config, modConfig.asString(config)));
        return CommandResult.success();
    }

    @Override
    protected CommandResult add(PermissibleCommandSource source, ModConfigImpl modConfig, String config, Object value) throws CommandNotFoundException {
        modConfig.add(config, value);
        source.sendMessage(new TranslatableText("betterconfig.commands.config.add", modConfig.asString(value), config));
        return CommandResult.success();
    }

    @Override
    protected CommandResult put(PermissibleCommandSource source, ModConfigImpl modConfig, String config, Object key, Object value) throws CommandNotFoundException {
        modConfig.put(config, key, value);
        source.sendMessage(new TranslatableText("betterconfig.commands.config.put", modConfig.asString(key), modConfig.asString(value), config));
        return CommandResult.success();
    }

    @Override
    protected CommandResult remove(PermissibleCommandSource source, ModConfigImpl modConfig, String config, Object value) throws CommandNotFoundException {
        modConfig.remove(config, value);
        source.sendMessage(new TranslatableText("betterconfig.commands.config.remove", modConfig.asString(value), config));
        return CommandResult.success();
    }
}
