package me.codexadrian.spirit.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import me.codexadrian.spirit.Constants;
import me.codexadrian.spirit.Spirit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Function;

/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
 * @author Team Resourceful
 */

public class CodecRecipeSerializer<R extends Recipe<?>> implements RecipeSerializer<R> {
    private static final Gson GSON = new Gson();
    private final RecipeType<R> recipeType;
    private final Function<ResourceLocation, Codec<R>> codecInitializer;

    public CodecRecipeSerializer(RecipeType<R> recipeType, Function<ResourceLocation, Codec<R>> codecInitializer) {
        this.recipeType = recipeType;
        this.codecInitializer = codecInitializer;
    }

    @Override
    public @NotNull R fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
        return codecInitializer.apply(id).parse(JsonOps.INSTANCE, json).getOrThrow(false, s -> Spirit.LOGGER.error("Could not parse {}", id));
    }

    @Nullable
    @Override
    public R fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buffer) {
        Optional<R> result = codecInitializer.apply(id).parse(JsonOps.COMPRESSED, GSON.fromJson(buffer.readUtf(), JsonArray.class)).result();
        return result.orElse(null);
    }

    @Override
    public void toNetwork(@NotNull FriendlyByteBuf buffer, @NotNull R recipe) {
        codecInitializer.apply(recipe.getId()).encodeStart(JsonOps.COMPRESSED, recipe).result().ifPresent(element -> buffer.writeUtf(element.toString()));
    }

    public RecipeType<R> type() {
        return recipeType;
    }
}