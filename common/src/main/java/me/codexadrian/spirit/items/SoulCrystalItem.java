package me.codexadrian.spirit.items;

import me.codexadrian.spirit.recipe.Tier;
import me.codexadrian.spirit.utils.ClientUtils;
import me.codexadrian.spirit.utils.SoulUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
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
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (itemStack.getTag() != null && level != null) {
            final CompoundTag storedEntity = itemStack.getTag().getCompound("StoredEntity");
            if (storedEntity.contains("Type")) {
                MutableComponent tooltip = Component.translatable(Util.makeDescriptionId("entity", new ResourceLocation(storedEntity.getString("Type"))));
                tooltip.append(Component.literal(" ").append(Component.translatable(SoulUtils.getTierDisplay(itemStack, level)).append(" - ")));
                Tier nextTier = SoulUtils.getNextTier(itemStack, level);
                int maxSouls = SoulUtils.getMaxSouls(itemStack, level);
                tooltip.append(Component.literal("(" + Math.min(storedEntity.getInt("Souls"), maxSouls) + "/" + Math.min(nextTier == null ? maxSouls : nextTier.requiredSouls(), maxSouls) + ") "));

                list.add(tooltip.withStyle(ChatFormatting.GRAY));
            }
        } else {
            MutableComponent unboundTooltip = Component.translatable("tooltip.spirit.soul_crystal.unbound");
            list.add(unboundTooltip.withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public static double getPercentage(ItemStack itemStack, Level level) {
        Tier tier = SoulUtils.getNextTier(itemStack, level);
        if (tier == null) {
            tier = SoulUtils.getTier(itemStack, level);
            if(tier == null) {
                return 0;
            }
        }

        double percentage = ((double) SoulUtils.getSoulsInCrystal(itemStack) / (tier.requiredSouls()));

        return Math.min(percentage, 1);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return 0x00fffb;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return ClientUtils.isItemInHand(stack);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        ClientLevel level = Minecraft.getInstance().level;
        if(level != null) return (int) (getPercentage(itemStack, level) * 13);
        return 0;
    }
}
