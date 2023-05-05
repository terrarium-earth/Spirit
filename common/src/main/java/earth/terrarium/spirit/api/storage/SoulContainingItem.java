package earth.terrarium.spirit.api.storage;

import earth.terrarium.spirit.api.storage.container.SoulContainer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface SoulContainingItem {
    @Nullable
    SoulContainer getContainer(ItemStack stack);
}
