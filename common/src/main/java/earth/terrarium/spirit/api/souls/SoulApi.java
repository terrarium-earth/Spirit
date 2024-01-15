package earth.terrarium.spirit.api.souls;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.souls.base.SoulContainer;
import earth.terrarium.spirit.api.souls.base.SoulContainingBlock;
import earth.terrarium.spirit.api.souls.base.SoulContainingItem;
import earth.terrarium.spirit.api.souls.stack.SoulStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SoulApi {
    public static final String SOULLESS_TAG = "Soulless";

    private static final Map<Supplier<BlockEntityType<?>>, SoulContainingBlock<?>> BLOCK_ENTITY_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Block>, SoulContainingBlock<?>> BLOCK_LOOKUP_MAP = new HashMap<>();
    private static final Map<Supplier<Item>, SoulContainingItem<?>> ITEM_LOOKUP_MAP = new HashMap<>();

    private static Map<BlockEntityType<?>, SoulContainingBlock<?>> FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = null;
    private static Map<Block, SoulContainingBlock<?>> FINALIZED_BLOCK_LOOKUP_MAP = null;
    private static Map<Item, SoulContainingItem<?>> FINALIZED_ITEM_LOOKUP_MAP = null;

    public static <T, U> Map<T, U> finalizeRegistration(Map<Supplier<T>, U> unfinalized, @Nullable Map<T, U> finalized, String type) {
        if (finalized == null) {
            Spirit.LOGGER.debug("Finalizing {} registration", type);
            Map<T, U> collected = unfinalized.entrySet().stream().map(entry -> Pair.of(entry.getKey().get(), entry.getValue())).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond));
            unfinalized.clear();
            return collected;
        }

        return finalized;
    }

    public static Map<BlockEntityType<?>, SoulContainingBlock<?>> getBlockEntityRegistry() {
        return FINALIZED_BLOCK_ENTITY_LOOKUP_MAP = finalizeRegistration(BLOCK_ENTITY_LOOKUP_MAP, FINALIZED_BLOCK_ENTITY_LOOKUP_MAP, "soul containing block entity");
    }

    public static Map<Block, SoulContainingBlock<?>> getBlockRegistry() {
        return FINALIZED_BLOCK_LOOKUP_MAP = finalizeRegistration(BLOCK_LOOKUP_MAP, FINALIZED_BLOCK_LOOKUP_MAP, "soul containing block");
    }

    public static Map<Item, SoulContainingItem<?>> getItemRegistry() {
        return FINALIZED_ITEM_LOOKUP_MAP = finalizeRegistration(ITEM_LOOKUP_MAP, FINALIZED_ITEM_LOOKUP_MAP, "soul containing item");
    }

    public static SoulContainingBlock<?> getSoulContainingBlock(Block block) {
        return getBlockRegistry().get(block);
    }

    public static SoulContainingBlock<?> getSoulContainingBlock(BlockEntityType<?> blockEntity) {
        return getBlockEntityRegistry().get(blockEntity);
    }

    public static SoulContainingItem<?> getSoulContainingItem(Item item) {
        return getItemRegistry().get(item);
    }

    public static void registerBlockEntity(Supplier<BlockEntityType<?>> block, SoulContainingBlock<?> getter) {
        BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerBlockEntity(SoulContainingBlock<?> getter, Supplier<BlockEntityType<?>>... blocks) {
        for (Supplier<BlockEntityType<?>> block : blocks) {
            BLOCK_ENTITY_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerBlock(Supplier<Block> block, SoulContainingBlock<?> getter) {
        BLOCK_LOOKUP_MAP.put(block, getter);
    }

    @SafeVarargs
    public static void registerBlock(SoulContainingBlock<?> getter, Supplier<Block>... blocks) {
        for (Supplier<Block> block : blocks) {
            BLOCK_LOOKUP_MAP.put(block, getter);
        }
    }

    public static void registerItem(Supplier<Item> item, SoulContainingItem<?> getter) {
        ITEM_LOOKUP_MAP.put(item, getter);
    }

    @SafeVarargs
    public static void registerItem(SoulContainingItem<?> getter, Supplier<Item>... items) {
        for (Supplier<Item> item : items) {
            ITEM_LOOKUP_MAP.put(item, getter);
        }
    }
}
