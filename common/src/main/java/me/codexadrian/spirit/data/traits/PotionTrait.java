package me.codexadrian.spirit.data.traits;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.MobTrait;
import me.codexadrian.spirit.data.MobTraitSerializer;
import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.Optional;

public record PotionTrait(List<MobEffectInstance> effects) implements MobTrait<PotionTrait> {
    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public void initializeArrow(SoulArrowEntity soulArrow) {
        for (MobEffectInstance effect : effects()) {
            soulArrow.addEffect(new MobEffectInstance(effect));
        }
    }

    @Override
    public void onHitBlock(ToolType type, Entity entity, BlockState blockState, Level level, BlockPos pos) {
        AreaEffectCloud potionCloud = EntityType.AREA_EFFECT_CLOUD.create(entity.level);
        if(potionCloud == null) return;
        for(var effect : effects()) {
            potionCloud.addEffect(new MobEffectInstance(effect));
        }
        potionCloud.setDuration(60);
        potionCloud.setRadius(1);
        potionCloud.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        level.addFreshEntity(potionCloud);
    }

    @Override
    public void onHitEntity(ToolType type, Entity attacker, Entity victim) {
        if(type == ToolType.BOW) return;
        if(victim instanceof LivingEntity livingEntity) {
            for (MobEffectInstance effect : effects()) {
                livingEntity.addEffect(new MobEffectInstance(effect));
            }
        }
    }

    @Override
    public MobTraitSerializer<PotionTrait> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MobTraitSerializer<PotionTrait> {
        public static Codec<MobEffectInstance> EFFECT_CODEC;

        static {
            EFFECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Registry.MOB_EFFECT.byNameCodec().fieldOf("effect").forGetter(MobEffectInstance::getEffect),
                    Codec.INT.fieldOf("duration").orElse(0).forGetter(MobEffectInstance::getDuration),
                    Codec.INT.fieldOf("amplifier").orElse(0).forGetter(MobEffectInstance::getAmplifier),
                    Codec.BOOL.fieldOf("ambient").orElse(false).forGetter(MobEffectInstance::isAmbient),
                    Codec.BOOL.fieldOf("visible").orElse(true).forGetter(MobEffectInstance::isVisible),
                    Codec.BOOL.fieldOf("showIcon").orElse(true).forGetter(MobEffectInstance::showIcon)
            ).apply(instance, Serializer::mobEffectInstanceOf));
        }

        public static final Codec<PotionTrait> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EFFECT_CODEC.listOf().fieldOf("effects").forGetter(PotionTrait::effects)
        ).apply(instance, PotionTrait::new));


        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Spirit.MODID, "potion_effect");
        }

        @Override
        public Codec<PotionTrait> codec() {
            return CODEC;
        }

        public static MobEffectInstance mobEffectInstanceOf(MobEffect effect, int dur, int amp, boolean ambient, boolean visible, boolean icon) {
            return new MobEffectInstance(effect, dur, amp, ambient, visible, icon, null);
        }
    }
}