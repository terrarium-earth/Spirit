package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.elements.SoulElement;
import earth.terrarium.spirit.common.entity.SoulReceptacle;
import earth.terrarium.spirit.common.recipes.PedestalRecipe;
import earth.terrarium.spirit.common.registry.SpiritEntities;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import earth.terrarium.spirit.common.util.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class RagingSoulFireBlock extends SoulFireBlock {

    public RagingSoulFireBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(24) == 0) {
            level.playLocalSound((double) blockPos.getX() + 0.5, (double) blockPos.getY() + 0.5, (double) blockPos.getZ() + 0.5, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + randomSource.nextFloat(), randomSource.nextFloat() * 0.7F + 0.3F, false);
        }

        //add a colored particle
        for (int i = 0; i < 2; ++i) {
            Supplier<Double> d = () -> (double) blockPos.getX() + randomSource.nextDouble();
            Supplier<Double> e = () -> (double) blockPos.getY() + randomSource.nextDouble();
            Supplier<Double> f = () -> (double) blockPos.getZ() + randomSource.nextDouble();
            level.addParticle(ParticleTypes.SOUL, d.get(), e.get(), f.get(), 0.0, 0.03, 0.0);
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d.get(), e.get(), f.get(), 0.0, 0.03, 0.0);
            level.addParticle(new DustParticleOptions(Vec3.fromRGB24(Spirit.SOUL_COLOR).toVector3f(), 1f), d.get(), e.get(), f.get(), 0.0, 0.03, 0.0);
        }
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if(!(level instanceof ServerLevel serverLevel)) return;
        if (entity instanceof ItemEntity itemE) {
            var recipes = PedestalRecipe.getRecipesForEntity(SpiritRecipes.TRANSMUTATION.get(), itemE.getItem(), level.getRecipeManager());
            if (!recipes.isEmpty()) {
                var items = RecipeUtils.getPedestalItems(blockPos, level);
                var souls = RecipeUtils.getPedestalSouls(blockPos, level);
                for (var recipe : recipes) {
                    if (RecipeUtils.validatePedestals(level, recipe, items, souls, true)) {
                        itemE.getItem().shrink(1);
                        if (recipe.duration() > 0) {
                            SoulReceptacle soulReceptacle = SpiritEntities.SOUL_RECEPTACLE.get().create(level);
                            if (soulReceptacle != null) {
                                soulReceptacle.setResult(recipe.result().copy(), recipe.duration());
                                soulReceptacle.setPos(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
                                level.addFreshEntity(soulReceptacle);
                            }
                        } else {
                            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 20, 0.5, 0.5, 0.5, 0.1);
                            serverLevel.sendParticles(ParticleTypes.SOUL, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 20, 0.5, 0.5, 0.5, 0.1);
                            serverLevel.addFreshEntity(new ItemEntity(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, recipe.result().copy()));
                        }
                        if (recipe.consumesFlame()) {
                            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                        }
                        break;
                    }
                }
            }
        } else if (entity instanceof LivingEntity livingEntity) {
            if (!entity.fireImmune()) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 40, 0));
                livingEntity.hurt(livingEntity.damageSources().inFire(), 3.0F);
            }
        }
    }
}
