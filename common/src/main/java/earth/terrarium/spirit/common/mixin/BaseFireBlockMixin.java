package earth.terrarium.spirit.common.mixin;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.recipes.SoulEngulfingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin {

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void onBurn(BlockState blockState, Level level, BlockPos blockPos, Entity entity, CallbackInfo ci) {
        if (blockState.is(Blocks.SOUL_FIRE) && entity instanceof ItemEntity itemE && level instanceof ServerLevel serverLevel) {
            for (var recipe : SoulEngulfingRecipe.getRecipesForStack(itemE.getItem(), level.getRecipeManager())) {
                if (recipe.validateRecipe(blockPos, itemE, serverLevel)) {
                    ci.cancel();
                    break;
                }
            }

            if(itemE.getItem().is(Spirit.SOUL_FIRE_IMMUNE)) {
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
    }
}
