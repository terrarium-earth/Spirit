package earth.terrarium.spirit.api.souls;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface Atunable {
    void setAttunement(ItemStack stack, EntityType<?> type, boolean critical);

    void clearAttunement(ItemStack stack);

    boolean canAttune(ItemStack stack, EntityType<?> type, boolean critical);

    boolean isAttunedFor(ItemStack stack, EntityType<?> type);

    boolean isCritical(ItemStack stack);
}
