package earth.terrarium.spirit.common.mixin;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import earth.terrarium.spirit.common.item.tools.SoulSteelTool;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
public class BlockMixin {

    @Unique
    private boolean cancelFalling = false;

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

    @Inject(at=@At("HEAD"), method="fallOn", cancellable = true)
    public void onLandedUpon(Level level, BlockState blockState, BlockPos blockPos, Entity entity, float f, CallbackInfo ci){
        cancelFalling = false;
        if (entity instanceof Player player) {
            for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                ItemStack itemStack = player.getItemBySlot(slot);
                ArmorAbility ability = SoulSteelArmor.getAbility(itemStack);
                if (ability != null && entity.fallDistance > 2f) {
                    Vec3 vec3d = entity.getDeltaMovement();
                    if (vec3d.y < 0.0D) {
                        cancelFalling = cancelFalling || ability.onLand(player, slot, itemStack, entity.fallDistance);
                    }
                }
            }
            if (cancelFalling) {
                ci.cancel();
            }
        }
    }

    @Inject(at=@At("HEAD"), method="updateEntityAfterFallOn", cancellable = true)
    public void onEntityLand(BlockGetter blockGetter, Entity entity, CallbackInfo ci) {
        if (cancelFalling) {
            entity.fallDistance = 0f;
            entity.playSound(SoundEvents.ARMOR_EQUIP_IRON, 1f, 1f);
            ci.cancel();
        }
    }
}
