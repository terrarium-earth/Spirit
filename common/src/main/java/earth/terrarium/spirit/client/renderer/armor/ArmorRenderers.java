package earth.terrarium.spirit.client.renderer.armor;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.NotImplementedException;

public class ArmorRenderers {

    public static void init() {
        ArmorRenderers.registerArmour((entity, stack, slot, original) -> {
            EntityModelSet modelLoader = Minecraft.getInstance().getEntityModels();
            ModelPart layer = modelLoader.bakeLayer(SoulSteelArmorModel.LAYER_LOCATION);
            return new SoulSteelArmorModel(layer, original, entity, slot, stack);
        }, SpiritItems.SOUL_STEEL_HELMET.get(), SpiritItems.SOUL_STEEL_CHESTPLATE.get(), SpiritItems.SOUL_STEEL_LEGGINGS.get(), SpiritItems.SOUL_STEEL_BOOTS.get());
    }

    @ExpectPlatform
    public static void registerArmour(ArmorModelSupplier modelSupplier, Item... items) {
        throw new NotImplementedException();
    }

    @FunctionalInterface
    public interface ArmorModelSupplier {
        HumanoidModel<LivingEntity> getArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<LivingEntity> original);
    }
}
