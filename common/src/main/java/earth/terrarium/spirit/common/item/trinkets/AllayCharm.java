package earth.terrarium.spirit.common.item.trinkets;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class AllayCharm extends BaseTrinket {

    public AllayCharm(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean bl) {
        if (level instanceof ServerLevel serverLevel && isEnabled(itemStack)) {
            //on tick, push all items in a 5x5x5 area toward the player
            AABB magnetBox = entity.getBoundingBox().inflate(5, 5, 5);
            level.getEntitiesOfClass(ItemEntity.class, magnetBox).forEach(e -> {
                if (e != entity) {
                    e.setDeltaMovement(e.getDeltaMovement().add(entity.position().subtract(e.position()).normalize().scale(0.1)));
                    e.hurtMarked = true;
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME , e.getX(), e.getY(), e.getZ(), 1, 0, 0, 0, 0);
                }
            });
        }
    }
}
