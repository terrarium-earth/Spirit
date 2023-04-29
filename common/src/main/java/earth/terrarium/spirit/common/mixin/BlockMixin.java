package earth.terrarium.spirit.common.mixin;

import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.common.item.tools.SoulSteelTool;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    private static void spirit$getDrops(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, BlockEntity blockEntity, Entity entity, ItemStack itemStack, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (entity instanceof Player player && itemStack.getItem() instanceof SoulSteelTool tool) {
            ToolAbility ability = tool.getAbility(itemStack);
            if (ability != null) {
                ArrayList<ItemStack> drops = new ArrayList<>(cir.getReturnValue());
                ability.modifyDrops(player, drops);
                cir.setReturnValue(drops);
            }
        }
    }
}
