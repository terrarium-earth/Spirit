package earth.terrarium.spirit.common.registry.fabric;

import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SpiritBlockEntitiesImpl {

    public static <T extends BlockEntity>BlockEntityType.Builder<T> create(SpiritBlockEntities.BlockEntitySupplier<T> supplier, Block... blocks) {
        return BlockEntityType.Builder.of(supplier::create, blocks);
    }
}
