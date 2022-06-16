package me.codexadrian.spirit.data;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
 * @author Team Resourceful
 */

public interface MobTraitSerializer<T extends MobTrait<T>> {
    ResourceLocation id();

    Codec<T> codec();
}
