package earth.terrarium.spirit.common.item.armor;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.architectury.injectables.annotations.PlatformOnly;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.common.util.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SoulSteelArmor extends ArmorItem implements SpiritArmorItem {
    public static final String ABILITY_KEY = "Ability";

    public SoulSteelArmor(ArmorMaterial armorMaterial, Type equipmentSlot, Properties properties) {
        super(armorMaterial, equipmentSlot, properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        var ability = getAbility(itemStack);
        if(ability != null) {
            list.add(Component.translatable("misc.spirit.armor_ability_prefix").withStyle(Style.EMPTY.withColor(ability.getColor().primary())).append(Component.translatable(ability.getNameId()).withStyle(ChatFormatting.GRAY)));
            var contents = Component.translatable(ability.getDescriptionId()).getString();
            List<MutableComponent> components = Arrays.stream(contents.split("<br>"))
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .map(Component::literal)
                    .map(mutableComponent -> mutableComponent.withStyle(ChatFormatting.GRAY))
                    .toList();
            ClientUtils.shiftTooltip(list, components, List.of());
        }
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
        Multimap<Attribute, AttributeModifier> base = HashMultimap.create(super.getDefaultAttributeModifiers(slot));
        var ability = getAbility(stack);
        if(ability != null && slot == this.type.getSlot()) {
            ability.modifyAttributes(ARMOR_MODIFIER_UUID_PER_TYPE.get(getType()), slot, stack, base);
        }
        return base;
    }

    @Nullable
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
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return new ResourceLocation(Spirit.MODID, "textures/entity/armor/soul_steel_armor/soul_steel_armor" + (getAbility(stack) != null ? "_empty" : "") + ".png").toString();
    }

    @Override @Nullable
    public ResourceLocation getUnderlayTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return getAbility(stack) != null ? new ResourceLocation(Spirit.MODID, "textures/entity/armor/soul_steel_armor/soul_steel_armor_layer") : null;
    }
}
