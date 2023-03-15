package earth.terrarium.spirit.common.handlers;

import earth.terrarium.spirit.common.containers.ItemWrappedMobContainer;
import earth.terrarium.spirit.common.containers.SingleMobContainer;
import earth.terrarium.spirit.common.item.MobCrystalItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MobCrystalHandler {

    public static InteractionResult mobInteraction(Entity entity, Player player, InteractionHand hand) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.getType().getCategory() != MobCategory.MISC) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof MobCrystalItem item) {
                if (!player.level.isClientSide) {
                    ItemWrappedMobContainer container = item.getContainer(stack);
                    if (container != null) {
                        if (container.insertMob(livingEntity)) {
                            entity.discard();
                            return InteractionResult.CONSUME;
                        }
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public static float mobCrystalProperty(ItemStack itemStack) {
        if (itemStack.getItem() instanceof MobCrystalItem item) {
            ItemWrappedMobContainer container = item.getContainer(itemStack);
            if (container != null) {
                if (container.container() instanceof SingleMobContainer singleMobContainer) {
                    return singleMobContainer.entityType == null ? 0 : 1;
                }
            }
        }
        return 0;
    }
}
