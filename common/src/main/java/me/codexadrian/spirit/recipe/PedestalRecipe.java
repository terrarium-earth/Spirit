package me.codexadrian.spirit.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.SyncedData;
import me.codexadrian.spirit.data.TagAndListSetCodec;
import me.codexadrian.spirit.registry.SpiritItems;
import me.codexadrian.spirit.registry.SpiritMisc;
import me.codexadrian.spirit.registry.SpiritRecipes;
import me.codexadrian.spirit.utils.CodecUtils;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.Optional;

public record PedestalRecipe(ResourceLocation id, HolderSet<EntityType<?>> entityInput, Ingredient activationItem, boolean consumesActivator, List<Ingredient> ingredients,
                             EntityType<?> entityOutput, int duration, boolean shouldSummon,
                             Optional<CompoundTag> outputNbt) implements SyncedData {
    public static Codec<PedestalRecipe> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                TagAndListSetCodec.of(Registry.ENTITY_TYPE).fieldOf("entityInput").forGetter(PedestalRecipe::entityInput),
                CodecUtils.INGREDIENT_CODEC.fieldOf("activatorItem").orElse(Ingredient.of()).forGetter(PedestalRecipe::activationItem),
                Codec.BOOL.fieldOf("consumesActivator").orElse(false).forGetter(PedestalRecipe::consumesActivator),
                CodecUtils.INGREDIENT_CODEC.listOf().fieldOf("itemInputs").forGetter(PedestalRecipe::ingredients),
                Registry.ENTITY_TYPE.byNameCodec().fieldOf("entityOutput").forGetter(PedestalRecipe::entityOutput),
                Codec.INT.fieldOf("duration").orElse(60).forGetter(PedestalRecipe::duration),
                Codec.BOOL.fieldOf("shouldSummonMob").orElse(false).forGetter(PedestalRecipe::shouldSummon),
                CompoundTag.CODEC.optionalFieldOf("outputNbt").forGetter(PedestalRecipe::outputNbt)
        ).apply(instance, PedestalRecipe::new));
    }

    @Override
    public ResourceLocation getId() {
        return new ResourceLocation(Spirit.MODID, "soul_transmutation");
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpiritMisc.SOUL_TRANSMUTATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SpiritRecipes.getSoulTransmutationRecipe().get();
    }

    public static List<PedestalRecipe> getRecipesForEntity(EntityType<?> entity, ItemStack stack, RecipeManager manager) {
        return manager.getAllRecipesFor(SpiritRecipes.getSoulTransmutationRecipe().get()).stream().filter(recipe -> recipe.entityInput().contains(entity.builtInRegistryHolder()) && recipe.activationItem().test(stack)).toList();
    }

    public static Optional<PedestalRecipe> getEffect(String id, RecipeManager manager) {
        return (Optional<PedestalRecipe>) manager.byKey(ResourceLocation.tryParse(id));
    }
}
