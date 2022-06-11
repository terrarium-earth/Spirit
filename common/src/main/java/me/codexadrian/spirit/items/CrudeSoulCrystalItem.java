package me.codexadrian.spirit.items;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
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

public class CrudeSoulCrystalItem extends Item {
    public CrudeSoulCrystalItem(Properties $$0) {
        super($$0);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        int souls = SoulUtils.getSoulsInCrystal(itemStack);
        if (souls != 0) {
            MutableComponent tooltip = Component.translatable("spirit.item.crude_soul_crystal.tooltip");
            tooltip.append(Component.literal(souls + "/" + Spirit.getSpiritConfig().getCrudeSoulCrystalCap()));
            list.add(tooltip.withStyle(ChatFormatting.GRAY));
        } else {
            list.add(Component.translatable("spirit.item.crude_soul_crystal.tooltip_empty").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return 0x00fffb;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        return (int) (SoulUtils.getSoulsInCrystal(itemStack) / (double) Spirit.getSpiritConfig().getCrudeSoulCrystalCap() * 13);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        return SoulUtils.getSoulsInCrystal(itemStack) < Spirit.getSpiritConfig().getCrudeSoulCrystalCap();
    }
}
