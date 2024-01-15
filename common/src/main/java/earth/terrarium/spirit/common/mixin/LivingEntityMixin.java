package earth.terrarium.spirit.common.mixin;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.utils.SoulfulCreature;
import earth.terrarium.spirit.api.souls.SoulApi;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SoulfulCreature {

    @Shadow public abstract Iterable<ItemStack> getArmorSlots();

    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final EntityDataAccessor<Boolean> SOULLESS = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void defineCorrupted(CallbackInfo ci) {
        entityData.define(SOULLESS, false);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readCorrupted(CompoundTag compoundTag, CallbackInfo ci) {
        setIfSoulless(compoundTag.getBoolean(SoulApi.SOULLESS_TAG));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveCorrupted(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean(SoulApi.SOULLESS_TAG, isSoulless());
    }

    @Override
    public boolean isSoulless() {
        return entityData.get(SOULLESS);
    }

    @Override
    public void setIfSoulless(boolean state) {
        entityData.set(SOULLESS, state);
    }

    @Inject(method = "onChangedBlock", at = @At("TAIL"))
    private void onChangedBlock(CallbackInfo ci) {
        this.getArmorSlots().forEach(stack -> {
            if (stack.getItem() instanceof SoulSteelArmor armor) {
                ArmorAbility ability = armor.getAbility(stack);
                if (ability != null) {
                    ability.onEntityMove(stack, (LivingEntity) (Object) this, this.level(), this.blockPosition());
                }
            }
        });
    }
}
