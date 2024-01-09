package earth.terrarium.spirit.api.rituals.results.impl.fabric;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@SuppressWarnings("ALL")
public class ItemResultImpl {
    public static boolean hasContainerBelow(Level level, BlockPos blockPos) {
        return ItemStorage.SIDED.find(level, blockPos, level.getBlockState(blockPos), level.getBlockEntity(blockPos), Direction.UP) != null;
    }

    public static boolean insertItemIntoContainerBelow(Level level, BlockPos blockPos, ItemStack toInsert) {
        var storage = ItemStorage.SIDED.find(level, blockPos, level.getBlockState(blockPos), level.getBlockEntity(blockPos), Direction.UP);
        var itemVariant = ItemVariant.of(toInsert);
        try (Transaction txn = Transaction.openOuter()) {
            if (storage != null) {
                int inserted = (int) storage.insert(itemVariant, toInsert.getCount(), txn);

                if (inserted == toInsert.getCount()) {
                    txn.commit();
                    return true;
                } else {
                    txn.abort();
                    return false;
                }
            }
        }
        return false;
    }
}
