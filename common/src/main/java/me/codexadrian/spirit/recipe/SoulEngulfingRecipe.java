package me.codexadrian.spirit.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.codexadrian.spirit.EngulfableItem;
import me.codexadrian.spirit.SpiritRegistry;
import me.codexadrian.spirit.utils.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SoulEngulfingRecipe(ResourceLocation id, SoulEngulfingInput input, int duration, boolean breaksBlocks, Item output, int outputAmount) implements Recipe<Container> {

    public static Codec<SoulEngulfingRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                MapCodec.of(Encoder.empty(), Decoder.unit(() -> id)).forGetter(SoulEngulfingRecipe::id),
                SoulEngulfingInput.CODEC.fieldOf("input").forGetter(SoulEngulfingRecipe::input),
                Codec.INT.fieldOf("duration").orElse(0).forGetter(SoulEngulfingRecipe::duration),
                Codec.BOOL.fieldOf("destroysStructure").orElse(true).forGetter(SoulEngulfingRecipe::breaksBlocks),
                Registry.ITEM.byNameCodec().fieldOf("outputItem").forGetter(SoulEngulfingRecipe::output),
                Codec.INT.fieldOf("outputAmount").orElse(1).forGetter(SoulEngulfingRecipe::outputAmount)
        ).apply(instance, SoulEngulfingRecipe::new));
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(@NotNull Container container) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int i, int j) {
        return false;
    }

    @Override
    public ItemStack getResultItem() {
        return new ItemStack(this.output, this.outputAmount);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpiritRegistry.SOUL_ENGULFING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SpiritRegistry.SOUL_ENGULFING_RECIPE.get();
    }

    public boolean validateRecipe(BlockPos blockPos, ItemEntity itemE, Level level) {
        if (RecipeUtils.checkMultiblock(blockPos, level, this.input().catalyst(), false) && itemE instanceof EngulfableItem engulfableItem) {
            if(!engulfableItem.isEngulfed() && this.duration() > 0) engulfableItem.setMaxEngulfTime(this.duration());
            else if(engulfableItem.isEngulfed() || this.duration() == 0) {
                if(!RecipeUtils.checkMultiblock(blockPos, level, this.input().catalyst(), false)) engulfableItem.resetEngulfing();
                if(engulfableItem.isFullyEngulfed() && RecipeUtils.checkMultiblock(blockPos, level, this.input().catalyst(), this.breaksBlocks())) {
                    itemE.setInvulnerable(true);
                    ItemEntity output = new ItemEntity(itemE.level, itemE.getX(), itemE.getY(), itemE.getZ(), this.getResultItem());
                    output.setInvulnerable(true);
                    itemE.level.addFreshEntity(output);
                    itemE.getItem().shrink(1);
                    engulfableItem.resetEngulfing();
                    ServerLevel sLevel = (ServerLevel) itemE.level;
                    sLevel.sendParticles(ParticleTypes.SOUL, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 40, 1, 2, 1, 0);
                }
            }
            return true;
        }
        return false;
    }
    public static List<SoulEngulfingRecipe> getRecipesForStack(ItemStack stack, RecipeManager manager) {
        return manager.getAllRecipesFor(SpiritRegistry.SOUL_ENGULFING_RECIPE.get()).stream().filter(recipe -> recipe.input.item().test(stack)).toList();
    }

}