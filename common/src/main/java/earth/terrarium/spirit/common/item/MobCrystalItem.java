package earth.terrarium.spirit.common.item;

import earth.terrarium.spirit.api.storage.AutoAbsorbing;
import earth.terrarium.spirit.api.storage.SoulContainingItem;
import earth.terrarium.spirit.common.containers.ItemWrappedMobContainer;
import earth.terrarium.spirit.common.containers.SingleMobContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MobCrystalItem extends Item implements SoulContainingItem, AutoAbsorbing {

    public MobCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        ItemWrappedMobContainer container = getContainer(itemStack);
        if (container != null) {
            if (container.container() instanceof SingleMobContainer singleMobContainer) {
                list.add(singleMobContainer.toComponent());
            }
        }
    }

    @Override
    public @Nullable ItemWrappedMobContainer getContainer(ItemStack object) {
        return new ItemWrappedMobContainer(object, new SingleMobContainer());
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if (!useOnContext.getLevel().isClientSide) {
            Player player = useOnContext.getPlayer();
            if (player != null) {
                ItemStack stack = player.getItemInHand(useOnContext.getHand());
                if (stack.getItem() instanceof MobCrystalItem) {
                    ItemWrappedMobContainer container = getContainer(stack);
                    if (container != null) {
                        LivingEntity entity = container.extractMob(useOnContext.getLevel());
                        container.update();
                        if (entity != null) {
                            BlockPos offset = useOnContext.getClickedPos().relative(useOnContext.getClickedFace());
                            entity.setPos(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5);
                            useOnContext.getLevel().addFreshEntity(entity);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return super.useOn(useOnContext);
    }

    @Override
    public int getPriority() {
        return 15;
    }
}
