package me.codexadrian.spirit.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record SoulEngulfingRecipe(SoulEngulfingInput input, int duration, boolean breaksBlocks, Item output, int outputAmount) {
    public static final Codec<SoulEngulfingRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoulEngulfingInput.CODEC.fieldOf("input").forGetter(SoulEngulfingRecipe::input),
            Codec.INT.fieldOf("duration").orElse(0).forGetter(SoulEngulfingRecipe::duration),
            Codec.BOOL.fieldOf("destroysStructure").orElse(true).forGetter(SoulEngulfingRecipe::breaksBlocks),
            Registry.ITEM.byNameCodec().fieldOf("outputItem").forGetter(SoulEngulfingRecipe::output),
            Codec.INT.fieldOf("outputAmount").orElse(1).forGetter(SoulEngulfingRecipe::outputAmount)
    ).apply(instance, SoulEngulfingRecipe::new));

    public ItemStack getOutputStack() {
        return new ItemStack(this.output, this.outputAmount);
    }
}
