package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.elements.BaseElements;
import earth.terrarium.spirit.common.item.ElementalCrystalItem;
import earth.terrarium.spirit.common.item.MobCrystalItem;
import earth.terrarium.spirit.common.item.ScytheItem;
import earth.terrarium.spirit.common.item.SoulCrystalItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SpiritItems {

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, Spirit.MODID);

    //crystals
    public static final Supplier<Item> SOUL_CRYSTAL = ITEMS.register("soul_crystal", () -> new SoulCrystalItem(new Item.Properties()));
    public static final Supplier<Item> MOB_CRYSTAL = ITEMS.register("mob_crystal", () -> new MobCrystalItem(new Item.Properties()));

    //cage
    public static final Supplier<Item> SOUL_CAGE = ITEMS.register("soul_cage", () -> new BlockItem(SpiritBlocks.SOUL_CAGE.get(), new Item.Properties()));

    //pedestals
    public static final Supplier<Item> PEDESTAL = ITEMS.register("pedestal", () -> new BlockItem(SpiritBlocks.PEDESTAL.get(), new Item.Properties()));
    public static final Supplier<Item> SUMMONING_PEDESTAL = ITEMS.register("summoning_pedestal", () -> new BlockItem(SpiritBlocks.SUMMONING_PEDESTAL.get(), new Item.Properties()));
    public static final Supplier<Item> SOUL_BASIN = ITEMS.register("soul_basin", () -> new BlockItem(SpiritBlocks.SOUL_BASIN.get(), new Item.Properties()));

    //tools
    public static final Supplier<Item> SCYTHE = ITEMS.register("scythe", () -> new ScytheItem(new Item.Properties()));

    //crafting materials
    public static final Supplier<Item> SOUL_SHARD = ITEMS.register("soul_shard", () -> new Item(new Item.Properties()));
    public static final Supplier<Item> SOUL_STEEL_INGOT = ITEMS.register("soul_steel_ingot", () -> new Item(new Item.Properties()));

    //elemental crystals fire, water, earth, ender
    public static final Supplier<Item> EMBER_CRYSTAL = ITEMS.register("ember_crystal", () -> new ElementalCrystalItem(BaseElements.EMBER, new Item.Properties()));
    public static final Supplier<Item> WATER_CRYSTAL = ITEMS.register("water_crystal", () -> new ElementalCrystalItem(BaseElements.WATER, new Item.Properties()));
    public static final Supplier<Item> EARTH_CRYSTAL = ITEMS.register("earth_crystal", () -> new ElementalCrystalItem(BaseElements.EARTH, new Item.Properties()));
    public static final Supplier<Item> ENDER_CRYSTAL = ITEMS.register("ender_crystal", () -> new ElementalCrystalItem(BaseElements.ENDER, new Item.Properties()));
}
