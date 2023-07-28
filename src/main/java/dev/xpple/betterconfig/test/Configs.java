package dev.xpple.betterconfig.test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import dev.xpple.betterconfig.api.Config;
import net.legacyfabric.fabric.api.command.v2.lib.sponge.CommandNotFoundException;

import java.util.*;

@SuppressWarnings({"FieldMayBeFinal", "unused", "MismatchedQueryAndUpdateOfCollection"})
public class Configs {
    @Config(adder = @Config.Adder("customAdder"))
    public static Collection<String> exampleCustomAdder = Lists.newArrayList("1", "2");
    public static void customAdder(String string) throws CommandNotFoundException {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            throw new CommandNotFoundException(string);
        }
        exampleCustomAdder.add(string);
    }

    @Config(putter = @Config.Putter("none"), adder = @Config.Adder("customMapAdder"))
    public static Map<String, String> exampleMapAdder = new HashMap<>(ImmutableMap.of("a", "A", "b", "B"));
    public static void customMapAdder(String string) {
        exampleMapAdder.put(string.toLowerCase(Locale.ROOT), string.toUpperCase(Locale.ROOT));
    }

    @Config(adder = @Config.Adder(value = "customTypeAdder", type = int.class))
    public static Collection<String> exampleCustomType = Lists.newArrayList("%", "@");
    public static void customTypeAdder(int codepoint) {
        exampleCustomType.add(String.valueOf((char) codepoint));
    }

    @Config
    public static TestEnum exampleEnum = TestEnum.ONE;

    @Config(readOnly = true)
    public static double exampleReadOnly = Math.PI;

    @Config(temporary = true)
    public static double exampleTemporary = Math.random();

    @Config
    private static boolean examplePrivate = false;

    @Config(setter = @Config.Setter("privateSetter"))
    public static String examplePrivateSetter = "nice";
    private static void privateSetter(String string) {
        examplePrivateSetter = string + '!';
    }

    @Config(comment = "This is a mysterious object")
    public static Object exampleComment = null;
}
