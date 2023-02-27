package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.common.blockentity.TransmutationPedestalBlockEntity;
import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TransmutationPedestalBlock extends BasePedestalBlock<TransmutationRecipe> {
    public TransmutationPedestalBlock(Properties properties) {
        super(SpiritRecipes.TRANSMUTATION.get(), properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TransmutationPedestalBlockEntity(blockPos, blockState);
    }
}
