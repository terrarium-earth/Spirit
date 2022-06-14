package me.codexadrian.spirit.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.codexadrian.spirit.SpiritRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.Optional;

public record SoulArrowEffect(ResourceLocation id, EntityType<?> entity, boolean isOnFire, int burnTime, double additionalDamage, Optional<List<MobEffectInstance>> potionEffects) implements SyncedData {

    public static Codec<MobEffectInstance> EFFECT_CODEC;

    static {
        EFFECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Registry.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(MobEffectInstance::getEffect),
                Codec.INT.fieldOf("duration").orElse(0).forGetter(MobEffectInstance::getDuration),
                Codec.INT.fieldOf("amplifier").orElse(0).forGetter(MobEffectInstance::getAmplifier),
                Codec.BOOL.fieldOf("ambient").orElse(false).forGetter(MobEffectInstance::isAmbient),
                Codec.BOOL.fieldOf("visible").orElse(true).forGetter(MobEffectInstance::isVisible),
                Codec.BOOL.fieldOf("showIcon").orElse(true).forGetter(MobEffectInstance::showIcon),
                MobEffectInstance.FactorData.CODEC.optionalFieldOf("factorData").forGetter(MobEffectInstance::getFactorData)
        ).apply(instance, SoulArrowEffect::mobEffectInstanceOf));
    }

    public static Codec<SoulArrowEffect> codec(ResourceLocation id) {
        return RecordCodecBuilder.create(instance -> instance.group(
                RecordCodecBuilder.point(id),
                Registry.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(SoulArrowEffect::entity),
                Codec.BOOL.fieldOf("isOnFire").orElse(false).forGetter(SoulArrowEffect::isOnFire),
                Codec.INT.fieldOf("burnTime").orElse(100).forGetter(SoulArrowEffect::burnTime),
                Codec.DOUBLE.fieldOf("additionalDamage").orElse(0.0).forGetter(SoulArrowEffect::additionalDamage),
                EFFECT_CODEC.listOf().optionalFieldOf("potionEffects").forGetter(SoulArrowEffect::potionEffects)
        ).apply(instance, SoulArrowEffect::new));
    }


    public static MobEffectInstance mobEffectInstanceOf(MobEffect effect, int dur, int amp, boolean ambient, boolean visible, boolean icon, Optional<MobEffectInstance.FactorData> data) {
        return new MobEffectInstance(effect, dur, amp, ambient, visible, icon, null, data);
    }

    @Override
    public ResourceLocation getId() {
        return id();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SpiritRegistry.SOUL_ARROW_EFFECT_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return SpiritRegistry.SOUL_ARROW_EFFECT_RECIPE.get();
    }

    public static Optional<SoulArrowEffect> getEffectForEntity(EntityType<?> entityType, RecipeManager manager) {
        return manager.getAllRecipesFor(SpiritRegistry.SOUL_ARROW_EFFECT_RECIPE.get()).stream().filter(recipe -> recipe.entity.equals(entityType)).findFirst();
    }
}
