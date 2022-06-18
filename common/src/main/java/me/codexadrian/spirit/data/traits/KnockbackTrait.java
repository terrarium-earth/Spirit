package me.codexadrian.spirit.data.traits;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.MobTrait;
import me.codexadrian.spirit.data.MobTraitSerializer;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import net.minecraft.resources.ResourceLocation;

public record KnockbackTrait(int knockback) implements MobTrait<KnockbackTrait> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public void initializeArrow(SoulArrowEntity soulArrow) {
        soulArrow.setKnockback(knockback);
    }

    @Override
    public MobTraitSerializer<KnockbackTrait> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MobTraitSerializer<KnockbackTrait> {
        public static final Codec<KnockbackTrait> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("knockback").forGetter(KnockbackTrait::knockback)
        ).apply(instance, KnockbackTrait::new));

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Spirit.MODID, "knockback");
        }

        @Override
        public Codec<KnockbackTrait> codec() {
            return CODEC;
        }
    }
}
