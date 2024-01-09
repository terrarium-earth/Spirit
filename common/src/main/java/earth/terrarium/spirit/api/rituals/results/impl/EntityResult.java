package earth.terrarium.spirit.api.rituals.results.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.rituals.results.RitualResult;
import earth.terrarium.spirit.api.rituals.results.RitualResultSerializer;
import earth.terrarium.spirit.compat.rei.ComponentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Optional;

public record EntityResult(EntityType<?> entityType, Optional<CompoundTag> nbt) implements RitualResult<EntityResult> {

    public static final Serializer SERIALIZER = new Serializer();

    @Override
    public ItemStack getItemRepresentation() {
        return Items.SKELETON_SKULL.getDefaultInstance();
    }

    @Override
    public void onRitualComplete(Level level, BlockPos blockPos, ItemStack catalyst) {
        Entity entity = entityType.create(level);
        if (entity != null) {
            nbt.ifPresent(entity::load);
            entity.setPos(blockPos.getX() + 0.5, blockPos.getY() + 0.75, blockPos.getZ() + 0.5);
            level.addFreshEntity(entity);
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.SOUL, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
            }
        }
    }

    @Override
    public ComponentUtils.ReiPlacer getREIPlacer() {
        return ComponentUtils.soulOutputPlacer(this);
    }

    @Override
    public RitualResultSerializer<EntityResult> getSerializer() {
        return SERIALIZER;
    }

    public static class Serializer implements RitualResultSerializer<EntityResult> {
        public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "entity");
        public static final Codec<EntityResult> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(EntityResult::entityType),
                CompoundTag.CODEC.optionalFieldOf("nbt").forGetter(EntityResult::nbt)
        ).apply(instance, EntityResult::new));

        @Override
        public ResourceLocation id() {
            return ID;
        }

        @Override
        public Codec<EntityResult> codec() {
            return CODEC;
        }
    }
}
