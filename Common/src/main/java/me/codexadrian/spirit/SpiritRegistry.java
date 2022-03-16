package me.codexadrian.spirit;

import me.codexadrian.spirit.blocks.soulcage.SoulCageBlock;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlockEntity;
import me.codexadrian.spirit.items.DivineCrystalItem;
import me.codexadrian.spirit.platform.Services;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

import static me.codexadrian.spirit.Spirit.SPIRIT;

public class SpiritRegistry {
    public static final Supplier<SoulCageBlock> SOUL_CAGE = () -> new SoulCageBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops());
    public static final Supplier<BlockEntityType<SoulCageBlockEntity>> SOUL_CAGE_ENTITY = () -> Services.REGISTRY.createBlockEntityType(SoulCageBlockEntity::new, SOUL_CAGE.get());
    public static final Supplier<BlockItem> SOUL_CAGE_ITEM = () -> new BlockItem(SOUL_CAGE.get(), new Item.Properties().tab(SPIRIT).rarity(Rarity.EPIC));
    public static final Supplier<DivineCrystalItem> SOUL_CRYSTAL = () -> new DivineCrystalItem(new Item.Properties().tab(SPIRIT).stacksTo(1).rarity(Rarity.RARE));
    public static final Supplier<Block> BROKEN_SPAWNER = () -> new Block(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops());
    public static final Supplier<BlockItem> BROKEN_SPAWNER_ITEM = () -> new BlockItem(BROKEN_SPAWNER.get(), new Item.Properties().tab(SPIRIT).rarity(Rarity.EPIC));

    private static Supplier<Item> registerItem(String id, Supplier<Item> item) {
        return Services.REGISTRY.registerItem(id, item);
    }

    private static Supplier<Block> registerBlock(String id, Supplier<Block> item) {
        return Services.REGISTRY.registerBlock(id, item);
    }

    private static Supplier<BlockEntityType<SoulCageBlockEntity>> register(String id, Supplier<BlockEntityType<SoulCageBlockEntity>> item) {
        return Services.REGISTRY.registerBlockEntity(id, item);
    }

    public static void registerAll() {
        //TEEHEE
    }
}
