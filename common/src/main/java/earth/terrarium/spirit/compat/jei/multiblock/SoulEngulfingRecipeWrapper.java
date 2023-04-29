package earth.terrarium.spirit.compat.jei.multiblock;

import earth.terrarium.spirit.common.recipes.SoulEngulfingRecipe;
import earth.terrarium.spirit.common.recipes.SoulfireMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.block.Block;

import java.util.*;

public class SoulEngulfingRecipeWrapper {
    public final List<List<BlockMap>> blockMap;
    private final SoulEngulfingRecipe recipe;
    public int layer;

    public SoulEngulfingRecipeWrapper(SoulEngulfingRecipe recipe) {
        this.recipe = recipe;
        SoulfireMultiblock multiblock = getMultiblock();

        Map<BlockPos, Character> placementMap = new HashMap<>();
        for (int y = 0; y < multiblock.pattern().size(); y++) {
            for (int x = 0; x < multiblock.pattern().get(0).size(); x++) {
                char[] chars = multiblock.pattern().get(y).get(x).toCharArray();
                for (int z = 0; z < chars.length; z++) {
                    placementMap.put(new BlockPos(x, multiblock.pattern().size() - 1 - y, z), chars[z]);
                }
            }
        }
        List<List<BlockMap>> blockBlockBlock = new ArrayList<>();
        for (List<String> ignored : multiblock.pattern()) {
            blockBlockBlock.add(new ArrayList<>());
        }
        List<BlockMap> blocks = new ArrayList<>();
        var keys = new HashMap<>(multiblock.keys());
        keys.putAll(SoulfireMultiblock.RESERVED_VALUES);
        for (Map.Entry<BlockPos, Character> entry : placementMap.entrySet()) {
            if (keys.get(String.valueOf(entry.getValue())).blocks().isPresent()) {
                blocks.add(new BlockMap(entry.getKey(), convert(keys.get(String.valueOf(entry.getValue())).blocks().get())));
            }
        }
        for (BlockMap block : blocks) {
            blockBlockBlock.get(block.pos.getY()).add(block);
        }
        //Collections.reverse(blockBlockBlock);
        for (List<BlockMap> blockMaps : blockBlockBlock) {
            blockMaps.sort(Comparator.comparingInt(value -> value.pos.getZ()));
        }

        this.blockMap = blockBlockBlock;
        this.layer = this.blockMap.size();
    }

    public RotatableList<Block> convert(HolderSet<Block> set) {
        return new RotatableList<>(set.stream().filter(Holder::isBound).map(Holder::value).toList());
    }

    public void tick() {
        blockMap.forEach(blockMaps -> blockMaps.forEach(BlockMap::shuffle));
    }

    public SoulEngulfingRecipe getRecipe() {
        return recipe;
    }

    public SoulfireMultiblock getMultiblock() {
        return recipe.input().multiblock();
    }

    public record BlockMap(BlockPos pos, RotatableList<Block> blocks) {
        public void shuffle() {
            blocks.next();
        }
    }
}

