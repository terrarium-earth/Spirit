package earth.terrarium.spirit.common.abilities.armor;

import com.google.common.collect.Multimap;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class BuiltDifferentAbility implements ArmorAbility {
    public static final UUID HEALTH_MODIFIER_UUID = UUID.nameUUIDFromBytes("Built Different".getBytes());
    @Override
    public void modifyAttributes(UUID uuid, EquipmentSlot slot, ItemStack stack, Multimap<Attribute, AttributeModifier> attributes) {
        attributes.put(Attributes.MAX_HEALTH, new AttributeModifier(HEALTH_MODIFIER_UUID, "Armor modifier", 10, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFBDBBA7, 0xFA6A77C, 0XffD6D5CD);
    }
}
