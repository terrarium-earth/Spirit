package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.item.CrudeSoulCrystalItem;
import earth.terrarium.spirit.common.item.MobCrystalItem;
import earth.terrarium.spirit.common.item.SoulCrystalItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public class SpiritItems {

    public static final ResourcefulRegistry<Item> ITEMS = ResourcefulRegistries.create(BuiltInRegistries.ITEM, Spirit.MODID);

    public static final Supplier<Item> SOUL_CRYSTAL = ITEMS.register("soul_crystal", () -> new SoulCrystalItem(new Item.Properties()));
    public static final Supplier<Item> CRUDE_SOUL_CRYSTAL = ITEMS.register("crude_soul_crystal", () -> new CrudeSoulCrystalItem(new Item.Properties()));
    public static final Supplier<Item> MOB_CRYSTAL = ITEMS.register("mob_crystal", () -> new MobCrystalItem(new Item.Properties()));
    public static final Supplier<Item> SOUL_CAGE = ITEMS.register("soul_cage", () -> new BlockItem(SpiritBlocks.SOUL_CAGE.get(), new Item.Properties()));
}
