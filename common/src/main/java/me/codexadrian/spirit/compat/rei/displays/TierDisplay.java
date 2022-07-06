package me.codexadrian.spirit.compat.rei.displays;

import com.google.common.base.Suppliers;
import me.codexadrian.spirit.compat.rei.categories.SoulCageCategory;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.registry.SpiritBlocks;
import me.codexadrian.spirit.registry.SpiritItems;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.List;
import java.util.function.Supplier;

public class TierDisplay implements Display {
    private static final Supplier<List<EntryIngredient>> INPUT = Suppliers.memoize(() -> {
        return List.of(EntryIngredient.of(EntryStacks.of(SpiritItems.SOUL_CRYSTAL.get())),
                EntryIngredient.of(EntryStacks.of(SpiritBlocks.SOUL_CAGE.get())));
    });
    private static final Supplier<List<EntryIngredient>> REQUIRED_INPUT = Suppliers.memoize(() -> {
        return List.of(EntryIngredient.of(EntryStacks.of(SpiritItems.SOUL_CRYSTAL.get())));
    });
    private final Tier tier;
    
    public TierDisplay(Tier tier) {
        this.tier = tier;
    }
    
    public Tier tier() {
        return tier;
    }
    
    @Override
    public List<EntryIngredient> getInputEntries() {
        return INPUT.get();
    }
    
    @Override
    public List<EntryIngredient> getRequiredEntries() {
        return REQUIRED_INPUT.get();
    }
    
    @Override
    public List<EntryIngredient> getOutputEntries() {
        return List.of();
    }
    
    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SoulCageCategory.RECIPE;
    }
}
