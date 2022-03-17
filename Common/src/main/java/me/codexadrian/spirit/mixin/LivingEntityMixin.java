package me.codexadrian.spirit.mixin;

import me.codexadrian.spirit.Corrupted;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.SpiritRegistry;
import me.codexadrian.spirit.Tier;
import me.codexadrian.spirit.platform.Services;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Corrupted {

    private static final EntityDataAccessor<Boolean> CORRUPTED = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

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
                if (!Arrays.stream(Spirit.getSpiritConfig().getBlacklist()).anyMatch(s -> {
                    ResourceLocation tag = new ResourceLocation(s);
                    return Registry.ENTITY_TYPE.getKey(victim.getType()).equals(tag);
                })) {
                    if (victim.canChangeDimensions() && (Spirit.getSpiritConfig().isCollectFromCorrupt() || !corrupt.isCorrupted())) {
                        ItemStack savedStack = ItemStack.EMPTY;
                        int savedSouls = 0;

                        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                            ItemStack currentItem = player.getInventory().getItem(i);
                            if (currentItem.getItem() != SpiritRegistry.SOUL_CRYSTAL.get()) {
                                continue;
                            }

                            if (savedStack.isEmpty() && (!currentItem.hasTag() || (currentItem.getTag().getCompound("StoredEntity").getString("Type").equals(Registry.ENTITY_TYPE.getKey(victim.getType()).toString())) && currentItem.getTag().getCompound("StoredEntity").getInt("Souls") < SoulUtils.getMaxSouls(currentItem))) {
                                savedStack = currentItem;
                            } else {
                                if (currentItem.hasTag()) {
                                    CompoundTag tag = currentItem.getTag().getCompound("StoredEntity");
                                    if (tag.getString("Type").equals(Registry.ENTITY_TYPE.getKey(victim.getType()).toString()) && tag.getInt("Souls") < SoulUtils.getMaxSouls(currentItem)) {
                                        int souls = tag.getInt("Souls");
                                        if (souls > savedSouls) {
                                            savedStack = currentItem;
                                            savedSouls = souls;
                                        }
                                    }
                                }
                            }
                        }

                        if (!savedStack.isEmpty()) {
                            CompoundTag storedEntity;

                            if (!savedStack.hasTag() || !savedStack.getTag().contains("StoredEntity")) {
                                CompoundTag tag = new CompoundTag();
                                tag.putString("Type", Registry.ENTITY_TYPE.getKey(victim.getType()).toString());
                                savedStack.getOrCreateTag().put("StoredEntity", tag);
                                storedEntity = tag;
                            } else {
                                storedEntity = savedStack.getTag().getCompound("StoredEntity");
                            }

                            storedEntity.putInt("Souls", storedEntity.getInt("Souls") + 1);
                            ServerLevel serverLevel = (ServerLevel) player.level;
                            serverLevel.sendParticles(ParticleTypes.SOUL, victim.getX(), victim.getY(), victim.getZ(), 20, victim.getBbWidth(), victim.getBbHeight(), victim.getBbWidth(), 0);
                            Tier tier = SoulUtils.getTier(savedStack);

                            if (tier != null && storedEntity.getInt("Souls") == tier.getRequiredSouls()) {
                                player.displayClientMessage(new TranslatableComponent("item.spirit.soul_crystal.upgrade_message").withStyle(ChatFormatting.AQUA), true);
                                serverLevel.sendParticles(ParticleTypes.SOUL, player.getX(), player.getY(), player.getZ(), 40, 1, 2, 1, 0);
                            }
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
