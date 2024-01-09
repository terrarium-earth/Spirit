package earth.terrarium.spirit.api.rituals.results.impl.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Optional;

public class ItemResultImpl {
    public static boolean insertItemIntoContainerBelow(Level level, BlockPos blockPos, ItemStack itemStack) {
        LazyOptional<IItemHandler> itemHandler = getItemHandler(level, blockPos);
        if (itemHandler.isPresent()) {
            IItemHandler iItemHandler = itemHandler.orElseThrow(() -> new RuntimeException("Item Handler is not present"));
            ItemStack stack = ItemHandlerHelper.insertItem(iItemHandler, itemStack, true);
            if (stack.isEmpty()) {
                ItemHandlerHelper.insertItem(iItemHandler, itemStack, false);
                return true;
            }
        }
        return false;
    }

    public static boolean hasContainerBelow(Level level, BlockPos blockPos) {
        return getItemHandler(level, blockPos).isPresent();
    }

    private static LazyOptional<IItemHandler> getItemHandler(Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        return blockEntity != null
                ? blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP)
                : LazyOptional.empty();
    }
}
