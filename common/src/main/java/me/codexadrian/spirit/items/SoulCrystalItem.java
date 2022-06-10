package me.codexadrian.spirit.items;

import me.codexadrian.spirit.Tier;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulCrystalItem extends Item {

    public SoulCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (itemStack.hasTag()) {
            final CompoundTag storedEntity = itemStack.getTag().getCompound("StoredEntity");
            if (storedEntity.contains("Type")) {
                MutableComponent tooltip = Component.translatable(Util.makeDescriptionId("entity", new ResourceLocation(storedEntity.getString("Type"))));
                tooltip.append(Component.literal(" " + (SoulUtils.getTierIndex(itemStack) + 1) + " - "));
                if (!Screen.hasShiftDown()) {
                    tooltip.append(Component.literal("(" + getPercentage(itemStack) + "%) "));
                } else {
                    tooltip.append(Component.literal("(" + Math.min(storedEntity.getInt("Souls"), SoulUtils.getMaxSouls(itemStack)) + "/" + Math.min(SoulUtils.getNextTier(itemStack) == null ? Integer.MAX_VALUE : SoulUtils.getNextTier(itemStack).getRequiredSouls(), SoulUtils.getMaxSouls(itemStack)) + ") "));
                }

                list.add(tooltip.withStyle(ChatFormatting.GRAY));
            }
        } else {
            MutableComponent unboundTooltip = Component.translatable("tooltip.spirit.soul_crystal.unbound");
            list.add(unboundTooltip.withStyle(ChatFormatting.BOLD, ChatFormatting.RED));
        }
    }

    public static double getPercentage(ItemStack itemStack) {
        int storedSouls = itemStack.getTag().getCompound("StoredEntity").getInt("Souls");
        Tier tier = SoulUtils.getNextTier(itemStack);
        if (tier == null) {
            return 100;
        }

        double percentage = ((double) storedSouls / (tier.getRequiredSouls())) * 100;
        double p = percentage * 10;
        int p2 = (int) p;

        return SoulUtils.isMaxTier(itemStack) ? 100 : (double) p2 / 10;
    }
}
