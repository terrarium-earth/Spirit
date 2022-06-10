package me.codexadrian.spirit.recipe;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class SoulEnguflingRecipeManager extends ArrayList<SoulEngulfingRecipe> {

    private static final SoulEnguflingRecipeManager INSTANCE = new SoulEnguflingRecipeManager();

    public Optional<SoulEngulfingRecipe> getRecipe(ItemStack stack) {
        return this.stream().filter(soulEngulfingRecipe -> soulEngulfingRecipe.input().item().test(stack)).findFirst();
    }

    public void addEntry(SoulEngulfingRecipe recipe) {
        this.add(recipe);
    }

    public static SoulEnguflingRecipeManager getInstance() {
        return INSTANCE;
    }
}
