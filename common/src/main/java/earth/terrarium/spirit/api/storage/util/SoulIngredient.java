package earth.terrarium.spirit.api.storage.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.teamresourceful.resourcefullib.common.codecs.tags.HolderSetCodec;
import earth.terrarium.spirit.api.utils.SoulStack;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class SoulIngredient implements Predicate<SoulStack> {

    public static final Codec<SoulIngredient> CODEC = Codec.either(HolderSetCodec.of(BuiltInRegistries.ENTITY_TYPE), HolderSetCodec.of(BuiltInRegistries.ENTITY_TYPE).listOf())
            .xmap(
                    either -> either.map(SoulIngredient::new, SoulIngredient::new),
                    soulIngredient -> soulIngredient.sets.size() == 1 ? Either.left(soulIngredient.sets.get(0)) : Either.right(soulIngredient.sets)
            );

    private final List<HolderSet<EntityType<?>>> sets;
    private List<SoulStack> stacks;

    private SoulIngredient(List<HolderSet<EntityType<?>>> sets) {
        this.sets = sets;
    }

    private SoulIngredient(HolderSet<EntityType<?>> set) {
        this(List.of(set));
    }

    public static SoulIngredient of() {
        return new SoulIngredient(HolderSet.direct());
    }

    public static SoulIngredient of(EntityType<?>... entityTypes) {
        return SoulIngredient.of(Arrays.stream(entityTypes).map(SoulStack::of));
    }

    public static SoulIngredient of(SoulStack... soulStacks) {
        return SoulIngredient.of(Arrays.stream(soulStacks));
    }

    public static SoulIngredient of(Stream<SoulStack> stream) {
        return new SoulIngredient(
                HolderSet.direct(
                        EntityType::builtInRegistryHolder,
                        stream.filter(Predicate.not(SoulStack::isEmpty)).map(SoulStack::getEntity).toList()
                )
        );
    }

    public static SoulIngredient of(TagKey<EntityType<?>> tag) {
        return BuiltInRegistries.ENTITY_TYPE.getTag(tag)
                .map(SoulIngredient::new)
                .orElseGet(SoulIngredient::of);
    }

    @Override
    public boolean test(SoulStack soulStack) {
        if (isEmpty() && soulStack.isEmpty()) {
            return true;
        }
        return contains(soulStack.getEntity());
    }

    public boolean isEmpty() {
        if (this.sets.size() == 0) {
            return true;
        }
        return this.sets.stream().allMatch(set -> set.size() == 0);
    }

    public boolean contains(EntityType<?> entityType) {
        if(entityType == null) return false;

        for (HolderSet<EntityType<?>> set : this.sets) {
            if (set.contains(entityType.builtInRegistryHolder())) {
                return true;
            }
        }
        return false;
    }

    public Collection<SoulStack> getStacks() {
        if (stacks == null) {
            stacks = sets.stream()
                    .flatMap(HolderSet::stream)
                    .filter(Holder::isBound)
                    .map(Holder::value)
                    .map(SoulStack::of)
                    .filter(Predicate.not(SoulStack::isEmpty))
                    .toList();
        }
        return stacks;
    }

    public Stream<EntityType<?>> getEntities() {
        return getStacks().stream().map(SoulStack::getEntity);
    }
}