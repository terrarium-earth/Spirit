package me.codexadrian.spirit.data;

import me.codexadrian.spirit.entity.SoulArrowEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
 * @author Team Resourceful
 */

public interface MobTrait<T extends MobTrait<T>> {

    default void initializeArrow(SoulArrowEntity soulArrow){}

    default void onHitEntity(ToolType type, @Nullable Entity attacker, Entity victim){}

    default void onHitBlock(ToolType type, Entity owner, BlockState blockState, Level level, BlockPos pos){}

    default void onArrowFire(SoulArrowEntity soulArrow) {}

    default void onArrowTick(SoulArrowEntity soulArrow) {}

    MobTraitSerializer<T> serializer();
}