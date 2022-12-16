package earth.terrarium.spirit.common.registry.fabric;

import earth.terrarium.spirit.Spirit;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class SpiritItemsImpl {
    public static <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        var supplier = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Spirit.MODID, id), item.get());
        return () -> supplier;
    }
}
