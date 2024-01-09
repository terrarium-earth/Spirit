package earth.terrarium.spirit.api.abilities.armor;

import com.google.common.collect.Multimap;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface ArmorAbility {
    default void modifyAttributes(UUID uuid, EquipmentSlot slot, ItemStack stack, Multimap<Attribute, AttributeModifier> attributes) {}
    default void onArmorTick(ItemStack stack, Level level, Player player) {}
    default void onEntityMove(ItemStack stack, LivingEntity livingEntity, Level level, BlockPos blockPos) {}
    default void onEat(ItemStack armor, ItemStack food, Level level, Player player) {}
    default void onEquip(Player player, EquipmentSlot slot, ItemStack stack) {}
    default void onUnequip(Player player, EquipmentSlot slot, ItemStack stack) {}
    default boolean onLand(Player player, EquipmentSlot slot, ItemStack stack, float distance) { return false; }
    default boolean overrideStackedOnOther(ItemStack itemStack, Slot slot, ClickAction clickAction, Player player) { return false; }

    ColorPalette getColor();

    default String getOrCreateNameId() {
        return Util.makeDescriptionId("armor_ability", ResourceLocation.tryParse(ArmorAbilityManager.getName(this)));
    }

    default String getNameId() {
        return this.getOrCreateNameId();
    }

    default String getDescriptionId() {
        return this.getOrCreateNameId() + ".desc";
    }
}
