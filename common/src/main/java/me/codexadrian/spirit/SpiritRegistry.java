package me.codexadrian.spirit;

import me.codexadrian.spirit.blocks.BrokenSpawnerBlock;
import me.codexadrian.spirit.blocks.SoulCageBlock;
import me.codexadrian.spirit.blocks.SoulPedestalBlock;
import me.codexadrian.spirit.blocks.blockentity.SoulCageBlockEntity;
import me.codexadrian.spirit.blocks.blockentity.SoulPedestalBlockEntity;
import me.codexadrian.spirit.enchantments.SoulReaperEnchantment;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import me.codexadrian.spirit.items.CrudeSoulCrystalItem;
import me.codexadrian.spirit.items.SoulCrystalItem;
import me.codexadrian.spirit.platform.Services;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

import static me.codexadrian.spirit.Spirit.SPIRIT;

public class SpiritRegistry {

    public static final Supplier<Block> SOUL_CAGE = registerBlock("soul_cage", () ->
            new SoulCageBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops()));

    public static final Supplier<BlockEntityType<SoulCageBlockEntity>> SOUL_CAGE_ENTITY =
            register("soul_cage", () -> Services.REGISTRY.createBlockEntityType(SoulCageBlockEntity::new, SOUL_CAGE.get()));

    public static final Supplier<Block> SOUL_PEDESTAL = registerBlock("soul_pedestal", () ->
            new SoulPedestalBlock(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops()));

    public static final Supplier<BlockEntityType<SoulPedestalBlockEntity>> SOUL_PEDESTAL_ENTITY =
            register("soul_pedestal", () -> Services.REGISTRY.createBlockEntityType(SoulPedestalBlockEntity::new, SOUL_PEDESTAL.get()));

    public static final Supplier<Item> SOUL_CAGE_ITEM = registerItem("soul_cage", () ->
            new BlockItem(SOUL_CAGE.get(), new Item.Properties().tab(SPIRIT).rarity(Rarity.EPIC)));

    public static final Supplier<Item> SOUL_PEDESTAL_ITEM = registerItem("soul_pedestal", () ->
            new BlockItem(SOUL_PEDESTAL.get(), new Item.Properties().tab(SPIRIT)));

    public static final Supplier<Item> SOUL_CRYSTAL = registerItem("soul_crystal", () ->
            new SoulCrystalItem(new Item.Properties().tab(SPIRIT).stacksTo(1).rarity(Rarity.RARE)));

    public static final Supplier<Item> CRUDE_SOUL_CRYSTAL = registerItem("crude_soul_crystal", () ->
            new CrudeSoulCrystalItem(new Item.Properties().tab(SPIRIT).stacksTo(1).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_BLADE = registerItem("soul_blade", () ->
            new SwordItem(new SoulSwordMaterial(), 3, -2.4F, new Item.Properties().tab(SPIRIT).stacksTo(1).rarity(Rarity.RARE)));

    public static final Supplier<Enchantment> SOUL_REAPER_ENCHANTMENT = registerEnchantment("soul_reaper", SoulReaperEnchantment::new);

    public static final Supplier<Block> BROKEN_SPAWNER = registerBlock("broken_spawner", BrokenSpawnerBlock::new);

    public static final Supplier<Item> BROKEN_SPAWNER_ITEM = registerItem("broken_spawner", () ->
            new BlockItem(BROKEN_SPAWNER.get(), new Item.Properties().tab(SPIRIT).rarity(Rarity.EPIC)));

    public static final Supplier<EntityType<SoulArrowEntity>> SOUL_ARROW_ENTITY = registerEntity("soul_arrow", SoulArrowEntity::new, MobCategory.MISC, 1, 1);

    private static Supplier<Item> registerItem(String id, Supplier<Item> item) {
        return Services.REGISTRY.registerItem(id, item);
    }

    private static Supplier<Block> registerBlock(String id, Supplier<Block> block) {
        return Services.REGISTRY.registerBlock(id, block);
    }

    private static Supplier<Enchantment> registerEnchantment(String id, Supplier<Enchantment> enchantment) {
        return Services.REGISTRY.registerEnchantment(id, enchantment);
    }

    private static <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> register(String id, Supplier<T> item) {
        return Services.REGISTRY.registerBlockEntity(id, item);
    }

    private static <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, EntityType.EntityFactory<T> item, MobCategory category, int width, int height) {
        return Services.REGISTRY.registerEntity(id, item, category, width, height);
    }

    public static void registerAll() {
        //TEEHEE
    }
}
