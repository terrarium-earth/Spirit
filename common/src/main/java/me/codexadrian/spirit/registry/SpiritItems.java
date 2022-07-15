package me.codexadrian.spirit.registry;

import me.codexadrian.spirit.items.*;
import me.codexadrian.spirit.items.tools.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

import static me.codexadrian.spirit.Spirit.SPIRIT;
import static me.codexadrian.spirit.platform.Services.REGISTRY;

public class SpiritItems {
    public static final Supplier<Item> SOUL_CRYSTAL_SHARD = REGISTRY.registerItem("soul_crystal_shard", () ->
            new MobCrystalItem(new Item.Properties().tab(SPIRIT).stacksTo(1)));

    public static final Supplier<Item> CRUDE_SOUL_CRYSTAL = REGISTRY.registerItem("crude_soul_crystal", () ->
            new CrudeSoulCrystalItem(new Item.Properties().tab(SPIRIT).stacksTo(1)));

    public static final Supplier<Item> SOUL_CRYSTAL = REGISTRY.registerItem("soul_crystal", () ->
            new SoulCrystalItem(new Item.Properties().tab(SPIRIT).stacksTo(1).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_STEEL = REGISTRY.registerItem("soul_steel_ingot", () ->
            new Item(new Item.Properties().tab(SPIRIT)));

    public static final Supplier<Item> SOUL_POWDER = REGISTRY.registerItem("soul_powder", () ->
            new Item(new Item.Properties().tab(SPIRIT)));

    public static final Supplier<Item> SOUL_STEEL_AXE = REGISTRY.registerItem("soul_steel_axe", () ->
            new SoulSteelAxe(new Item.Properties().tab(SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_BOW = REGISTRY.registerItem("soul_steel_bow", () ->
            new SoulSteelBow(new Item.Properties().tab(SPIRIT).durability(64).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_STEEL_HOE = REGISTRY.registerItem("soul_steel_hoe", () ->
            new SoulSteelHoe(new Item.Properties().tab(SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_STEEL_PICKAXE = REGISTRY.registerItem("soul_steel_pickaxe", () ->
            new SoulSteelPickaxe(new Item.Properties().tab(SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_STEEL_SHOVEL = REGISTRY.registerItem("soul_steel_shovel", () ->
            new SoulSteelShovel(new Item.Properties().tab(SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_STEEL_BLADE = REGISTRY.registerItem("soul_steel_sword", () ->
            new SoulSteelSword(new Item.Properties().tab(SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_STEEL_WAND = REGISTRY.registerItem("soul_steel_wand", () -> new Item(new Item.Properties().tab(SPIRIT).stacksTo(1).rarity(Rarity.RARE)));

    public static void registerAll() {
    }
}
