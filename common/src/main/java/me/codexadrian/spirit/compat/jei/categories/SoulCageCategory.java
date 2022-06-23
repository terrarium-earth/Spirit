package me.codexadrian.spirit.compat.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.registry.SpiritBlocks;
import me.codexadrian.spirit.registry.SpiritItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class SoulCageCategory extends BaseCategory<Tier> {
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "soul_cage_tier");
    public static final RecipeType<Tier> RECIPE = new RecipeType<>(ID, Tier.class);
    public SoulCageCategory(IGuiHelper guiHelper) {
        super(guiHelper,
                RECIPE,
                Component.translatable("spirit.jei.soul_cage_tier.title"),
                guiHelper.createBlankDrawable(170,103),
                guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, SpiritBlocks.SOUL_CAGE.get().asItem().getDefaultInstance()));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Tier recipe, IFocusGroup focuses) {
        builder.addInvisibleIngredients(RecipeIngredientRole.CATALYST).addIngredients(Ingredient.of(SpiritBlocks.SOUL_CAGE.get()));
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(Ingredient.of(SpiritItems.SOUL_CRYSTAL.get()));
    }

    @Override
    public void draw(Tier recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        Font font = Minecraft.getInstance().font;
        font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.tier_prefix", Component.translatable(recipe.displayName())), 5, 5, 0x00a8ba);
        font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.required_souls", recipe.requiredSouls()), 5, 17, 0x373737);
        font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.spawn_delay", recipe.minSpawnDelay(), recipe.maxSpawnDelay()), 5, 29, 0x373737);
        font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.spawn_count", recipe.spawnCount()), 5, 41, 0x373737);
        font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.spawn_range", recipe.spawnRange()), 5, 53, 0x373737);
        if(recipe.nearbyRange() == -1) font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.player_nearby_not_required"), 5, 65, 0x373737);
        else font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.player_nearby", recipe.nearbyRange()), 5, 65, 0x373737);
        if(recipe.redstoneControlled()) font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.redstone_controlled_true"), 5, 77, 0x373737);
        else font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.redstone_controlled_false"), 5, 77, 0x373737);
        if(recipe.redstoneControlled()) font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.ignored_spawn_conditions_true"), 5, 89, 0x373737);
        else font.draw(stack, Component.translatable("spirit.jei.soul_cage_info.ignored_spawn_conditions_false"), 5, 89, 0x373737);
    }
}
