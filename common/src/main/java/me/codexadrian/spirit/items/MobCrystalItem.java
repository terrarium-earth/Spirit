package me.codexadrian.spirit.items;

import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MobCrystalItem extends Item {
    public MobCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (itemStack.getTag() != null && level != null) {
            if (itemStack.getTag().contains("EntityType")) {
                MutableComponent tooltip = Component.translatable("spirit.item.soul_crystal_shard.tooltip", Component.translatable(Util.makeDescriptionId("entity", ResourceLocation.tryParse(itemStack.getTag().getString("EntityType")))));
                list.add(tooltip.withStyle(ChatFormatting.GRAY));
            } else {
                MutableComponent unboundTooltip = Component.translatable("spirit.item.crude_soul_crystal.tooltip_empty");
                list.add(unboundTooltip.withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            MutableComponent unboundTooltip = Component.translatable("spirit.item.crude_soul_crystal.tooltip_empty");
            list.add(unboundTooltip.withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public static float mobCrystalType(ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity, int i) {
        if (itemStack.getTag() != null && itemStack.getTag().contains("EntityType")) {
            return 1.0F;
        }
        return 0.0F;
    }
}
