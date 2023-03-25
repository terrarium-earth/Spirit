package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.item.ElementalCrystalItem;
import earth.terrarium.spirit.common.item.MobCrystalItem;
import earth.terrarium.spirit.common.item.ScytheItem;
import earth.terrarium.spirit.common.item.SoulCrystalItem;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmorMaterial;
import earth.terrarium.spirit.common.item.trinkets.AllayCharm;
import earth.terrarium.spirit.common.item.trinkets.BaseTrinket;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.List;

public class SpiritItems {

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, Spirit.MODID);

    //crystals
    public static final RegistryEntry<Item> SOUL_CRYSTAL = ITEMS.register("soul_crystal", () -> new SoulCrystalItem(new Item.Properties()));
    public static final RegistryEntry<Item> MOB_CRYSTAL = ITEMS.register("mob_crystal", () -> new MobCrystalItem(new Item.Properties()));

    //pedestals
    public static final RegistryEntry<Item> PEDESTAL = ITEMS.register("pedestal", () -> new BlockItem(SpiritBlocks.PEDESTAL.get(), new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_BASIN = ITEMS.register("soul_basin", () -> new BlockItem(SpiritBlocks.SOUL_BASIN.get(), new Item.Properties()));

    //tools
    public static final RegistryEntry<Item> SCYTHE = ITEMS.register("scythe", () -> new ScytheItem(new Item.Properties()));

    //crafting materials
    public static final RegistryEntry<Item> CRYSTAL_SHARD = ITEMS.register("crystal_shard", () -> new Item(new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_INGOT = ITEMS.register("soul_steel_ingot", () -> new Item(new Item.Properties()));

    // armor
    private static final ArmorMaterial SOUL_STEEL_ARMOR_MATERIAL = new SoulSteelArmorMaterial();
    public static final RegistryEntry<Item> SOUL_STEEL_HELMET = ITEMS.register("soul_steel_helmet", () -> new SoulSteelArmor(SOUL_STEEL_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_CHESTPLATE = ITEMS.register("soul_steel_chestplate", () -> new SoulSteelArmor(SOUL_STEEL_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_LEGGINGS = ITEMS.register("soul_steel_leggings", () -> new SoulSteelArmor(SOUL_STEEL_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryEntry<Item> SOUL_STEEL_BOOTS = ITEMS.register("soul_steel_boots", () -> new SoulSteelArmor(SOUL_STEEL_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties()));

    //trinkets
    public static final RegistryEntry<Item> CAT_CHARM = ITEMS.register("cat_charm", () -> new BaseTrinket(new Item.Properties()));
    public static final RegistryEntry<Item> PIGLIN_CHARM = ITEMS.register("piglin_charm", () -> new BaseTrinket(new Item.Properties()));
    public static final RegistryEntry<Item> PHANTOM_CHARM = ITEMS.register("phantom_charm", () -> new BaseTrinket(new Item.Properties()));
    public static final RegistryEntry<Item> ALLAY_CHARM = ITEMS.register("allay_charm", () -> new AllayCharm(new Item.Properties()));
    public static final RegistryEntry<Item> IRON_GOLEM_CHARM = ITEMS.register("iron_golem_charm", () -> new BaseTrinket(new Item.Properties()));

    public static void onRegisterCreativeTabs(TriConsumer<ResourceLocation, RegistryEntry<Item>, List<Item>> consumer) {
        consumer.accept(new ResourceLocation(Spirit.MODID, "main"), SOUL_CRYSTAL, BuiltInRegistries.ITEM
                .stream()
                .filter(i -> BuiltInRegistries.ITEM.getKey(i).getNamespace().equals(Spirit.MODID))
                .toList());
    }
}
