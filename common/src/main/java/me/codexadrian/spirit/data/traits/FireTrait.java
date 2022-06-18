package me.codexadrian.spirit.data.traits;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.MobTrait;
import me.codexadrian.spirit.data.MobTraitSerializer;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.phys.BlockHitResult;

public record FireTrait(int burnTime) implements MobTrait<FireTrait> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public void initializeArrow(SoulArrowEntity soulArrow) {
        soulArrow.setSecondsOnFire(burnTime());
    }

    @Override
    public void onHitEntity(Entity attacker, Entity victim) {
        victim.setSecondsOnFire(burnTime());
    }

    @Override
    public void onHitBlock(Entity owner, BlockHitResult hitResult) {
        Level level = owner.level;
        if (BaseFireBlock.canBePlacedAt(level, hitResult.getBlockPos(), hitResult.getDirection())) {
            level.setBlock(hitResult.getBlockPos(), BaseFireBlock.getState(level, hitResult.getBlockPos()), 11);
        }
    }

    @Override
    public MobTraitSerializer<FireTrait> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MobTraitSerializer<FireTrait> {
        public static final Codec<FireTrait> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("burnTime").orElse(120).forGetter(FireTrait::burnTime)
        ).apply(instance, FireTrait::new));

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Spirit.MODID, "fire");
        }

        @Override
        public Codec<FireTrait> codec() {
            return CODEC;
        }
    }
}
