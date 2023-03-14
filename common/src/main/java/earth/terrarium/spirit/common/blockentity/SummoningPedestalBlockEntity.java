package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.common.recipes.SummoningRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

public class SummoningPedestalBlockEntity extends AbstractPedestalBlockEntity<SummoningRecipe> {
    public SummoningPedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritBlockEntities.SUMMONING_PEDESTAL.get(), blockPos, blockState, SpiritRecipes.SUMMONING.get());
    }

    @Override
    public void finishRecipe() {
        if (level == null || this.containedRecipe == null) return;
        Entity entity = this.containedRecipe.result().create(level);
        if (entity != null) {
            entity.setPos(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.75, getBlockPos().getZ() + 0.5);
            if (this.containedRecipe.outputNbt().isPresent()) entity.load(this.containedRecipe.outputNbt().get());
            level.addFreshEntity(entity);
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.SOUL, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
            }
        }
    }
}
