package earth.terrarium.spirit.common.item;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.storage.AutoAbsorbing;
import earth.terrarium.spirit.api.storage.ItemStackContainer;
import earth.terrarium.spirit.api.storage.SoulContainingItem;
import earth.terrarium.spirit.api.storage.Tierable;
import earth.terrarium.spirit.api.souls.SoulData;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.api.utils.EntityRarity;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.api.utils.SoulUtils;
import earth.terrarium.spirit.api.utils.TooltipUtils;
import earth.terrarium.spirit.common.config.items.CrystalConfig;
import earth.terrarium.spirit.common.containers.SingleTypeContainer;
import earth.terrarium.spirit.common.util.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SoulCrystalItem extends Item implements SoulContainingItem, AutoAbsorbing, Tierable {
    public SoulCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    public SoulContainer getContainer(ItemStack object) {
        return new ItemStackContainer(object, new SingleTypeContainer(CrystalConfig.soulCrystalCap));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        SoulStack container = getContainer(itemStack).getSoulStack(0);
        Component component;
        if (container.isEmpty() || container.getEntity() == null) {
            component = Component.translatable("item.spirit.soul_crystal.none");
        } else {
            component = Component.translatable("item.spirit.soul_crystal.entity_component", container.getAmount(), container.getEntity().getDescription()).withStyle(EntityRarity.getRarity(container.getEntity()).color);
        }
        list.add(Component.translatable("item.spirit.soul_crystal.tooltip", component).withStyle(ChatFormatting.GRAY));
        if (!container.isEmpty()) {
            List<Component> nonShift = new ArrayList<>();
            TooltipUtils.getNormalTooltips().forEach(tooltip -> tooltip.addTooltip(nonShift, itemStack, level));
            ClientUtils.shiftTooltip(list, shiftToolTipComponents(itemStack, level), nonShift);
        } else {
            list.add(Component.translatable("item.spirit.soul_crystal.info_empty").withStyle(ChatFormatting.GRAY));
        }
    }

    public static List<Component> shiftToolTipComponents(@NotNull ItemStack itemStack, @Nullable Level level) {
        List<Component> list = new ArrayList<>();
        if(!(itemStack.getItem() instanceof Tierable tierable)) return List.of();
        SoulData tier = tierable.getSoulData(itemStack);
        Component display = Component.translatable("misc.spirit." + (tier == null ? "untiered" : "tiered")).withStyle(ChatFormatting.GRAY);
        list.add(Component.translatable("misc.spirit.tier", display).withStyle(ChatFormatting.GRAY));
        if (tier == null) {
            list.add(Component.translatable("misc.spirit.not_viable").withStyle(ChatFormatting.RED));
        }
        if(SoulUtils.isAllowed(itemStack, Spirit.BLACKLISTED_TAG)) {
            list.add(Component.translatable("misc.spirit.soul_cage_compatible").withStyle(ChatFormatting.GREEN));
        } else {
            list.add(Component.translatable("misc.spirit.soul_cage_incompatible").withStyle(ChatFormatting.RED));
        }
        TooltipUtils.getShiftTooltips().forEach(tooltip -> tooltip.addTooltip(list, itemStack, level));
        return list;
    }

    @Override
    public int getPriority() {
        return 5;
    }

    @Override
    public SoulData getSoulData(ItemStack stack) {
        int amount = getContainer(stack).getSoulStack(0).getAmount();
        return amount > CrystalConfig.minRequiredAmount ? CrystalConfig.defaultData : null;
    }


    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return Spirit.SOUL_COLOR;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        return (int) (getPercentage(itemStack) * 13);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        return ClientUtils.isItemInHand(itemStack);
    }

    public double getPercentage(ItemStack itemStack) {
        double percentage = ((double) getContainer(itemStack).getSoulStack(0).getAmount() / (CrystalConfig.soulCrystalCap));
        return Math.min(percentage, 1);
    }
}
