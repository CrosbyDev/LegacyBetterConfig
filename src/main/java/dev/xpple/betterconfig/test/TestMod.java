package dev.xpple.betterconfig.test;

import dev.xpple.betterconfig.api.ModConfigBuilder;
import net.fabricmc.api.DedicatedServerModInitializer;

public class TestMod implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        new ModConfigBuilder("testmod", Configs.class)
            .build();
    }
}
