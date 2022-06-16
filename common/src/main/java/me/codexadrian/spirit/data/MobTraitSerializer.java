package me.codexadrian.spirit.data;

import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;

public interface MobTraitSerializer<T extends MobTrait<T>> {
    ResourceLocation id();

    Codec<T> codec();
}
