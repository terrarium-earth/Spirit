package earth.terrarium.spirit.common.item.armor;

import com.google.common.collect.Multimap;
import dev.architectury.injectables.annotations.PlatformOnly;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.armor_abilities.ArmorAbility;
import earth.terrarium.spirit.api.armor_abilities.ArmorAbilityManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SoulSteelArmor extends ArmorItem implements SpiritArmorItem {
    public static final String ABILITY_KEY = "Ability";

    public SoulSteelArmor(ArmorMaterial armorMaterial, Type equipmentSlot, Properties properties) {
        super(armorMaterial, equipmentSlot, properties);
    }

    @PlatformOnly("fabric")
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(ItemStack stack, EquipmentSlot slot) {
        return abilityAttributes(slot, stack);
    }

    @PlatformOnly("forge")
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return abilityAttributes(slot, stack);
    }

    public Multimap<Attribute, AttributeModifier> abilityAttributes(EquipmentSlot slot, ItemStack stack) {
        var base = stack.getItem().getDefaultAttributeModifiers(slot);
        var ability = getAbility(stack);
        if(ability != null) {
            ability.modifyAttributes(slot, stack, base);
        }
        return base;
    }

    public ArmorAbility getAbility(ItemStack stack) {
        if(stack.getTag() == null) return null;
        return stack.getTag().contains(ABILITY_KEY) ? ArmorAbilityManager.getAbilityRegistry().get(new ResourceLocation(stack.getOrCreateTag().getString(ABILITY_KEY))) : null;
    }

    @PlatformOnly("forge")
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        abilityTick(stack, level, player);
    }

    @PlatformOnly("fabric")
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        super.inventoryTick(itemStack, level, entity, i, bl);
        entity.getArmorSlots().iterator().forEachRemaining(stack -> {
            if(stack == itemStack && entity instanceof Player player) {
                var ability = ((SoulSteelArmor) stack.getItem()).getAbility(stack);
                if(ability != null) {
                    ability.onArmorTick(stack, level, player);
                }
            }
        });
    }

    public void abilityTick(ItemStack stack, Level level, Player player) {
        var ability = getAbility(stack);
        if(ability != null) {
            ability.onArmorTick(stack, level, player);
        }
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        ArmorAbility ability = getAbility(itemStack);
        return ability == null ? super.getBarColor(itemStack) : ability.getColor().primary();
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new ResourceLocation(Spirit.MODID, "textures/entity/armor/soul_steel_armor/soul_steel_armor" + (getAbility(stack) != null ? "_empty" : "") + ".png").toString();
    }

    @Override @Nullable
    public ResourceLocation getUnderlayTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return getAbility(stack) != null ? new ResourceLocation(Spirit.MODID, "textures/entity/armor/soul_steel_armor/soul_steel_armor_layer") : null;
    }
}
