package me.codexadrian.spirit.items.tools;

import me.codexadrian.spirit.utils.ClientUtils;
import me.codexadrian.spirit.utils.ToolUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulSteelWand extends Item {
    public SoulSteelWand(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ClientUtils.shiftTooltip(list, List.of(Component.translatable("item.spirit.soul_steel_wand.desc").withStyle(ChatFormatting.GRAY)), List.of());
    }
}
