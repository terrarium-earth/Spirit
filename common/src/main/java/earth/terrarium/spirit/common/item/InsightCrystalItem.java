package earth.terrarium.spirit.common.item;

import earth.terrarium.spirit.api.storage.SoulContainingBlock;
import earth.terrarium.spirit.api.utils.SoulStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class InsightCrystalItem extends Item {
    public InsightCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext useOnContext) {
        if (useOnContext.getLevel().isClientSide) return InteractionResult.SUCCESS;
        if (useOnContext.getLevel().getBlockEntity(useOnContext.getClickedPos()) instanceof SoulContainingBlock block) {
            if (block.getContainer() != null && useOnContext.getPlayer() != null) {
                useOnContext.getPlayer().displayClientMessage(block.getContainer().getSoulStack(0).toComponent(), true);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
