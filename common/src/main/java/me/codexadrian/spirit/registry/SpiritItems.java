package me.codexadrian.spirit.registry;

import me.codexadrian.spirit.items.*;
import me.codexadrian.spirit.items.tools.SoulSteelBow;
import me.codexadrian.spirit.items.tools.SoulSteelShield;
import me.codexadrian.spirit.items.tools.SoulSteelSword;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

import static me.codexadrian.spirit.Spirit.SPIRIT;
import static me.codexadrian.spirit.platform.Services.REGISTRY;

public class SpiritItems {
    public static final Supplier<Item> SOUL_CRYSTAL = REGISTRY.registerItem("soul_crystal", () ->
            new SoulCrystalItem(new Item.Properties().tab(SPIRIT).stacksTo(1).rarity(Rarity.RARE)));

    public static final Supplier<Item> CRUDE_SOUL_CRYSTAL = REGISTRY.registerItem("crude_soul_crystal", () ->
            new CrudeSoulCrystalItem(new Item.Properties().tab(SPIRIT).stacksTo(1).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_BOW = REGISTRY.registerItem("soul_bow", () ->
            new SoulSteelBow( new Item.Properties().tab(SPIRIT).durability(64).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_STEEL = REGISTRY.registerItem("soul_steel_ingot", () ->
            new Item( new Item.Properties().tab(SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_STEEL_BLADE = REGISTRY.registerItem("soul_blade", () ->
            new SoulSteelSword( new Item.Properties().tab(SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_STEEL_SHIELD = REGISTRY.registerItem("soul_steel_shield", () ->
            new SoulSteelShield( new Item.Properties().tab(SPIRIT).rarity(Rarity.RARE)));

    public static final Supplier<Item> SOUL_STEEL_WAND = REGISTRY.registerItem("soul_steel_wand", () -> new Item(new Item.Properties().tab(SPIRIT).rarity(Rarity.RARE)));

    public static void registerAll() {
    }
}
