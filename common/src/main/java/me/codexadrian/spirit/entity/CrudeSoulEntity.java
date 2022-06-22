package me.codexadrian.spirit.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.level.Level;

public class CrudeSoulEntity extends AmbientCreature {
    public CrudeSoulEntity(EntityType<? extends AmbientCreature> entityType, Level level) {
        super(entityType, level);
    }
}
