package me.codexadrian.spirit.items;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.SpiritConfig;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.entity.EntityRarity;
import me.codexadrian.spirit.platform.fabric.Services;
import me.codexadrian.spirit.utils.ClientUtils;
import me.codexadrian.spirit.utils.SoulUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SoulCrystalItem extends Item {

    public SoulCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        Component entityOrNone = Optional.ofNullable(SoulUtils.getSoulCrystalType(itemStack))
                .flatMap(EntityType::byString)
                .map(entityType -> Component.translatable("item.spirit.soul_crystal.entity_component", SoulUtils.getSoulsInCrystal(itemStack), entityType.getDescription()).withStyle(EntityRarity.getRarity(entityType).color))
                .orElse(Component.translatable("item.spirit.soul_crystal.none"));
        list.add(Component.translatable("item.spirit.soul_crystal.tooltip", entityOrNone).withStyle(ChatFormatting.GRAY));
        if(SoulUtils.getSoulsInCrystal(itemStack) > 0) {
            ClientUtils.shiftTooltip(list, shiftToolTipComponents(itemStack, level), List.of());
        } else {
            list.add(Component.translatable("item.spirit.soul_crystal.info_empty").withStyle(ChatFormatting.GRAY));
        }
    }

    private static List<Component> shiftToolTipComponents(@NotNull ItemStack itemStack, @Nullable Level level) {
        List<Component> list = new ArrayList<>();
        if(level == null) return list;
        Tier tier = SoulUtils.getTier(itemStack, level);
        Component display = Component.translatable(tier == null ? SpiritConfig.getInitialTierName() : tier.displayName()).withStyle(ChatFormatting.GRAY);
        list.add(Component.translatable("misc.spirit.tier", display).withStyle(ChatFormatting.GRAY));
        if(tier == null && (SoulUtils.canCrystalBeUsedInCage(itemStack) || (Services.PLATFORM.isModLoaded("vitalize") && SoulUtils.isAllowed(itemStack, Spirit.REVITALIZER_TAG)))) {
            list.add(Component.translatable(Services.PLATFORM.isModLoaded("vitalize") ? "misc.spirit.not_viable_vitalize" : "misc.spirit.not_viable").withStyle(ChatFormatting.RED));
        }
        Tier nextTier = SoulUtils.getNextTier(itemStack, level);
        if(nextTier != null) {
            list.add(Component.translatable("misc.spirit.next_tier", nextTier.requiredSouls() - SoulUtils.getSoulsInCrystal(itemStack), Component.translatable(nextTier.displayName())).withStyle(ChatFormatting.GRAY));
        }
        if(SoulUtils.canCrystalBeUsedInCage(itemStack)) {
            list.add(Component.translatable("misc.spirit.soul_cage_compatible").withStyle(ChatFormatting.GREEN));
        } else {
            list.add(Component.translatable("misc.spirit.soul_cage_incompatible").withStyle(ChatFormatting.RED));
        }
        if(Services.PLATFORM.isModLoaded("vitalize")) {
            if(SoulUtils.isAllowed(itemStack, Spirit.REVITALIZER_TAG)) {
                list.add(Component.translatable("misc.vitalize.machine_compatible").withStyle(ChatFormatting.GREEN));
            } else {
                list.add(Component.translatable("misc.vitalize.machine_incompatible").withStyle(ChatFormatting.RED));
            }
        }
        return list;
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
        return Spirit.SOUL_COLOR;
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
