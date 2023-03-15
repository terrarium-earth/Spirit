package earth.terrarium.spirit.common.item.armor;

import earth.terrarium.spirit.Spirit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class SoulSteelArmor extends ArmorItem implements SpiritArmorItem {
    private boolean renderUnderlay = true;

    public SoulSteelArmor(ArmorMaterial armorMaterial, EquipmentSlot equipmentSlot, Properties properties) {
        super(armorMaterial, equipmentSlot, properties);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new ResourceLocation(Spirit.MODID, "textures/entity/armor/soul_steel_armor/soul_steel_armor" + (renderUnderlay ? "_empty" : "") + ".png").toString();
    }

    // TODO currently hardcoded to ender armor.
    @Override
    public ResourceLocation getUnderlayTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return renderUnderlay ? new ResourceLocation(Spirit.MODID, "textures/entity/armor/soul_steel_armor/soul_steel_armor_ender.png") : null;
    }
}
