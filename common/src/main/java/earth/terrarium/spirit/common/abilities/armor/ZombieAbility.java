package earth.terrarium.spirit.common.abilities.armor;

import com.google.common.collect.Multimap;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ZombieAbility extends ArmorAbility {
    @Override
    public void modifyAttributes(UUID uuid, EquipmentSlot slot, ItemStack stack, Multimap<Attribute, AttributeModifier> attributes) {
        attributes.put(Attributes.MAX_HEALTH, new AttributeModifier(uuid, "Zombie Ability Modifier", 4, AttributeModifier.Operation.ADDITION));
    }

    @Override
    public ColorPalette getColor() {
        return new ColorPalette(0xFFff7a16, 0xFFdb4a12, 0xFFffc22e);
    }
}
