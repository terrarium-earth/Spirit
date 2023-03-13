package earth.terrarium.spirit.common.item.armor;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface SpiritArmorItem {
    ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type);

    @Nullable
    ResourceLocation getUnderlayTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type);
}
