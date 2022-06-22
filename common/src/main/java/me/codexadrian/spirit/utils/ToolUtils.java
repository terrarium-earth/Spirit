package me.codexadrian.spirit.utils;

import me.codexadrian.spirit.data.MobTrait;
import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.registry.SpiritItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class ToolUtils {

    @NotNull
    public static InteractionResultHolder<ItemStack> handleToolDrawing(Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        ItemStack crystal = SoulUtils.findCrystal(player, null, true);
        if (player.getAbilities().instabuild || (!crystal.isEmpty() && SoulUtils.getSoulsInCrystal(crystal) > 0)) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    public static InteractionResult handleOnHitBlock(InteractionResult result, ToolType type, Player player, ItemStack tool, Level level, BlockPos pos) {
        if(result == InteractionResult.CONSUME) {
            ItemStack soulCrystal = SoulUtils.findCrystal(player, null, true, true);
            if (soulCrystal.is(SpiritItems.SOUL_CRYSTAL.get()) && SoulUtils.getSoulsInCrystal(soulCrystal) > 0) {
                if (tool.getOrCreateTag().getBoolean("Charged")) {
                    var entityEffect = MobTraitData.getEffectForEntity(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(Objects.requireNonNull(SoulUtils.getSoulCrystalType(soulCrystal)))), level.getRecipeManager());
                    if (entityEffect.isPresent()) {
                        for (MobTrait<?> trait : entityEffect.get().traits()) {
                            trait.onHitBlock(type, player, level.getBlockState(pos), level, pos);
                        }
                        SoulUtils.deviateSoulCount(soulCrystal, -1, level, null);
                        spawnParticles(player);
                    }
                }
            }
        }
        return result;
    }

    public static void handleBreakBlock(Player player, ToolType type, ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos) {
        ItemStack soulCrystal = SoulUtils.findCrystal(player, null, true, true);
        if (soulCrystal.is(SpiritItems.SOUL_CRYSTAL.get()) && SoulUtils.getSoulsInCrystal(soulCrystal) > 0) {
            if (itemStack.getOrCreateTag().getBoolean("Charged")) {
                var entityEffect = MobTraitData.getEffectForEntity(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(Objects.requireNonNull(SoulUtils.getSoulCrystalType(soulCrystal)))), level.getRecipeManager());
                if (entityEffect.isPresent()) {
                    for (MobTrait<?> trait : entityEffect.get().traits()) {
                        trait.onHitBlock(type, player, blockState, level, blockPos);
                    }
                    SoulUtils.deviateSoulCount(soulCrystal, -1, level, null);
                    spawnParticles(player);
                }
            }
        }
    }

    public static void handleOnHitEntity(ItemStack itemStack, ToolType type, LivingEntity victim, Player player) {
        ItemStack soulCrystal = SoulUtils.findCrystal(player, null, true, true);
        if (!soulCrystal.isEmpty() && soulCrystal.is(SpiritItems.SOUL_CRYSTAL.get()) && SoulUtils.getSoulsInCrystal(soulCrystal) > 0) {
            if (itemStack.getOrCreateTag().getBoolean("Charged")) {
                var entityEffect = MobTraitData.getEffectForEntity(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(Objects.requireNonNull(SoulUtils.getSoulCrystalType(soulCrystal)))), player.getLevel().getRecipeManager());
                if (entityEffect.isPresent()) {
                    for (MobTrait<?> trait : entityEffect.get().traits()) {
                        trait.onHitEntity(type, player, victim);
                    }
                    SoulUtils.deviateSoulCount(soulCrystal, -1, player.level, null);
                    spawnParticles(player);
                }
            }
        }
    }

    public static void appendEmpoweredText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        if (itemStack.getOrCreateTag().getBoolean("Charged")) {
            list.add(Component.translatable("spirit.item.soul_steel_tool.empowered"));
        } else {
            list.add(Component.translatable("spirit.item.soul_steel_tool.unpowered"));
        }
    }

    public static void spawnParticles(Player player) {
        if (player.getLevel() instanceof ServerLevel serverLevel) {
            for (double i = 0; i < 1; i += 0.1) {
                serverLevel.sendParticles(
                        ParticleTypes.SOUL,
                        player.getX() + Math.sin(i * 2 * Math.PI),
                        player.getY() + 0.75,
                        player.getZ() + Math.cos(i * 2 * Math.PI),
                        1,
                        0,
                        0,
                        0,
                        0
                );
            }
        }
    }
}
