package me.codexadrian.spirit.data.traits;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.MobTrait;
import me.codexadrian.spirit.data.MobTraitSerializer;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import net.minecraft.resources.ResourceLocation;

public record DamageTrait(float additionalDamage) implements MobTrait<DamageTrait> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public void initializeArrow(SoulArrowEntity soulArrow) {
        soulArrow.setBaseDamage(soulArrow.getBaseDamage() + additionalDamage());
    }

    @Override
    public MobTraitSerializer<DamageTrait> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MobTraitSerializer<DamageTrait> {
        public static final Codec<DamageTrait> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("additionalDamage").forGetter(DamageTrait::additionalDamage)
        ).apply(instance, DamageTrait::new));

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Spirit.MODID, "damage");
        }

        @Override
        public Codec<DamageTrait> codec() {
            return CODEC;
        }
    }
}
