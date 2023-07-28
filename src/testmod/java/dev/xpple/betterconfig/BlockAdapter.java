package dev.xpple.betterconfig;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

import java.io.IOException;

class BlockAdapter extends TypeAdapter<Block> {
    @Override
    public void write(JsonWriter writer, Block block) throws IOException {
        writer.value(Block.REGISTRY.getIdentifier(block).toString());
    }

    @Override
    public Block read(JsonReader reader) throws IOException {
        return Block.REGISTRY.get(new Identifier(reader.nextString()));
    }
}
