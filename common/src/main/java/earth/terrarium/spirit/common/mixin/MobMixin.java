package earth.terrarium.spirit.common.mixin;

import earth.terrarium.spirit.common.item.trinkets.BaseTrinket;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {

    @Shadow @Final protected GoalSelector goalSelector;

    @SuppressWarnings("ConstantValue")
    @Inject(method = "<init>", at = @At("RETURN"))
    public void spirit$init(EntityType<? extends Mob> entityType, Level level, CallbackInfo ci) {
        if (level != null && !level.isClientSide && ((Object) this) instanceof Creeper creeper) {
            this.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, Player.class, (livingEntity) -> {
                if (livingEntity instanceof Player player) {
                    return player.getInventory().hasAnyMatching(itemStack -> itemStack.getItem() == SpiritItems.CAT_CHARM.get() && BaseTrinket.isEnabled(itemStack));
                }
                return false;
            }, 6, 1, 1.2, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test));
        }
    }
}
