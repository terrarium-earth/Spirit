package earth.terrarium.spirit.api.storage.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import earth.terrarium.spirit.api.utils.SoulStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SoulIngredient implements Predicate<SoulStack> {
    public static final Codec<SoulIngredient> CODEC = Codec.either(EntityValue.CODEC, TagValue.CODEC)
            .listOf()
            .xmap(SoulIngredient::new, SoulIngredient::getRawValues);

    private final List<Either<EntityValue, TagValue>> values;
    private List<SoulStack> soulStacks;

    private SoulIngredient(List<Either<EntityValue, TagValue>> stream) {
        this.values = stream;
    }

    public static SoulIngredient of() {
        return new SoulIngredient(List.of());
    }

    public static SoulIngredient of(EntityType<?>... entityTypes) {
        return SoulIngredient.of(Arrays.stream(entityTypes).map(SoulStack::of));
    }

    public static SoulIngredient of(SoulStack... soulStacks) {
        return SoulIngredient.of(Arrays.stream(soulStacks));
    }

    public static SoulIngredient of(Stream<SoulStack> soulStackStream) {
        List<Either<EntityValue, TagValue>> values = new ArrayList<>();
        for (SoulStack soulStack : soulStackStream.filter(Predicate.not(SoulStack::isEmpty)).toList()) {
            values.add(Either.left(new EntityValue(soulStack)));
        }
        return new SoulIngredient(values);
    }

    public static SoulIngredient of(TagKey<EntityType<?>> tag) {
        return new SoulIngredient(List.of(Either.right(new TagValue(tag))));
    }

    @Override
    public boolean test(SoulStack soulStack) {
        if (this.values.isEmpty()) {
            return soulStack.isEmpty();
        } else {
            for (SoulStack value : getSouls()) {
                if (soulStack.getEntity().equals(value.getEntity())) {
                    return true;
                }
            }
            return false;
        }
    }

    public List<SoulStack> getSouls() {
        dissolve();
        return soulStacks;
    }

    public void dissolve() {
        if (this.soulStacks == null) {
            this.soulStacks = this.values.stream().flatMap(either -> either.map(
                    soulStack -> Stream.of(soulStack.soulStack()),
                    tagValue -> tagValue.getSouls().stream()
            )).collect(Collectors.toList());
        }
    }

    public List<Either<EntityValue, TagValue>> getRawValues() {
        return values;
    }

    public interface Value {
        Collection<SoulStack> getSouls();
    }

    public record EntityValue(SoulStack soulStack) implements Value {
        private static final Codec<EntityValue> CODEC = SoulStack.CODEC.orElse(SoulStack.empty())
                .xmap(EntityValue::new, EntityValue::soulStack);

        @Override
        public Collection<SoulStack> getSouls() {
            return Collections.singleton(soulStack);
        }
    }

    public record TagValue(TagKey<EntityType<?>> tag) implements Value {
        public static final Codec<TagValue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                TagKey.codec(BuiltInRegistries.ENTITY_TYPE.key()).fieldOf("tag").forGetter(TagValue::tag)
        ).apply(instance, TagValue::new));

        @Override
        public Collection<SoulStack> getSouls() {
            List<SoulStack> list = Lists.newArrayList();

            for (Holder<EntityType<?>> holder : BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(this.tag)) {
                list.add(new SoulStack(holder.value(), 1));
            }

            return list;
        }
    }
}
