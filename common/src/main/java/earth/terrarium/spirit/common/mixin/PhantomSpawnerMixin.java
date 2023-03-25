package earth.terrarium.spirit.common.mixin;

import earth.terrarium.spirit.common.item.trinkets.BaseTrinket;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/Phantom;finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/entity/SpawnGroupData;",
                    ordinal = 0,
                    shift = At.Shift.BEFORE
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILSOFT // SOFT, because this will break in 2 seconds
    )
    private void checkPhantomSpawn(ServerLevel serverLevel, boolean bl, boolean bl2, CallbackInfoReturnable<Integer> cir, RandomSource randomSource, int i, Iterator var6, Player player, BlockPos blockPos, DifficultyInstance difficultyInstance, ServerStatsCounter serverStatsCounter, int j, int k, BlockPos blockPos2, BlockState blockState, FluidState fluidState, SpawnGroupData spawnGroupData, int l, int m, Phantom phantom) {
        if (player.getInventory().hasAnyMatching(itemStack -> itemStack.getItem() == SpiritItems.PHANTOM_CHARM.get() && BaseTrinket.isEnabled(itemStack))) {
            cir.setReturnValue(0);
        }
    }
}
