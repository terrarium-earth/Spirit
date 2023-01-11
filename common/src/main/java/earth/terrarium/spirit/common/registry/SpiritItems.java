package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.item.MobCrystalItem;
import earth.terrarium.spirit.common.item.SoulCrystalItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SpiritItems {

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, Spirit.MODID);

    public static final Supplier<Item> SOUL_CRYSTAL = ITEMS.register("soul_crystal", () -> new SoulCrystalItem(new Item.Properties()));
    public static final Supplier<Item> MOB_CRYSTAL = ITEMS.register("mob_crystal", () -> new MobCrystalItem(new Item.Properties()));
    public static final Supplier<Item> SOUL_CAGE = ITEMS.register("soul_cage", () -> new BlockItem(SpiritBlocks.SOUL_CAGE.get(), new Item.Properties()));
    public static final Supplier<Item> PEDESTAL = ITEMS.register("pedestal", () -> new BlockItem(SpiritBlocks.PEDESTAL.get(), new Item.Properties()));
    public static final Supplier<Item> SUMMONING_PEDESTAL = ITEMS.register("summoning_pedestal", () -> new BlockItem(SpiritBlocks.SUMMONING_PEDESTAL.get(), new Item.Properties()));
    public static final Supplier<Item> TRANSMUTATION_BASIN = ITEMS.register("transmutation_basin", () -> new BlockItem(SpiritBlocks.TRANSMUTATION_BASIN.get(), new Item.Properties()));
}
