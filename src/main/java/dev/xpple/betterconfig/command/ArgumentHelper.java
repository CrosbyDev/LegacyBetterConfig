package dev.xpple.betterconfig.command;

import net.legacyfabric.fabric.api.command.v2.lib.sponge.args.CommandElement;
import net.legacyfabric.fabric.impl.command.lib.sponge.args.NumericElement;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class ArgumentHelper {
    // Arg creators
    public static CommandElement floatNum(Text key) {
        return new NumericElement<>(key, Float::parseFloat, null, input -> new LiteralText(String.format("Expected a number, but input '%s was not", input)));
    }
}
