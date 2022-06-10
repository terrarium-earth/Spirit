package me.codexadrian.spirit.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import me.codexadrian.spirit.Constants;
import me.codexadrian.spirit.Spirit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public class SoulEnglufingReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private static final String FOLDER_ID = "soul_engulfing";

    public SoulEnglufingReloadListener() {
        super(GSON, FOLDER_ID);
    }

    @Override
    public void apply(Map<ResourceLocation, JsonElement> elements, ResourceManager manager, ProfilerFiller filter) {
        SoulEnguflingRecipeManager.getInstance().clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : elements.entrySet()) {
            ResourceLocation id = entry.getKey();
            SoulEngulfingRecipe recipe = SoulEngulfingRecipe.CODEC.parse(JsonOps.INSTANCE, entry.getValue().getAsJsonObject())
                    .getOrThrow(false, s ->
                            Constants.LOGGER.error("Unable to load soul engulfing data for {}, \n{}", id.toString(), s));

            SoulEnguflingRecipeManager.getInstance().addEntry(recipe);
        }
    }
}
