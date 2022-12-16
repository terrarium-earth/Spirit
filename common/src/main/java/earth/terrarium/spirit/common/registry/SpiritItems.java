package earth.terrarium.spirit.common.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.common.item.CrudeSoulCrystalItem;
import earth.terrarium.spirit.common.item.MobCrystalItem;
import earth.terrarium.spirit.common.item.SoulCrystalItem;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public class SpiritItems {

    public static final Supplier<Item> SOUL_CRYSTAL = registerItem("soul_crystal", () -> new SoulCrystalItem(new Item.Properties()));
    public static final Supplier<Item> CRUDE_SOUL_CRYSTAL = registerItem("crude_soul_crystal", () -> new CrudeSoulCrystalItem(new Item.Properties()));
    public static final Supplier<Item> MOB_CRYSTAL = registerItem("mob_crystal", () -> new MobCrystalItem(new Item.Properties()));

    @ExpectPlatform
    public static <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        throw new NotImplementedException();
    }

    public static void init() {
    }
}
