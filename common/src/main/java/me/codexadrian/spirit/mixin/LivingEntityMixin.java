package me.codexadrian.spirit.mixin;

import me.codexadrian.spirit.Corrupted;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.SpiritRegistry;
import me.codexadrian.spirit.Tier;
import me.codexadrian.spirit.blocks.blockentity.SoulPedestalBlockEntity;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Corrupted {

    private static final EntityDataAccessor<Boolean> CORRUPTED =
            SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineCorrupted(CallbackInfo ci) {
        entityData.define(CORRUPTED, false);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCorrupted(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.getBoolean("Corrupted")) {
            setCorrupted();
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveCorrupted(CompoundTag compoundTag, CallbackInfo ci) {
        if (isCorrupted()) {
            compoundTag.putBoolean("Corrupted", true);
        }
    }

    @Inject(method = "die", at = @At("HEAD"))
    private void onDeath(DamageSource source, CallbackInfo ci) {
        LivingEntity victim = (LivingEntity) (Object) this;
        Corrupted corrupt = (Corrupted) victim;
        if (!victim.level.isClientSide) {
            if (source.getEntity() instanceof Player player) {
                if (!Arrays.stream(Spirit.getSpiritConfig().getBlacklist()).anyMatch(s -> Registry.ENTITY_TYPE.getKey(victim.getType()).equals(new ResourceLocation(s)))) {
                    if (victim.canChangeDimensions() && (Spirit.getSpiritConfig().isCollectFromCorrupt() || !corrupt.isCorrupted())) {
                        ItemStack crystal = ItemStack.EMPTY;
                        AABB entityArea = victim.getBoundingBox().inflate(3, 1, 3);
                        Optional<BlockPos> pedestalPos = BlockPos.betweenClosedStream(entityArea).filter(pos -> level.getBlockEntity(pos) instanceof SoulPedestalBlockEntity).findFirst();
                        if(pedestalPos.isPresent() && level.getBlockEntity(pedestalPos.get()) instanceof SoulPedestalBlockEntity pedestal && !pedestal.isEmpty()) {
                            crystal = pedestal.getItem(0);
                        }
                        if(crystal.isEmpty() || !SoulUtils.canCrystalAcceptSoul(crystal, victim)) {
                            crystal = SoulUtils.getSoulCrystal(player, victim);
                            if (crystal.isEmpty()) {
                                crystal = SoulUtils.getCrudeSoulCrystal(player);
                            }
                        }
                        if(!crystal.isEmpty()) {
                            if(crystal.is(SpiritRegistry.SOUL_CRYSTAL)) SoulUtils.handleSoulCrystal(crystal, player, victim);
                            else if(crystal.is(SpiritRegistry.CRUDE_SOUL_CRYSTAL)) SoulUtils.handleCrudeSoulCrystal(crystal, player, victim);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isCorrupted() {
        return entityData.get(CORRUPTED);
    }

    @Override
    public void setCorrupted() {
        entityData.set(CORRUPTED, true);
    }
}
