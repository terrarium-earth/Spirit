package earth.terrarium.spirit.common.abilities.armor;

import com.google.common.collect.Multimap;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BuiltDifferentAbility implements ArmorAbility {
    @Override
    public void modifyAttributes(UUID uuid, EquipmentSlot slot, ItemStack stack, Multimap<Attribute, AttributeModifier> attributes) {
        ArmorAbility.super.modifyAttributes(uuid, slot, stack, attributes);
    }

    @Override
    public ColorPalette getColor() {
        return null;
    }
}
