package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.api.elements.SoulElement;
import earth.terrarium.spirit.common.recipes.PedestalRecipe;
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
        if(!(level instanceof ServerLevel serverLevel)) return;
        if (entity instanceof ItemEntity itemE) {
            var recipes = PedestalRecipe.getRecipesForEntity(SpiritRecipes.TRANSMUTATION.get(), itemE.getItem(), level.getRecipeManager());
            if (!recipes.isEmpty()) {
                var items = RecipeUtils.getPedestalItems(blockPos, level);
                var souls = RecipeUtils.getPedestalSouls(blockPos, level);
                for (var recipe : recipes) {
                    if (RecipeUtils.validatePedestals(level, recipe, items, souls, true)) {
                        itemE.getItem().shrink(1);
                        ItemEntity itemEntity = new ItemEntity(level, itemE.getX(), itemE.getY(), itemE.getZ(), recipe.result().copy());
                        level.addFreshEntity(itemEntity);
                        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
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
