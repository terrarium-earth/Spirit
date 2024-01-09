package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmorMaterial;
import earth.terrarium.spirit.common.item.crystals.*;
import earth.terrarium.spirit.common.item.tools.SoulSteelToolItem;
import earth.terrarium.spirit.common.item.trinkets.AllayCharm;
import earth.terrarium.spirit.common.item.trinkets.BaseTrinket;
import earth.terrarium.spirit.common.item.utilities.SkeletonsBow;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;

public class SpiritItems {

    public static final ResourcefulRegistry<CreativeModeTab> CREATIVE_MODE_TABS = ResourcefulRegistries.create(BuiltInRegistries.CREATIVE_MODE_TAB, Spirit.MODID);

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, Spirit.MODID);
    public static final ResourcefulRegistry<Item> ARMOR = ResourcefulRegistries.create(ITEMS);
    public static final ResourcefulRegistry<Item> TOOLS = ResourcefulRegistries.create(ITEMS);

    //crystals
    public static final RegistryEntry<Item> SOUL_CRYSTAL = ITEMS.register("soul_crystal", () -> new SoulCrystalItem(new Item.Properties()));
    public static final RegistryEntry<Item> MOB_CRYSTAL = ITEMS.register("mob_crystal", () -> new MobCrystalItem(new Item.Properties()));
    public static final RegistryEntry<Item> IGNITION_CRYSTAL = ITEMS.register("ignition_crystal", () -> new IgnitionCrystalItem(new Item.Properties().durability(128)));
    public static final RegistryEntry<Item> INSIGHT_CRYSTAL = ITEMS.register("insight_crystal", () -> new InsightCrystalItem(new Item.Properties()));

    public static final RegistryEntry<CreativeModeTab> SPIRIT_TAB = CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).icon(() -> SOUL_CRYSTAL.get().getDefaultInstance()).title(Component.translatable("itemGroup.spirit.main")).build());

    //pedestals
    public static final RegistryEntry<Item> PEDESTAL = ITEMS.register("pedestal", () -> new BlockItem(SpiritBlocks.PEDESTAL.get(), new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_BASIN = ITEMS.register("soul_basin", () -> new BlockItem(SpiritBlocks.SOUL_BASIN.get(), new Item.Properties()));

    //tools
    public static final RegistryEntry<Item> SCYTHE = TOOLS.register("scythe", () -> new SoulSteelToolItem(BlockTags.MINEABLE_WITH_HOE, new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_HAMMER = TOOLS.register("soul_steel_hammer", () -> new SoulSteelToolItem(BlockTags.MINEABLE_WITH_PICKAXE, new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_EXCAVATOR = TOOLS.register("soul_steel_excavator", () -> new SoulSteelToolItem(BlockTags.MINEABLE_WITH_SHOVEL, new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_BATTLEAXE = TOOLS.register("soul_steel_battleaxe", () -> new SoulSteelToolItem(BlockTags.MINEABLE_WITH_AXE, new Item.Properties()));
    public static final RegistryEntry<Item> SKELETON_BOW = TOOLS.register("skeleton_bow", () -> new SkeletonsBow(new Item.Properties()));

    //crafting materials
    public static final RegistryEntry<Item> CRYSTAL_SHARD = ITEMS.register("crystal_shard", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_INGOT = ITEMS.register("soul_steel_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_FIRE_CHARGE = ITEMS.register("soul_fire_charge", () -> new SoulFireChargeItem(new Item.Properties()));

    // armor
    private static final ArmorMaterial SOUL_STEEL_ARMOR_MATERIAL = new SoulSteelArmorMaterial();
    public static final RegistryEntry<Item> SOUL_STEEL_HELMET = ARMOR.register("soul_steel_helmet", () -> new SoulSteelArmor(SOUL_STEEL_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_CHESTPLATE = ARMOR.register("soul_steel_chestplate", () -> new SoulSteelArmor(SOUL_STEEL_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_LEGGINGS = ARMOR.register("soul_steel_leggings", () -> new SoulSteelArmor(SOUL_STEEL_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_BOOTS = ARMOR.register("soul_steel_boots", () -> new SoulSteelArmor(SOUL_STEEL_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties()));

    //trinkets
    public static final RegistryEntry<Item> CAT_CHARM = ITEMS.register("cat_charm", () -> new BaseTrinket(new Item.Properties().stacksTo(1)));
    public static final RegistryEntry<Item> PIGLIN_CHARM = ITEMS.register("piglin_charm", () -> new BaseTrinket(new Item.Properties().stacksTo(1)));
    public static final RegistryEntry<Item> PHANTOM_CHARM = ITEMS.register("phantom_charm", () -> new BaseTrinket(new Item.Properties().stacksTo(1)));
    public static final RegistryEntry<Item> ALLAY_CHARM = ITEMS.register("allay_charm", () -> new AllayCharm(new Item.Properties().stacksTo(1)));
    public static final RegistryEntry<Item> IRON_GOLEM_CHARM = ITEMS.register("iron_golem_charm", () -> new BaseTrinket(new Item.Properties().stacksTo(1)));

    //blocks
    public static final RegistryEntry<Item> SOUL_STEEL_BLOCK = ITEMS.register("soul_steel_block", () -> new BlockItem(SpiritBlocks.SOUL_STEEL_BLOCK.get(), new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_SLATE = ITEMS.register("soul_slate", () -> new BlockItem(SpiritBlocks.SOUL_SLATE.get(), new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_GLASS = ITEMS.register("soul_glass", () -> new BlockItem(SpiritBlocks.SOUL_GLASS.get(), new Item.Properties()));

    public static void onRegisterCreativeTabs(TriConsumer<ResourceLocation, RegistryEntry<Item>, List<Item>> consumer) {
        consumer.accept(new ResourceLocation(Spirit.MODID, "main"), SOUL_CRYSTAL, ITEMS.stream().map(RegistryEntry::get).toList());
    }
}
