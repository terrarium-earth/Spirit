package earth.terrarium.spirit.api.storage;

import earth.terrarium.spirit.api.storage.container.SoulContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public interface SoulContainingObject<T> {

    @Nullable
    SoulContainer getContainer(T object);

    interface Item extends SoulContainingObject<ItemStack> {}
    interface Block extends SoulContainingObject<BlockEntity> {}
}
