package earth.terrarium.spirit.common.recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamresourceful.resourcefullib.common.codecs.recipes.IngredientCodec;
import com.teamresourceful.resourcefullib.common.codecs.recipes.ItemStackCodec;
import com.teamresourceful.resourcefullib.common.recipe.CodecRecipe;
import earth.terrarium.spirit.api.utils.EngulfableItem;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SoulEngulfingRecipe(ResourceLocation id, SoulEngulfingInput input, int duration, boolean breaksBlocks,
                                  ItemStack output) implements CodecRecipe<Container> {

    public static Codec<SoulEngulfingRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                SoulEngulfingInput.CODEC.fieldOf("input").forGetter(SoulEngulfingRecipe::input),
                Codec.INT.fieldOf("duration").orElse(0).forGetter(SoulEngulfingRecipe::duration),
                Codec.BOOL.fieldOf("destroysStructure").orElse(true).forGetter(SoulEngulfingRecipe::breaksBlocks),
                ItemStackCodec.CODEC.fieldOf("outputItem").forGetter(SoulEngulfingRecipe::output)
        ).apply(instance, SoulEngulfingRecipe::new));
    }

    @Override
    public boolean matches(Container container, Level level) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpiritRecipes.SOUL_ENGULFING_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SpiritRecipes.SOUL_ENGULFING.get();
    }

    public boolean validateRecipe(BlockPos blockPos, ItemEntity itemE, ServerLevel level) {
        SoulfireMultiblock multiblock = input().multiblock();
        if (itemE instanceof EngulfableItem engulfableItem) {
            if (!engulfableItem.isEngulfed() && this.duration() > 0) engulfableItem.setMaxEngulfTime(this.duration());
            else if (engulfableItem.isEngulfed() || this.duration() == 0) {
                if (!multiblock.validateMultiblock(blockPos, level, false)) {
                    engulfableItem.resetEngulfing();
                    if (!engulfableItem.isRecipeOutput()) itemE.setInvulnerable(false);
                    return false;
                }
                if (engulfableItem.isFullyEngulfed() && multiblock.validateMultiblock(blockPos, level, breaksBlocks())) {
                    itemE.setInvulnerable(true);
                    ItemEntity output = new ItemEntity(itemE.level, itemE.getX(), itemE.getY(), itemE.getZ(), this.output().copy());
                    output.setInvulnerable(true);
                    itemE.level.addFreshEntity(output);
                    if (output instanceof EngulfableItem outputEngulf) outputEngulf.setRecipeOutput();
                    itemE.getItem().shrink(1);
                    engulfableItem.resetEngulfing();
                    level.sendParticles(ParticleTypes.SOUL, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 40, 0.4, 0.2, 0.4, 0.01);
                }
            }
            return true;
        }
        return false;
    }

    public static List<SoulEngulfingRecipe> getRecipesForStack(ItemStack stack, RecipeManager manager) {
        return manager.getAllRecipesFor(SpiritRecipes.SOUL_ENGULFING.get()).stream().filter(recipe -> recipe.input.item().test(stack)).toList();
    }

    public record SoulEngulfingInput(Ingredient item, SoulfireMultiblock multiblock) {
        public static final Codec<SoulEngulfingInput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                IngredientCodec.CODEC.fieldOf("ingredient").forGetter(SoulEngulfingInput::item),
                SoulfireMultiblock.CODEC.fieldOf("multiblock").orElse(SoulfireMultiblock.DEFAULT_RECIPE).forGetter(SoulEngulfingInput::multiblock)
        ).apply(instance, SoulEngulfingInput::new));
    }
}