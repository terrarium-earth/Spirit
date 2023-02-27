package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.common.recipes.TransmutationRecipe;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TransmutationPedestalBlockEntity extends AbstractPedestalBlockEntity<TransmutationRecipe> {
    public TransmutationPedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritRecipes.TRANSMUTATION.get(), blockPos, blockState);
    }

    @Override
    public void finishRecipe() {
        if(level == null || this.containedRecipe == null) return;
        Entity entity = new ItemEntity(level, getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.75, getBlockPos().getZ() + 0.5, this.containedRecipe.result().copy());
        level.addFreshEntity(entity);
        for (int i = 0; i < 10; i++) {
            level.addParticle(ParticleTypes.SOUL, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
        }
    }
}
