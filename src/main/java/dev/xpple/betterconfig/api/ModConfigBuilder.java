package dev.xpple.betterconfig.api;

import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import dev.xpple.betterconfig.impl.BetterConfigImpl;
import dev.xpple.betterconfig.impl.BetterConfigInternals;
import dev.xpple.betterconfig.impl.ModConfigImpl;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ModConfigBuilder {

    final String modId;
    final Class<?> configsClass;

    final GsonBuilder builder = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization();
    final Map<Class<?>, Function<Text, CommandElement>> arguments = new HashMap<>();

    public ModConfigBuilder(String modId, Class<?> configsClass) {
        this.modId = modId;
        this.configsClass = configsClass;
    }

    /**
     * Register a new type adapter and argument type for the specified type.
     * @param type the type's class
     * @param adapter the type adapter
     * @param argument a brigadier argument pair
     * @param <T> the type
     * @return the current builder instance
     * @see ModConfigBuilder#registerTypeHierarchyWithArgument
     */
    public <T> ModConfigBuilder registerTypeWithArgument(Class<T> type, TypeAdapter<T> adapter, Function<Text, CommandElement> argument) {
        this.builder.registerTypeAdapter(type, adapter);
        this.arguments.put(type, argument);
        return this;
    }

    /**
     * Register a new type adapter and argument type for the specified type and all subclasses.
     * @param type the type's class
     * @param adapter the type adapter
     * @param argument a pair of a brigadier argument and parser
     * @param <T> the type
     * @return the current builder instance
     */
    public <T> ModConfigBuilder registerTypeHierarchyWithArgument(Class<T> type, TypeAdapter<T> adapter, Function<Text, CommandElement> argument) {
        this.builder.registerTypeHierarchyAdapter(type, adapter);
        this.arguments.put(type, argument);
        return this;
    }

    /**
     * Finalise the registration process.
     * @throws IllegalArgumentException when a configuration already exists for this mod
     */
    public void build() {
        ModConfigImpl modConfig = new ModConfigImpl(this.modId, this.configsClass, this.builder.create(), this.arguments);
        if (BetterConfigImpl.getModConfigs().putIfAbsent(this.modId, modConfig) == null) {
            BetterConfigInternals.init(modConfig);
            return;
        }
        throw new IllegalArgumentException(this.modId);
    }
}
