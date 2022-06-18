package me.codexadrian.spirit.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.level.Level;

public class SoulEntity extends AmbientCreature {
    public SoulEntity(EntityType<? extends AmbientCreature> entityType, Level level) {
        super(entityType, level);
    }
}
