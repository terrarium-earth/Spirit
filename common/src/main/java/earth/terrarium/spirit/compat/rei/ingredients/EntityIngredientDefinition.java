package earth.terrarium.spirit.compat.rei.ingredients;

import earth.terrarium.spirit.compat.common.EntityIngredient;
import earth.terrarium.spirit.compat.rei.SpiritPlugin;
import me.shedaniel.rei.api.client.entry.renderer.EntryRenderer;
import me.shedaniel.rei.api.common.entry.EntrySerializer;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ComparisonContext;
import me.shedaniel.rei.api.common.entry.type.EntryDefinition;
import me.shedaniel.rei.api.common.entry.type.EntryType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class EntityIngredientDefinition implements EntryDefinition<EntityIngredient> {
    private final EntityIngredientRenderer renderer = new EntityIngredientRenderer();
    
    @Override
    public Class<EntityIngredient> getValueType() {
        return EntityIngredient.class;
    }
    
    @Override
    public EntryType<EntityIngredient> getType() {
        return SpiritPlugin.ENTITY_INGREDIENT;
    }
    
    @Override
    public EntryRenderer<EntityIngredient> getRenderer() {
        return renderer;
    }
    
    @Override
    @Nullable
    public ResourceLocation getIdentifier(EntryStack<EntityIngredient> entry, EntityIngredient value) {
        return BuiltInRegistries.ENTITY_TYPE.getKey(value.getEntityType());
    }
    
    @Override
    public boolean isEmpty(EntryStack<EntityIngredient> entry, EntityIngredient value) {
        return false;
    }
    
    @Override
    public EntityIngredient copy(EntryStack<EntityIngredient> entry, EntityIngredient value) {
        return new EntityIngredient(value.getEntityType(), value.getRotation(), value.getNbt());
    }
    
    @Override
    public EntityIngredient normalize(EntryStack<EntityIngredient> entry, EntityIngredient value) {
        return new EntityIngredient(value.getEntityType(), value.getRotation());
    }
    
    @Override
    public EntityIngredient wildcard(EntryStack<EntityIngredient> entry, EntityIngredient value) {
        return new EntityIngredient(value.getEntityType(), value.getRotation());
    }
    
    @Override
    public long hash(EntryStack<EntityIngredient> entry, EntityIngredient value, ComparisonContext context) {
        int code = BuiltInRegistries.ENTITY_TYPE.getKey(value.getEntityType()).hashCode();
        return (code * 31L + Float.hashCode(value.getRotation())) * 31L + value.getNbt().hashCode();
    }
    
    @Override
    public boolean equals(EntityIngredient o1, EntityIngredient o2, ComparisonContext context) {
        return o1.getEntityType() == o2.getEntityType() && o1.getRotation() == o2.getRotation() && o1.getNbt().equals(o2.getNbt());
    }
    
    @Override
    @Nullable
    public EntrySerializer<EntityIngredient> getSerializer() {
        return null;
    }
    
    @Override
    public Component asFormattedText(EntryStack<EntityIngredient> entry, EntityIngredient value) {
        return value.getDisplayName();
    }
    
    @Override
    public Stream<? extends TagKey<?>> getTagsFor(EntryStack<EntityIngredient> entry, EntityIngredient value) {
        return BuiltInRegistries.ENTITY_TYPE.getHolder(ResourceKey.create(BuiltInRegistries.ENTITY_TYPE.key(), BuiltInRegistries.ENTITY_TYPE.getKey(value.getEntityType())))
                .map(Holder::tags).orElse(Stream.empty());
    }
}
