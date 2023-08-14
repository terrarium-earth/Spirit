package earth.terrarium.spirit.common.item.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.api.abilities.tool.DataToolAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbilityManager;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface SoulSteelTool {
    String ABILITY_KEY = "Ability";
    String DATA_KEY = "Custom";

    @Nullable
    default ToolAbility getAbility(ItemStack stack) {
        if(stack.getTag() == null) return null;
        if (stack.getTag().contains(ABILITY_KEY)) {
            ToolAbility toolAbility = ToolAbilityManager.getAbility(stack.getOrCreateTag().getString(ABILITY_KEY));
            if (toolAbility instanceof DataToolAbility dataToolAbility) {
                dataToolAbility.deserialize(stack.getOrCreateTag().getCompound(DATA_KEY));
            }
            return toolAbility;
        }
        return null;
    }

    default boolean allowInfusion(ItemStack stack) {
        return !stack.getOrCreateTag().contains(SoulSteelArmor.ABILITY_KEY);
    }

    default void addAbility(ToolAbility ability, ItemStack stack) {
        if (allowInfusion(stack)) {
            stack.getOrCreateTag().putString(ABILITY_KEY, ToolAbilityManager.getName(ability));
            if (ability instanceof DataToolAbility dataToolAbility) {
                stack.getOrCreateTag().put(DATA_KEY, dataToolAbility.serialize(new CompoundTag()));
            }
        }
    }
}
