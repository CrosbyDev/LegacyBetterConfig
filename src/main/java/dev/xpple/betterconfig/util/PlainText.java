package dev.xpple.betterconfig.util;

import net.minecraft.text.LiteralText;

public class PlainText extends LiteralText {
    public PlainText(String string) {
        super(string);
    }

    @Override
    public String toString() {
        return computeValue();
    }
}
