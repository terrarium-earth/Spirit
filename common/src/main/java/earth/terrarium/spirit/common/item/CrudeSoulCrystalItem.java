package earth.terrarium.spirit.common.item;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.storage.AutoAbsorbing;
import earth.terrarium.spirit.api.storage.ItemStackContainer;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.config.items.CrystalConfig;
import earth.terrarium.spirit.common.containers.NonTypedContainer;
import earth.terrarium.spirit.common.util.ClientUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrudeSoulCrystalItem extends Item implements SoulContainingObject.Item, AutoAbsorbing {
    public CrudeSoulCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public @NotNull SoulContainer getContainer(ItemStack object) {
        return new ItemStackContainer(object, new NonTypedContainer(1024));
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return Spirit.SOUL_COLOR;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack itemStack) {
        SoulStack soulStack = getContainer(itemStack).getSoulStack(0);
        return (int) (soulStack.getAmount() / (double) CrystalConfig.soulCrystalCap * 13);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack itemStack) {
        return ClientUtils.isItemInHand(itemStack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        SoulContainer container = getContainer(itemStack);
        SoulStack soulStack = container.getSoulStack(0);
        int souls = soulStack.getAmount();
        MutableComponent tooltip = Component.translatable("spirit.item.crude_soul_crystal.tooltip").withStyle(ChatFormatting.AQUA);
        if (souls != 0) {
            tooltip.append(Component.literal(souls + "/" + container.maxCapacity()).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.append(Component.translatable("spirit.item.crystal.tooltip_empty").withStyle(ChatFormatting.GRAY));
        }
        list.add(tooltip);
    }
}
