package earth.terrarium.spirit.api.storage;

import earth.terrarium.spirit.api.souls.SoulData;
import net.minecraft.world.item.ItemStack;

public interface Tierable {
    SoulData getSoulData(ItemStack stack);
}
