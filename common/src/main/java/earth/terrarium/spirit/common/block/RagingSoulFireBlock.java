package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.elements.SoulElement;
import earth.terrarium.spirit.common.recipes.SoulEngulfingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class RagingSoulFireBlock extends SoulFireBlock {
    private final SoulElement element;

    public RagingSoulFireBlock(SoulElement element, Properties properties) {
        super(properties);
        this.element = element;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(24) == 0) {
            level.playLocalSound((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.5, (double) blockPos.getZ() + 0.5, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + randomSource.nextFloat(), randomSource.nextFloat() * 0.7F + 0.3F, false);
        }

        //add a colored particle
        for (int i = 0; i < 2; ++i) {
            double d = (double) blockPos.getX() + randomSource.nextDouble();
            double e = (double) blockPos.getY() + randomSource.nextDouble();
            double f = (double) blockPos.getZ() + randomSource.nextDouble();
            double r = (float) (element.getColor() >> 16 & 0xFF) / 255.0f;
            double g = (float) (element.getColor() >> 8 & 0xFF) / 255.0f;
            double b = (float) (element.getColor() & 0xFF) / 255.0f;
            level.addParticle(ParticleTypes.LARGE_SMOKE, d, e, f, 0.0, 0.0, 0.0);
            level.addParticle(new DustParticleOptions(Vec3.fromRGB24(element.getColor()).toVector3f(), 1f), d, e, f, 0.0, 0.0, 0.0);
            level.addParticle(ParticleTypes.EFFECT, d, e, f, r, g, b);
        }
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof ItemEntity itemE && level instanceof ServerLevel serverLevel) {
            for (var recipe : SoulEngulfingRecipe.getRecipesForStack(itemE.getItem(), level.getRecipeManager())) {
                if (recipe.validateRecipe(blockPos, itemE, serverLevel)) {
                    return;
                }
            }

            if (itemE.getItem().is(Spirit.SOUL_FIRE_IMMUNE)) {
                itemE.setInvulnerable(true);
            }

            if (itemE.getItem().is(Spirit.SOUL_FIRE_REPAIRABLE)) {
                itemE.setInvulnerable(true);
                ItemStack tool = itemE.getItem();
                if (tool.isDamaged() && level.random.nextBoolean()) {
                    tool.setDamageValue(tool.getDamageValue() - 1);
                    serverLevel.sendParticles(ParticleTypes.SOUL, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 5, 0.5, 0.75, 0.5, 0);
                }
            }
        }
        super.entityInside(blockState, level, blockPos, entity);
    }


}
