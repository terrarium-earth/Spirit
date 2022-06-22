package me.codexadrian.spirit.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.codexadrian.spirit.data.TagAndListSetCodec;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record SoulfireMultiblock(List<List<String>> pattern, Map<String, StrippedBlockPredicate> keys) {
    //'@' for soul fire
    //' ' for any

    public static final Codec<List<List<String>>> PATTERN_CODEC = Codec.STRING.listOf().listOf()
            .flatXmap(lists -> {
                if (shouldFail(lists)) return DataResult.error("Size invalid");
                return DataResult.success(lists);
            }, lists -> {
                if (shouldFail(lists)) return DataResult.error("Size invalid");
                return DataResult.success(lists);
            });

    public static final Codec<SoulfireMultiblock> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PATTERN_CODEC.fieldOf("pattern").forGetter(SoulfireMultiblock::pattern),
            Codec.unboundedMap(Codec.STRING, StrippedBlockPredicate.CODEC).fieldOf("keys").forGetter(SoulfireMultiblock::keys)
    ).apply(instance, SoulfireMultiblock::new));

    public static HashMap<String, StrippedBlockPredicate> RESERVED_VALUES = new HashMap<>();

    static {
        RESERVED_VALUES.put("@", new StrippedBlockPredicate(Optional.of(HolderSet.direct(Blocks.SOUL_FIRE.builtInRegistryHolder())), Optional.empty()));
        RESERVED_VALUES.put("&", new StrippedBlockPredicate(Optional.of(Registry.BLOCK.getOrCreateTag(BlockTags.SOUL_FIRE_BASE_BLOCKS)), Optional.empty()));
        RESERVED_VALUES.put(" ", StrippedBlockPredicate.ANY);
    }

    public static final SoulfireMultiblock DEFAULT_RECIPE = new SoulfireMultiblock(List.of(List.of("@"), List.of("&")), Map.of());

    public boolean validateMultiblock(BlockPos blockPos, ServerLevel level, boolean breakBlock) {
        BlockPos fireCoordinate = new BlockPos(0, 0, 0);
        for (int y = pattern.size() - 1; y >= 0; y--) {
            for (int x = 0; x < pattern.get(0).size(); x++) {
                char[] chars = pattern.get(y).get(x).toCharArray();
                for (int z = 0; z < chars.length; z++) {
                    if (chars[z] == '@') {
                        fireCoordinate = new BlockPos(x, y, z);
                        break;
                    }
                }
            }
        }

        Map<BlockPos, Character> placementMap = new HashMap<>();
        for (int y = pattern.size() - 1; y >= 0; y--) {
            for (int x = 0; x < pattern.get(0).size(); x++) {
                char[] chars = pattern.get(y).get(x).toCharArray();
                for (int z = 0; z < chars.length; z++) {
                    placementMap.put(blockPos.offset(x - fireCoordinate.getX(), fireCoordinate.getY() - y, z - fireCoordinate.getZ()).immutable(), chars[z]);
                }
            }
        }
        for (Map.Entry<BlockPos, Character> block : placementMap.entrySet()) {
            String key = String.valueOf(block.getValue());
            StrippedBlockPredicate blockPredicate = keys.get(key);
            if (blockPredicate == null) blockPredicate = RESERVED_VALUES.get(key);
            if (blockPredicate == null || !blockPredicate.matches(level, block.getKey())) {
                return false;
            }
        }
        if (breakBlock) {
            for (Map.Entry<BlockPos, Character> block : placementMap.entrySet()) {
                String key = String.valueOf(block.getValue());
                StrippedBlockPredicate blockPredicate = keys.get(key);
                if (blockPredicate == null) blockPredicate = RESERVED_VALUES.get(key);
                if (blockPredicate != null) {
                    if (blockPredicate.matches(level, block.getKey())) {
                        level.destroyBlock(block.getKey(), false);
                    }
                }
            }
        }
        return true;
    }

    public static boolean shouldFail(List<List<String>> lists) {
        for (List<String> list : lists) {
            for (String s : list) {
                if (s.length() != list.size()) return true;
            }
        }
        return false;
    }

    public record StrippedBlockPredicate(Optional<HolderSet<Block>> blocks, Optional<CompoundTag> nbtTag) {
        public static final StrippedBlockPredicate ANY = new StrippedBlockPredicate(Optional.empty(), Optional.empty());
        public static final Codec<StrippedBlockPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                TagAndListSetCodec.of(Registry.BLOCK).optionalFieldOf("block").forGetter(StrippedBlockPredicate::blocks),
                CompoundTag.CODEC.optionalFieldOf("nbtTag").forGetter(StrippedBlockPredicate::nbtTag)
        ).apply(instance, StrippedBlockPredicate::new));

        public boolean matches(ServerLevel serverLevel, BlockPos blockPos) {
            if (blocks().isPresent()) {
                if (!serverLevel.getBlockState(blockPos).is(this.blocks().get())) return false;
            }
            if (nbtTag().isPresent()) {
                var blockEntity = serverLevel.getBlockEntity(blockPos);
                if (blockEntity != null)
                    return new NbtPredicate(this.nbtTag().get()).matches(blockEntity.saveWithFullMetadata());
            }
            return true;
        }
    }
}
