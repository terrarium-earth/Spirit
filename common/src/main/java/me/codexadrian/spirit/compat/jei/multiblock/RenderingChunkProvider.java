package me.codexadrian.spirit.compat.jei.multiblock;

import com.mojang.datafixers.util.Pair;
import me.codexadrian.spirit.recipe.SoulEngulfingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.stream.Collectors;
/*
public class RenderingChunkProvider extends ChunkSource {
    private final Holder<Biome> VOID;
    private final SoulEngulfingRecipe recipe;

    private final Map<ChunkPos, ChunkAccess> chunks;
    private final RenderingWorld renderingLevel;
    private final LevelLightEngine lightManager;

    public RenderingChunkProvider(RenderingWorld renderingLevel, SoulEngulfingRecipe recipe) {
        VOID = ForgeRegistries.BIOMES.getHolder(Biomes.THE_VOID).get();

        this.recipe = recipe;

        this.renderingLevel = renderingLevel;
        this.lightManager = new LevelLightEngine(this, true, true);

        Map<ChunkPos, List<BlockPos>> byChunk = new HashMap<>();
        BlockPos.betweenClosedStream(this.recipe.getDimensions())
                .map(BlockPos::immutable)
                .forEach(pos -> {
                    byChunk.computeIfAbsent(new ChunkPos(pos), $ -> new ArrayList<>()).add(pos);
                });

        chunks = byChunk.keySet().stream()
                .map(chunkPos -> Pair.of(chunkPos, new RecipeChunk(this.renderingLevel, chunkPos, recipe)))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
    }

    @Nullable
    @Override
    public ChunkAccess getChunk(int cx, int cz, ChunkStatus status, boolean load) {
        return chunks.computeIfAbsent(new ChunkPos(cx, cz), p -> new EmptyLevelChunk(renderingLevel, p, VOID));
    }

    @Override
    public void tick(BooleanSupplier bool, boolean bool2) {

    }

    @Override
    public String gatherStats() {
        return "?";
    }

    @Override
    public int getLoadedChunksCount() {
        return chunks.size();
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return lightManager;
    }

    @Override
    public BlockGetter getLevel() {
        return renderingLevel;
    }
}
 */