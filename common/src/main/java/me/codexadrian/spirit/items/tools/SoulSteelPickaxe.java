package me.codexadrian.spirit.items.tools;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.items.SoulMetalMaterial;
import me.codexadrian.spirit.utils.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SoulSteelPickaxe extends PickaxeItem {
    public SoulSteelPickaxe(Properties properties) {
        super(SoulMetalMaterial.INSTANCE, 1, -2.8F, properties);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return Spirit.SOUL_COLOR;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        boolean mineBlock = super.mineBlock(itemStack, level, blockState, blockPos, livingEntity);
        if (mineBlock && livingEntity instanceof Player player) {
            ToolUtils.handleBreakBlock(player, ToolType.PICKAXE, itemStack, blockState, level, blockPos);
        }
        return mineBlock;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        ToolUtils.appendEmpoweredText(itemStack, list);
        super.appendHoverText(itemStack, level, list, tooltipFlag);
    }
}
