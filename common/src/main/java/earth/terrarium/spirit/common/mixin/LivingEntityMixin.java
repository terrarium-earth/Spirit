package earth.terrarium.spirit.common.mixin;

import earth.terrarium.spirit.api.souls.SoulfulCreature;
import earth.terrarium.spirit.api.utils.SoulUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SoulfulCreature {

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
        setIfSoulless(compoundTag.getBoolean(SoulUtils.SOULFUL_TAG));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveCorrupted(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean(SoulUtils.SOULFUL_TAG, isSoulless());
    }

    @Override
    public boolean isSoulless() {
        return entityData.get(SOULLESS);
    }

    @Override
    public void setIfSoulless(boolean state) {
        entityData.set(SOULLESS, state);
    }

    /*
    @Inject(method = "getAttributeValue", at = @At("RETURN"), cancellable = true)
    public void getMobTraitDamage(Attribute attribute, CallbackInfoReturnable<Double> cir) {
        //noinspection ConstantConditions
        if(attribute == Attributes.ATTACK_DAMAGE && (Object) this instanceof Player player && player.getInventory() != null) {
            if (player.getMainHandItem().is(SpiritItems.SOUL_STEEL_AXE.get()) || player.getMainHandItem().is(SpiritItems.SOUL_STEEL_BLADE.get())) {
                if (player.getMainHandItem().getOrCreateTag().getBoolean("Charged")) {
                    ItemStack soulCrystal = SoulUtils.findCrystal(player, null, true, true, false);
                    if (!soulCrystal.isEmpty()) {
                        String soulCrystalType = SoulUtils.getSoulCrystalType(soulCrystal);
                        if (soulCrystalType != null && SoulUtils.getSoulsInCrystal(soulCrystal) > 0) {
                            var entityEffect = MobTraitData.getEffectForEntity(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(soulCrystalType)), player.getLevel().getRecipeManager());
                            if (entityEffect.isPresent()) {
                                int damage = 0;
                                for (var trait : entityEffect.get().traits()) {
                                    if (trait instanceof DamageTrait damageTrait) {
                                        damage += damageTrait.additionalDamage();
                                    }
                                }
                                cir.setReturnValue(cir.getReturnValueD() + damage);
                            }
                        }
                    }
                }
            }
        }
    }
    */
}
