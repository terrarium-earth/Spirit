package me.codexadrian.spirit.data;

import me.codexadrian.spirit.entity.SoulArrowEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;

/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
 * @author Team Resourceful
 */

public interface MobTrait<T extends MobTrait<T>> {

    default void initializeArrow(SoulArrowEntity soulArrow){}

    default void onHitEntity(Entity attacker, Entity victim){}

    default void onHitBlock(Entity owner, BlockHitResult hitResult){}

    default void onBreakBlock(Entity owner, BlockHitResult hitResult){}

    default void onArrowFire(SoulArrowEntity soulArrow) {}

    default void onArrowTick(SoulArrowEntity soulArrow) {}

    MobTraitSerializer<T> serializer();
}