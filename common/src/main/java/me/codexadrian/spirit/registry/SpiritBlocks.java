package me.codexadrian.spirit.registry;

import me.codexadrian.spirit.blocks.BrokenSpawnerBlock;
import me.codexadrian.spirit.blocks.PedestalBlock;
import me.codexadrian.spirit.blocks.SoulCageBlock;
import me.codexadrian.spirit.blocks.SoulPedestalBlock;
import me.codexadrian.spirit.blocks.blockentity.PedestalBlockEntity;
import me.codexadrian.spirit.blocks.blockentity.SoulCageBlockEntity;
import me.codexadrian.spirit.blocks.blockentity.SoulPedestalBlockEntity;
import me.codexadrian.spirit.items.ChippedBlockItem;
import me.codexadrian.spirit.platform.Services;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.function.Supplier;

import static me.codexadrian.spirit.Spirit.SPIRIT;
import static me.codexadrian.spirit.platform.Services.REGISTRY;

public class SpiritBlocks {
    public static final ArrayList<Supplier<Block>> SOUL_GLASS_BLOCKS = new ArrayList<>();

    public static final Supplier<Block> SOUL_CAGE = registerBlockWithItem("soul_cage", () ->
            new SoulCageBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops()));

    public static final Supplier<BlockEntityType<SoulCageBlockEntity>> SOUL_CAGE_ENTITY =
            REGISTRY.registerBlockEntity("soul_cage", () -> Services.REGISTRY.createBlockEntityType(SoulCageBlockEntity::new, SOUL_CAGE.get()));

    public static final Supplier<Block> SOUL_PEDESTAL = registerBlockWithItem("soul_pedestal", () ->
            new SoulPedestalBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops()));

    public static final Supplier<BlockEntityType<SoulPedestalBlockEntity>> SOUL_PEDESTAL_ENTITY =
            REGISTRY.registerBlockEntity("soul_pedestal", () -> Services.REGISTRY.createBlockEntityType(SoulPedestalBlockEntity::new, SOUL_PEDESTAL.get()));

    public static final Supplier<Block> PEDESTAL = registerBlockWithItem("pedestal", () ->
            new PedestalBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops()));

    public static final Supplier<BlockEntityType<PedestalBlockEntity>> PEDESTAL_ENTITY =
            REGISTRY.registerBlockEntity("pedestal", () -> Services.REGISTRY.createBlockEntityType(PedestalBlockEntity::new, PEDESTAL.get()));

    private static Supplier<Block> registerBlockWithItem(String name, Supplier<Block> block, Item.Properties properties) {
        var newBlock = REGISTRY.registerBlock(name, block);
        REGISTRY.registerItem(name, () -> new BlockItem(newBlock.get(), properties));
        return newBlock;
    }

    private static Supplier<Block> registerBlockWithItem(String name, Supplier<Block> block) {
        return registerBlockWithItem(name, block, new Item.Properties().tab(SPIRIT));
    }

    public static final Supplier<Block> SOUL_GLASS = registerBlockWithItem("soul_glass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));

    public static final Supplier<Block> SOUL_STEEL_BLOCK = registerBlockWithItem("soul_steel_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), new Item.Properties().tab(SPIRIT).rarity(Rarity.RARE));

    public static final Supplier<Block> BROKEN_SPAWNER = registerBlockWithItem("broken_spawner", BrokenSpawnerBlock::new);

    private static void registerChippedVariants(String name, Supplier<Block> block, int blocks) {
        for (int i = 1; i <= blocks; i++) {
            Supplier<Block> ctmBlock = REGISTRY.registerBlock(name + "_" + i, block);
            REGISTRY.registerItem(name + "_" + i, () -> new ChippedBlockItem(ctmBlock.get(), Services.PLATFORM.isModLoaded("chipped") ? new Item.Properties().tab(SPIRIT) : new Item.Properties()));
            SOUL_GLASS_BLOCKS.add(ctmBlock);
        }
    }

    public static void registerAll() {
        registerChippedVariants("soul_glass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)), 13);
    }
}
