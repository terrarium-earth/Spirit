package me.codexadrian.spirit.compat.jei.ingredients;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
 * @author Team Resourceful
 */
public class EntityIngredient {

    @Nullable
    private final Entity entity;
    private final EntityType<?> entityType;
    private final Optional<CompoundTag> nbt;
    private final float rotation;

    private final int soulCount;

    public EntityIngredient(EntityType<?> entityType, float rotation){
        this(entityType, rotation, Optional.empty());
    }

    public EntityIngredient(EntityType<?> entityType, float rotation, Optional<CompoundTag> nbt) {
        this(entityType, nbt, rotation, 1);
    }

    public EntityIngredient(EntityType<?> entityType, Optional<CompoundTag> nbt, float rotation, int soulCount) {
        this.rotation = rotation;
        this.entityType = entityType;
        this.nbt = nbt;
        this.soulCount = soulCount;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            entity = this.entityType.create(mc.level);
            if (entity != null) nbt.ifPresent(entity::load);
        }else {
            entity = null;
        }
    }

    public float getRotation() {
        return rotation;
    }

    public @Nullable Entity getEntity() {
        return entity;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public Optional<CompoundTag> getNbt() {
        return nbt;
    }

    public int getSoulCount() {
        return soulCount;
    }

    public Component getDisplayName() {
        return Component.literal(this.soulCount > 1 ? this.soulCount + " * " : "").append(entity == null ? entityType.getDescription() : entity.getDisplayName());
    }

    public List<Component> getTooltip() {
        List<Component> tooltip = new ArrayList<>();

        if (entity != null) {
            if (Minecraft.getInstance().options.advancedItemTooltips) {
                ResourceLocation key = Registry.ENTITY_TYPE.getKey(entityType);
                if (key != null) {
                    tooltip.add(Component.literal(key.toString()).withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }
        return tooltip;
    }

    @Override
    public String toString() {
        ResourceLocation key = Registry.ENTITY_TYPE.getKey(entityType);
        return key != null ? key.toString() : entityType.toString();
    }
}