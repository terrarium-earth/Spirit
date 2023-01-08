package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.blockentity.PedestalBlockEntity;
import earth.terrarium.spirit.common.blockentity.SoulCageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public class SpiritBlockEntities {

    public static final ResourcefulRegistry<BlockEntityType<?>> BLOCK_ENTITIES = ResourcefulRegistries.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Spirit.MODID);

    public static final Supplier<BlockEntityType<SoulCageBlockEntity>> SOUL_CAGE = BLOCK_ENTITIES.register("soul_cage", () -> create(SoulCageBlockEntity::new, SpiritBlocks.SOUL_CAGE.get()).build(null));

    public static final Supplier<BlockEntityType<PedestalBlockEntity>> PEDESTAL = BLOCK_ENTITIES.register("pedestal", () -> create(PedestalBlockEntity::new, SpiritBlocks.PEDESTAL.get()).build(null));


    @ExpectPlatform
    public static <T extends BlockEntity>BlockEntityType.Builder<T> create(BlockEntitySupplier<T> supplier, Block... blocks) {
        throw new NotImplementedException();
    }

    @FunctionalInterface
    public interface BlockEntitySupplier<T extends BlockEntity> {
        T create(BlockPos blockPos, BlockState blockState);
    }
}
