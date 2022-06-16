package me.codexadrian.spirit.data.entitytraits;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.MobTrait;
import me.codexadrian.spirit.data.MobTraitSerializer;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public record ExplosionTrait(float power) implements MobTrait<ExplosionTrait> {
    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public void onHitEntity(Entity attacker, Entity victim) {
        attacker.level.explode(victim, victim.getX(), victim.getY(), victim.getZ(), power(), Explosion.BlockInteraction.NONE);
    }

    @Override
    public void onHitBlock(Entity entity, BlockHitResult hitResult) {
        Vec3 location = hitResult.getLocation();
        entity.level.explode(null, location.x(), location.y(), location.z(), power(), Explosion.BlockInteraction.NONE);
        if(entity instanceof SoulArrowEntity) entity.discard();
    }

    @Override
    public MobTraitSerializer<ExplosionTrait> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MobTraitSerializer<ExplosionTrait> {
        public static final Codec<ExplosionTrait> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("power").orElse(2.0F).forGetter(ExplosionTrait::power)
        ).apply(instance, ExplosionTrait::new));

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Spirit.MODID, "explosion");
        }

        @Override
        public Codec<ExplosionTrait> codec() {
            return CODEC;
        }
    }
}
