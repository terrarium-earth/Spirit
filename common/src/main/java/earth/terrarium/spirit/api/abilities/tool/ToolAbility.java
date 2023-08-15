package earth.terrarium.spirit.api.abilities.tool;

import com.google.common.collect.Multimap;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.UUID;

public interface ToolAbility {
    default boolean onHit(DamageSource source, LivingEntity victim, float amount) { return true; }
    default void onKill(Player player, ItemStack stack, LivingEntity target) {}
    default void onBlockBreak(ItemStack stack, Level level, BlockState state, BlockPos pos, Player player) {}
    default void modifyAttributes(ItemStack stack, Multimap<Attribute, AttributeModifier> attributes) {}
    default void modifyDrops(Player player, List<ItemStack> drops) {}
    default void whileHeld(Player player, ItemStack stack) {}

    ColorPalette getColor();

    default String getOrCreateNameId() {
        return Util.makeDescriptionId("tool_ability", ResourceLocation.tryParse(ToolAbilityManager.getName(this)));
    }

    default String getNameId() {
        return this.getOrCreateNameId();
    }

    default String getDescriptionId() {
        return this.getOrCreateNameId() + ".desc";
    }
}
