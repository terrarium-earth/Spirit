package earth.terrarium.spirit.api.armor_abilities;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ArmorAbility {
    default void modifyAttributes(EquipmentSlot slot, ItemStack stack, Multimap<Attribute, AttributeModifier> attributes) {}
    default void onArmorTick(ItemStack stack, Level level, Player player) {}
    ColorPalette getColor();
}
