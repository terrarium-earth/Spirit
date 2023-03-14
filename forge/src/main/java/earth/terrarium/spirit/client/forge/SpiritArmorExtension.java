package earth.terrarium.spirit.client.forge;

import earth.terrarium.spirit.client.renderer.armor.ArmorRenderers;
import earth.terrarium.spirit.client.renderer.armor.forge.ArmorRenderersImpl;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

public class SpiritArmorExtension implements IClientItemExtensions {

    private ArmorRenderers.ArmorModelSupplier renderer;

    @SuppressWarnings("unchecked")
    private static <T extends LivingEntity> void uncheckedCopyTo(HumanoidModel<T> from, HumanoidModel<?> to) {
        from.copyPropertiesTo((HumanoidModel<T>) to);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        if (renderer == null) {
            renderer = ArmorRenderersImpl.getArmourRenderer(itemStack.getItem());
        }
        if (renderer != null) {
            return renderer.getArmorModel(livingEntity, itemStack, equipmentSlot, (HumanoidModel<LivingEntity>) original);
        }
        return original;
    }

    @Override
    public @NotNull Model getGenericArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
        HumanoidModel<?> replacement = this.getHumanoidArmorModel(livingEntity, itemStack, equipmentSlot, original);
        if (replacement != original) {
            uncheckedCopyTo(original, replacement);
            return replacement;
        } else {
            return original;
        }
    }
}
