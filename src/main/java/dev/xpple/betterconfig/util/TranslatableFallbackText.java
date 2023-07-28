package dev.xpple.betterconfig.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import net.minecraft.text.*;
import net.minecraft.util.CommonI18n;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;

public class TranslatableFallbackText extends BaseText {
    private final String key;
    private final String fallback;
    private final Object[] args;
    private final Object lock = new Object();
    private long languageReloadTimestamp = -1L;
    private List<Text> translations = Lists.newArrayList();

    public TranslatableFallbackText(String string, String fallback, Object... objects) {
        this.key = string;
        this.fallback = fallback;
        this.args = objects;
    }

    synchronized void updateTranslations() {
        synchronized(this.lock) {
            long l = CommonI18n.getTimeLoaded();
            if (l == this.languageReloadTimestamp) {
                return;
            }

            this.languageReloadTimestamp = l;
            this.translations.clear();
        }

        try {
            String translated = CommonI18n.translate(this.key);
            //noinspection StringEquality
            if (translated == this.key) setFallback();
            else this.setTranslation(CommonI18n.translate(this.key));
        } catch (TranslationException var6) {
            setFallback();
        }
    }

    private void setFallback() {
        this.translations = Lists.newArrayList(new LiteralText(fallback));
    }

    protected void setTranslation(String translation) {
        Matcher matcher = TranslatableText.ARG_FORMAT.matcher(translation);
        int i = 0;
        int j = 0;

        try {
            int l;
            for(; matcher.find(j); j = l) {
                int k = matcher.start();
                l = matcher.end();
                if (k > j) {
                    LiteralText literalText = new LiteralText(String.format(translation.substring(j, k)));
                    literalText.getStyle().setParent(this.getStyle());
                    this.translations.add(literalText);
                }

                String string = matcher.group(2);
                String string2 = translation.substring(k, l);
                if ("%".equals(string) && "%%".equals(string2)) {
                    LiteralText literalText2 = new LiteralText("%");
                    literalText2.getStyle().setParent(this.getStyle());
                    this.translations.add(literalText2);
                } else {
                    if (!"s".equals(string)) {
                        throw new TranslationWithFallbackException(this, "Unsupported format: '" + string2 + "'");
                    }

                    String string3 = matcher.group(1);
                    int m = string3 != null ? Integer.parseInt(string3) - 1 : i++;
                    if (m < this.args.length) {
                        this.translations.add(this.getArg(m));
                    }
                }
            }

            if (j < translation.length()) {
                LiteralText literalText3 = new LiteralText(String.format(translation.substring(j)));
                literalText3.getStyle().setParent(this.getStyle());
                this.translations.add(literalText3);
            }

        } catch (IllegalFormatException var12) {
            throw new TranslationWithFallbackException(this, var12);
        }
    }

    private Text getArg(int index) {
        if (index >= this.args.length) {
            throw new TranslationWithFallbackException(this, index);
        } else {
            Object object = this.args[index];
            Text text;
            if (object instanceof Text) {
                text = (Text) object;
            } else {
                text = new LiteralText(object == null ? "null" : object.toString());
                text.getStyle().setParent(this.getStyle());
            }

            return text;
        }
    }

    public Text setStyle(Style style) {
        super.setStyle(style);

        for (Object arg : this.args) {
            if (arg instanceof Text) {
                ((Text)arg).getStyle().setParent(this.getStyle());
            }
        }

        if (this.languageReloadTimestamp > -1L) {
            for (Text text : this.translations) {
                text.getStyle().setParent(style);
            }
        }

        return this;
    }

    public Iterator<Text> iterator() {
        this.updateTranslations();
        return Iterators.concat(method_7458(this.translations), method_7458(this.siblings));
    }

    public String computeValue() {
        this.updateTranslations();
        StringBuilder stringBuilder = new StringBuilder();

        for (Text text : this.translations) {
            stringBuilder.append(text.computeValue());
        }

        return stringBuilder.toString();
    }

    public TranslatableFallbackText copy() {
        Object[] objects = new Object[this.args.length];

        for(int i = 0; i < this.args.length; ++i) {
            if (this.args[i] instanceof Text) {
                objects[i] = ((Text)this.args[i]).copy();
            } else {
                objects[i] = this.args[i];
            }
        }

        TranslatableFallbackText translatableText = new TranslatableFallbackText(this.key, this.fallback, objects);
        translatableText.setStyle(this.getStyle().deepCopy());

        for (Text text : this.getSiblings()) {
            translatableText.append(text.copy());
        }

        return translatableText;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof TranslatableFallbackText)) {
            return false;
        } else {
            TranslatableFallbackText translatableText = (TranslatableFallbackText)object;
            return Arrays.equals(this.args, translatableText.args) && this.key.equals(translatableText.key) && this.fallback.equals(translatableText.fallback) && super.equals(object);
        }
    }

    public int hashCode() {
        int i = super.hashCode();
        i = 31 * i + this.key.hashCode();
        i = 31 * i + Arrays.hashCode(this.args);
        return i;
    }

    public String toString() {
        return "TranslatableWithFallbackComponent{key='" + this.key + "', fallback='" + this.fallback + "', args=" + Arrays.toString(this.args) + ", siblings=" + this.siblings + ", style=" + this.getStyle() + '}';
    }

    public String getKey() {
        return this.key;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public static class TranslationWithFallbackException extends IllegalArgumentException {
        public TranslationWithFallbackException(TranslatableFallbackText translatableText, String string) {
            super(String.format("Error parsing: %s: %s", translatableText, string));
        }

        public TranslationWithFallbackException(TranslatableFallbackText translatableText, int i) {
            super(String.format("Invalid index %d requested for %s", i, translatableText));
        }

        public TranslationWithFallbackException(TranslatableFallbackText translatableText, Throwable throwable) {
            super(String.format("Error while parsing: %s", translatableText), throwable);
        }
    }
}
