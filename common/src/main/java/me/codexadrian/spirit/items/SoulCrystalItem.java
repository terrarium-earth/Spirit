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
import org.jetbrains.annotations.NotNull;
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
                tooltip.append(Component.literal("(" + Math.min(storedEntity.getInt("Souls"), SoulUtils.getMaxSouls(itemStack)) + "/" + Math.min(SoulUtils.getNextTier(itemStack) == null ? Integer.MAX_VALUE : SoulUtils.getNextTier(itemStack).getRequiredSouls(), SoulUtils.getMaxSouls(itemStack)) + ") "));

                list.add(tooltip.withStyle(ChatFormatting.GRAY));
            }
        } else {
            MutableComponent unboundTooltip = Component.translatable("tooltip.spirit.soul_crystal.unbound");
            list.add(unboundTooltip.withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public static double getPercentage(ItemStack itemStack) {
        Tier tier = SoulUtils.getNextTier(itemStack);
        if (tier == null) {
            return 1;
        }

        double percentage = ((double) SoulUtils.getSoulsInCrystal(itemStack) / (tier.getRequiredSouls()));

        return SoulUtils.isMaxTier(itemStack) ? 1 : percentage;
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return 0x00fffb;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        return !SoulUtils.isMaxTier(itemStack) && itemStack.hasTag() && itemStack.getTag().contains("StoredEntity");
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        return (int) (getPercentage(itemStack) * 13);
    }
}
