package me.codexadrian.spirit.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.*;
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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record SoulEngulfingRecipe(ResourceLocation id, SoulEngulfingInput input, int duration, boolean breaksBlocks, Item output, int outputAmount) implements SyncedData {

    public static Codec<SoulEngulfingRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                SoulEngulfingInput.CODEC.fieldOf("input").forGetter(SoulEngulfingRecipe::input),
                Codec.INT.fieldOf("duration").orElse(0).forGetter(SoulEngulfingRecipe::duration),
                Codec.BOOL.fieldOf("destroysStructure").orElse(true).forGetter(SoulEngulfingRecipe::breaksBlocks),
                Registry.ITEM.byNameCodec().fieldOf("outputItem").forGetter(SoulEngulfingRecipe::output),
                Codec.INT.fieldOf("outputAmount").orElse(1).forGetter(SoulEngulfingRecipe::outputAmount)
        ).apply(instance, SoulEngulfingRecipe::new));
    }
    @Override
    public ItemStack getResultItem() {
        return new ItemStack(this.output, this.outputAmount);
    }

    @Override
    public ResourceLocation getId() {
        return id();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpiritRegistry.SOUL_ENGULFING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SpiritRegistry.SOUL_ENGULFING_RECIPE.get();
    }

    public boolean validateRecipe(BlockPos blockPos, ItemEntity itemE, ServerLevel level) {
        Optional<SoulfireMultiblock> multiblock = input().multiblock();
        if (itemE instanceof EngulfableItem engulfableItem) {
            if(!engulfableItem.isEngulfed() && this.duration() > 0) engulfableItem.setMaxEngulfTime(this.duration());
            else if(engulfableItem.isEngulfed() || this.duration() == 0) {
                if(multiblock.isPresent() && !multiblock.get().validateMultiblock(blockPos, level, false)) engulfableItem.resetEngulfing();
                if(engulfableItem.isFullyEngulfed() && multiblock.isPresent() && multiblock.get().validateMultiblock(blockPos, level, breaksBlocks())){
                    itemE.setInvulnerable(true);
                    ItemEntity output = new ItemEntity(itemE.level, itemE.getX(), itemE.getY(), itemE.getZ(), this.getResultItem());
                    output.setInvulnerable(true);
                    itemE.level.addFreshEntity(output);
                    itemE.getItem().shrink(1);
                    engulfableItem.resetEngulfing();
                    level.sendParticles(ParticleTypes.SOUL, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 40, 1, 2, 1, 0);
                }
            }
            return true;
        }
        return false;
    }
    public static List<SoulEngulfingRecipe> getRecipesForStack(ItemStack stack, RecipeManager manager) {
        return manager.getAllRecipesFor(SpiritRegistry.SOUL_ENGULFING_RECIPE.get()).stream().filter(recipe -> recipe.input.item().test(stack)).toList();
    }

    public record SoulEngulfingInput(Ingredient item, Optional<SoulfireMultiblock> multiblock) {
        public static final Codec<Ingredient> INGREDIENT_CODEC = Codec.PASSTHROUGH.comapFlatMap(SoulEngulfingInput::decodeIngredient, SoulEngulfingInput::encodeIngredient);
        public static final Codec<SoulEngulfingInput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                INGREDIENT_CODEC.fieldOf("ingredient").forGetter(SoulEngulfingInput::item),
                SoulfireMultiblock.CODEC.optionalFieldOf("multiblock").forGetter(SoulEngulfingInput::multiblock)
        ).apply(instance, SoulEngulfingInput::new));


        private static DataResult<Ingredient> decodeIngredient(Dynamic<?> dynamic) {
            return DataResult.success(Ingredient.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue()));
        }

        private static Dynamic<JsonElement> encodeIngredient(Ingredient ingredient) {
            return new Dynamic<>(JsonOps.INSTANCE, ingredient.toJson()).convert(JsonOps.COMPRESSED);
        }
    }
}