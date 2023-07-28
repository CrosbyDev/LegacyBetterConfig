package dev.xpple.betterconfig.command;

import dev.xpple.betterconfig.api.Config;
import dev.xpple.betterconfig.impl.BetterConfigImpl;
import dev.xpple.betterconfig.impl.ModConfigImpl;
import dev.xpple.betterconfig.util.CheckedFunction;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandNotFoundException;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandResult;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandContext;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.GenericArguments;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.spec.CommandSpec;
import net.legacyfabric.fabric.api.permission.v1.PermissibleCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class ConfigCommandHelper<S extends PermissibleCommandSource>  {
    protected CommandSpec.Builder create() {
        CommandSpec.Builder root = CommandSpec.builder();
        for (ModConfigImpl modConfig : BetterConfigImpl.getModConfigs().values()) {
            Map<String, CommandSpec.Builder> literals = new HashMap<>();
            for (String config : modConfig.getConfigs().keySet()) {
                CommandSpec.Builder configLiteral = CommandSpec.builder();
                literals.put(config, configLiteral);

                configLiteral.child(CommandSpec.builder().executor((source, args) -> get(source, modConfig, config)).build(), "get");
                configLiteral.child(CommandSpec.builder().executor((source, args) -> reset(source, modConfig, config)).build(), "reset");
            }

            modConfig.getComments().forEach((config, comment) -> literals.get(config).child(CommandSpec.builder().executor((source, args) -> comment(source, config, comment)).build(), "comment"));
            modConfig.getSetters().keySet().forEach(config -> {
                Config annotation = modConfig.getAnnotations().get(config);
                Config.Setter setter = annotation.setter();
                Class<?> type = setter.type() == Config.EMPTY.class ? modConfig.getType(config) : setter.type();
                Function<Text, CommandElement> argumentFactory = modConfig.getArgument(type);
                if (type.isEnum()) {
                    //noinspection rawtypes, unchecked
                    CommandSpec subCommand = CommandSpec.builder().arguments(GenericArguments.enumValue(new LiteralText("value"), (Class<? extends Enum>) type)).executor((source, args) -> set(source, modConfig, config, args.requireOne("value"))).build();
                    literals.get(config).child(subCommand, "set");
                } else if (argumentFactory != null) {
                    CommandSpec subCommand = CommandSpec.builder().arguments(argumentFactory.apply(new LiteralText("value"))).executor((source, args) -> set(source, modConfig, config, args.requireOne("value"))).build();
                    literals.get(config).child(subCommand, "set");
                }
            });
            modConfig.getAdders().keySet().forEach(config -> {
                Config annotation = modConfig.getAnnotations().get(config);
                Config.Adder adder = annotation.adder();
                Class<?> type = adder.type() == Config.EMPTY.class ? (Class<?>) modConfig.getParameterTypes(config)[0] : adder.type();
                Function<Text, CommandElement> argumentFactory = modConfig.getArgument(type);
                if (type.isEnum()) {
                    //noinspection rawtypes, unchecked
                    CommandSpec subCommand = CommandSpec.builder().arguments(GenericArguments.enumValue(new LiteralText("value"), (Class<? extends Enum>) type)).executor((source, args) -> add(source, modConfig, config, args.requireOne("value"))).build();
                    literals.get(config).child(subCommand, "add");
                } else if (argumentFactory != null) {
                    CommandSpec subCommand = CommandSpec.builder().arguments(argumentFactory.apply(new LiteralText("value"))).executor((source, args) -> add(source, modConfig, config, args.requireOne("value"))).build();
                    literals.get(config).child(subCommand, "add");
                }
            });
            modConfig.getPutters().keySet().forEach(config -> {
                Config annotation = modConfig.getAnnotations().get(config);
                Config.Putter putter = annotation.putter();
                Type[] types = modConfig.getParameterTypes(config);
                Class<?> keyType = putter.keyType() == Config.EMPTY.class ? (Class<?>) types[0] : putter.keyType();
                CommandElement keyArgument;
                CheckedFunction<CommandContext, ?, CommandNotFoundException> getKey;
                Function<Text, CommandElement> keyArgumentFactory = modConfig.getArgument(keyType);
                if (keyType.isEnum()) {
                    //noinspection rawtypes, unchecked
                    keyArgument = GenericArguments.enumValue(new LiteralText("key"), (Class<? extends Enum>) keyType);
                    getKey = args -> args.requireOne("key");
                } else if (keyArgumentFactory != null) {
                    keyArgument = keyArgumentFactory.apply(new LiteralText("key"));
                    getKey = args -> args.requireOne("key");
                } else {
                    return;
                }
                Class<?> valueType = putter.valueType() == Config.EMPTY.class ? (Class<?>) types[1] : putter.valueType();
                Function<Text, CommandElement> valueArgumentFactory = modConfig.getArgument(valueType);
                if (valueType.isEnum()) {
                    //noinspection rawtypes, unchecked
                    CommandSpec subCommand = CommandSpec.builder().arguments(keyArgument, GenericArguments.enumValue(new LiteralText("value"), (Class<? extends Enum>) valueType)).executor((source, args) -> put(source, modConfig, config, getKey.apply(args), args.requireOne("value"))).build();
                    literals.get(config).child(subCommand, "put");
                } else if (valueArgumentFactory != null) {
                    CommandSpec subCommand = CommandSpec.builder().arguments(keyArgument, valueArgumentFactory.apply(new LiteralText("value"))).executor((source, args) -> put(source, modConfig, config, getKey.apply(args), args.requireOne("value"))).build();
                    literals.get(config).child(subCommand, "put");
                }
            });
            modConfig.getRemovers().keySet().forEach(config -> {
                Config annotation = modConfig.getAnnotations().get(config);
                Config.Remover remover = annotation.remover();
                Class<?> type = remover.type() == Config.EMPTY.class ? (Class<?>) modConfig.getParameterTypes(config)[0] : remover.type();
                Function<Text, CommandElement> argumentFactory = modConfig.getArgument(type);
                if (type.isEnum()) {
                    //noinspection rawtypes, unchecked
                    CommandSpec subCommand = CommandSpec.builder().arguments(GenericArguments.enumValue(new LiteralText("value"), (Class<? extends Enum>) type)).executor((source, args) -> remove(source, modConfig, config, args.requireOne("value"))).build();
                    literals.get(config).child(subCommand, "remove");
                } else if (argumentFactory != null) {
                    CommandSpec subCommand = CommandSpec.builder().arguments(argumentFactory.apply(new LiteralText("value"))).executor((source, args) -> remove(source, modConfig, config, args.requireOne("value"))).build();
                    literals.get(config).child(subCommand, "remove");
                }
            });
            literals.values().forEach(literal -> root.child(literal.build(), modConfig.getModId()));
        }
        return root;
    }

    protected abstract CommandResult comment(PermissibleCommandSource source, String config, String comment);

    protected abstract CommandResult get(PermissibleCommandSource source, ModConfigImpl modConfig, String config);

    protected abstract CommandResult reset(PermissibleCommandSource source, ModConfigImpl modConfig, String config);

    protected abstract CommandResult set(PermissibleCommandSource source, ModConfigImpl modConfig, String config, Object value) throws CommandNotFoundException;

    protected abstract CommandResult add(PermissibleCommandSource source, ModConfigImpl modConfig, String config, Object value) throws CommandNotFoundException;

    protected abstract CommandResult put(PermissibleCommandSource source, ModConfigImpl modConfig, String config, Object key, Object value) throws CommandNotFoundException;

    protected abstract CommandResult remove(PermissibleCommandSource source, ModConfigImpl modConfig, String config, Object value) throws CommandNotFoundException;
}
