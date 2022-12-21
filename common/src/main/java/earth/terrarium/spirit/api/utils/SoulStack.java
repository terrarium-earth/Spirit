package earth.terrarium.spirit.api.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SoulStack {
    private static final String ENTITY_KEY = "Entity";
    private static final String AMOUNT_KEY = "Amount";
    private final EntityType<?> entity;
    private int amount;

    public static final Codec<SoulStack> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf(ENTITY_KEY).forGetter(SoulStack::getEntity),
            Codec.intRange(0, Integer.MAX_VALUE).fieldOf(AMOUNT_KEY).forGetter(SoulStack::getAmount)
    ).apply(instance, SoulStack::new));

    public SoulStack(@Nullable EntityType<?> entity, int amount) {
        this.entity = entity;
        this.amount = amount;
    }

    @Nullable
    public EntityType<?> getEntity() {
        return entity;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = Mth.clamp(amount, 0, Integer.MAX_VALUE);
    }

    public boolean isEmpty() {
        return amount == 0 || entity == null;
    }

    public SoulStack copy() {
        return new SoulStack(entity, amount);
    }

    public static SoulStack empty() {
        return new SoulStack(null, 0);
    }

    public static SoulStack fromTag(CompoundTag tag) {
        return new SoulStack(EntityType.byString(tag.getString(ENTITY_KEY)).orElse(null), tag.getInt(AMOUNT_KEY));
    }

    public CompoundTag toTag(CompoundTag tag) {
        if(entity != null) {
            tag.putString(ENTITY_KEY, EntityType.getKey(entity).toString());
            tag.putInt(AMOUNT_KEY, amount);
        }
        return tag;
    }

    //toEntity method
    @Nullable
    public LivingEntity toEntity(Level level) {
        if (getEntity() == null) return null;
        return (LivingEntity) this.getEntity().create(level);
    }
}
