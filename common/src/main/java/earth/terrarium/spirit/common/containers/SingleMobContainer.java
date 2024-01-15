package earth.terrarium.spirit.common.containers;

import earth.terrarium.spirit.api.utils.SoulfulCreature;
import earth.terrarium.spirit.api.souls.InteractionMode;
import earth.terrarium.spirit.api.souls.base.MobContainer;
import earth.terrarium.spirit.api.souls.base.SingleSoulStackContainer;
import earth.terrarium.spirit.api.souls.stack.SoulStack;
import earth.terrarium.spirit.api.souls.SoulApi;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SingleMobContainer extends SingleSoulStackContainer implements MobContainer {
    @Nullable
    public EntityType<?> entityType;
    @Nullable
    public CompoundTag entityData;
    public boolean soulless;

    public static final String MOB_KEY = "Mob";
    public static final String ID_KEY = "EntityType";
    public static final String DATA_KEY = "Data";

    @Override
    public SoulStack getSoulStack(int index) {
        return new SoulStack(entityType, entityType == null ? 0 : 1);
    }

    @Override
    public int insertIntoSlot(SoulStack soulStack, int slot, InteractionMode mode) {
        if (entityType == null) {
            return 0;
        } else {
            if (entityType == soulStack.getEntity()) {
                if (!soulless) {
                    return 0;
                } else {
                    if (mode == InteractionMode.NO_TAKE_BACKSIES) soulless = false;
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public SoulStack extractFromSlot(SoulStack soulStack, int slot, InteractionMode mode) {
        if (entityType == null) {
            return SoulStack.empty();
        } else {
            if (entityType == soulStack.getEntity()) {
                if (!soulless) {
                    if (mode == InteractionMode.NO_TAKE_BACKSIES) soulless = true;
                    return new SoulStack(entityType, 1);
                } else {
                    return SoulStack.empty();
                }
            }
        }
        return SoulStack.empty();
    }

    @Override
    public boolean insertMob(LivingEntity mob) {
        if (entityType == null) {
            entityType = mob.getType();
            entityData = mob.saveWithoutId(new CompoundTag());
            soulless = ((SoulfulCreature) mob).isSoulless();
            return true;
        }
        return false;
    }

    @Nullable
    public LivingEntity extractMob(Level level) {
        if (entityType == null) {
            return null;
        } else {
            LivingEntity mob = (LivingEntity) entityType.create(level);
            if (mob != null) {
                mob.load(entityData);
                entityType = null;
                entityData = null;
                ((SoulfulCreature) mob).setIfSoulless(soulless);
                soulless = true;
                return mob;
            }
        }
        return null;
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        CompoundTag entity = new CompoundTag();
        if (entityType != null) {
            entity.put(DATA_KEY, entityData);
            entity.putString(ID_KEY, EntityType.getKey(entityType).toString());
            entity.putBoolean(SoulApi.SOULLESS_TAG, soulless);
        }
        tag.put(MOB_KEY, entity);
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        if (tag.contains(MOB_KEY)) {
            CompoundTag entity = tag.getCompound(MOB_KEY);
            entityData = entity.getCompound(DATA_KEY);
            entityType = EntityType.byString(entity.getString(ID_KEY)).orElse(null);
            soulless = entity.getBoolean(SoulApi.SOULLESS_TAG);
        }
    }

    @Override
    public int maxCapacity() {
        return 1;
    }

    public MutableComponent toComponent() {
        if (entityType != null) {
            MutableComponent component = Component.translatable(entityType.getDescriptionId()).withStyle(ChatFormatting.GRAY);
            if (!soulless) {
                return Component.translatable("spirit.item.mob_container.soulful", component).withStyle(ChatFormatting.RED);
            } else {
                return Component.translatable("spirit.item.mob_container.soulless", component).withStyle(ChatFormatting.AQUA);
            }
        } else {
            return Component.translatable("spirit.item.crystal.tooltip_empty").withStyle(ChatFormatting.GRAY);
        }
    }
}
