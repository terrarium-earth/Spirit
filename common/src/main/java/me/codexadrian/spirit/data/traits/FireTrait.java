package me.codexadrian.spirit.data.traits;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.MobTrait;
import me.codexadrian.spirit.data.MobTraitSerializer;
import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.entity.SoulArrowEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public record FireTrait(int burnTime) implements MobTrait<FireTrait> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public void initializeArrow(SoulArrowEntity soulArrow) {
        soulArrow.setSecondsOnFire(burnTime());
    }

    @Override
    public void onHitEntity(ToolType type, Entity attacker, Entity victim) {
        if(type == ToolType.BOW) return;
        victim.setSecondsOnFire(burnTime());
    }

    @Override
    public void onHitBlock(ToolType type, Entity entity, BlockState blockState, Level level, BlockPos pos) {
        if (BaseFireBlock.canBePlacedAt(level, pos, Direction.DOWN)) {
            level.setBlock(pos, BaseFireBlock.getState(level, pos), 11);
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
