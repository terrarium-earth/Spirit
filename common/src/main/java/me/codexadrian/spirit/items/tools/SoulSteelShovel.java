package me.codexadrian.spirit.items.tools;

import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.items.SoulMetalMaterial;
import me.codexadrian.spirit.utils.ToolUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SoulSteelShovel extends ShovelItem {
    public SoulSteelShovel(Properties properties) {
        super(SoulMetalMaterial.INSTANCE, 3, -2.4F, properties);
    }


    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        boolean mineBlock = super.mineBlock(itemStack, level, blockState, blockPos, livingEntity);
        if (mineBlock && livingEntity instanceof Player player) {
            ToolUtils.handleBreakBlock(player, ToolType.SHOVEL, itemStack, blockState, level, blockPos);
        }
        return mineBlock;
    }

    @Override
    public InteractionResult useOn(@NotNull UseOnContext useOnContext) {
        return ToolUtils.handleOnHitBlock(super.useOn(useOnContext), ToolType.SHOVEL, useOnContext.getPlayer(), useOnContext.getItemInHand(), useOnContext.getLevel(), useOnContext.getClickedPos());
    }
}
