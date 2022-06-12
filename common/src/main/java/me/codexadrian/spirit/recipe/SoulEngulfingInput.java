package me.codexadrian.spirit.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

//Ingredient Codec stolen from Resourceful bees

public record SoulEngulfingInput(Ingredient item, BlockPredicate catalyst) {
    public static final Codec<Ingredient> INGREDIENT_CODEC = Codec.PASSTHROUGH.comapFlatMap(SoulEngulfingInput::decodeIngredient, SoulEngulfingInput::encodeIngredient);
    public static final Codec<BlockPredicate> BLOCK_PREDICATE_CODEC = Codec.PASSTHROUGH.comapFlatMap(SoulEngulfingInput::decodeBlockPredicate, SoulEngulfingInput::encodeBlockPredicate);

    public static final Codec<SoulEngulfingInput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            INGREDIENT_CODEC.fieldOf("ingredient").forGetter(SoulEngulfingInput::item),
            BLOCK_PREDICATE_CODEC.fieldOf("catalyst").orElse(BlockPredicate.ANY).forGetter(SoulEngulfingInput::catalyst)
    ).apply(instance, SoulEngulfingInput::new));


    private static DataResult<Ingredient> decodeIngredient(Dynamic<?> dynamic) {
        return DataResult.success(Ingredient.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue()));
    }

    private static Dynamic<JsonElement> encodeIngredient(Ingredient ingredient) {
        return new Dynamic<>(JsonOps.INSTANCE, ingredient.toJson());
    }

    private static DataResult<BlockPredicate> decodeBlockPredicate(Dynamic<?> dynamic) {
        return DataResult.success(BlockPredicate.fromJson(dynamic.convert(JsonOps.INSTANCE).getValue()));
    }

    private static Dynamic<JsonElement> encodeBlockPredicate(BlockPredicate blockPredicate) {
        return new Dynamic<>(JsonOps.INSTANCE, blockPredicate.serializeToJson());
    }
}
