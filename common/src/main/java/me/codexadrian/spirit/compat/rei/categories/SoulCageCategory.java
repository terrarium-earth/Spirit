package me.codexadrian.spirit.compat.rei.categories;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.compat.rei.displays.TierDisplay;
import me.codexadrian.spirit.data.Tier;
import me.codexadrian.spirit.registry.SpiritBlocks;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SoulCageCategory implements DisplayCategory<TierDisplay> {
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "soul_cage_tier");
    public static final CategoryIdentifier<TierDisplay> RECIPE = CategoryIdentifier.of(ID);

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(SpiritBlocks.SOUL_CAGE.get().asItem().getDefaultInstance());
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("spirit.jei.soul_cage_tier.title");
    }

    @Override
    public CategoryIdentifier<? extends TierDisplay> getCategoryIdentifier() {
        return RECIPE;
    }

    @Override
    public int getDisplayWidth(TierDisplay display) {
        return 170 + 8;
    }

    @Override
    public int getDisplayHeight() {
        return 103 + 8;
    }

    @Override
    public List<Widget> setupDisplay(TierDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createDrawableWidget((helper, stack, mouseX, mouseY, delta) -> {
            stack.pushPose();
            stack.translate(bounds.x + 4, bounds.y + 4, 0);
            Tier recipe = display.tier();
            Font font = Minecraft.getInstance().font;
            font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.tier_prefix", new TranslatableComponent(recipe.displayName())), 5, 5, 0x00a8ba);
            font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.required_souls", recipe.requiredSouls()), 5, 17, 0x373737);
            font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.spawn_delay", recipe.minSpawnDelay(), recipe.maxSpawnDelay()), 5, 29, 0x373737);
            font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.spawn_count", recipe.spawnCount()), 5, 41, 0x373737);
            font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.spawn_range", recipe.spawnRange()), 5, 53, 0x373737);
            if (recipe.nearbyRange() == -1) font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.player_nearby_not_required"), 5, 65, 0x373737);
            else font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.player_nearby", recipe.nearbyRange()), 5, 65, 0x373737);
            if (recipe.redstoneControlled()) font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.redstone_controlled_true"), 5, 77, 0x373737);
            else font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.redstone_controlled_false"), 5, 77, 0x373737);
            if (recipe.redstoneControlled())
                font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.ignored_spawn_conditions_true"), 5, 89, 0x373737);
            else font.draw(stack, new TranslatableComponent("spirit.jei.soul_cage_info.ignored_spawn_conditions_false"), 5, 89, 0x373737);
            stack.popPose();
        }));

        return widgets;
    }
}
