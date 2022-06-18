package me.codexadrian.spirit.compat.jei.ingredients;

import me.codexadrian.spirit.compat.jei.SpiritPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
/**
 * This class was largely inspired by or taken from the Resourceful Bees repository with
 * the expressed permission from one of their developers.
 * @author Team Resourceful
 */
public class EntityIngredientHelper implements IIngredientHelper<EntityIngredient> {

    @Override
    public @NotNull IIngredientType<EntityIngredient> getIngredientType() {
        return SpiritPlugin.ENTITY_INGREDIENT;
    }


    @NotNull
    @Override
    public String getDisplayName(EntityIngredient entityIngredient) {
        return I18n.get(entityIngredient.getEntityType().getDescriptionId());
    }

    @Override
    public @NotNull String getUniqueId(@NotNull EntityIngredient entityIngredient, @NotNull UidContext context) {
        Entity entity = entityIngredient.getEntity();
        if (entity == null) return "spirit_entity:error";
        ResourceLocation id = EntityType.getKey(entity.getType());
        return id == null ? "spirit_entity:error" : "spirit_entity:" + id;
    }

    @NotNull
    @Override
    public ItemStack getCheatItemStack(EntityIngredient ingredient) {
        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public String getWildcardId(@NotNull EntityIngredient entityIngredient) {
        return this.getUniqueId(entityIngredient, UidContext.Ingredient);
    }

    @Override
    public @NotNull ResourceLocation getResourceLocation(EntityIngredient ingredient) {
        ResourceLocation id = Registry.ENTITY_TYPE.getKey(ingredient.getEntityType());
        if (id == null) return new ResourceLocation("error");
        return id;
    }

    @NotNull
    @Override
    public EntityIngredient copyIngredient(@NotNull EntityIngredient entityIngredient) {
        return entityIngredient;
    }

    @NotNull
    @Override
    public String getErrorInfo(@Nullable EntityIngredient entityIngredient) {
        return entityIngredient == null ? "null" : entityIngredient.toString();
    }
}