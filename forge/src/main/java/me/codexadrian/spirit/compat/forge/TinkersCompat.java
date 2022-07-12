package me.codexadrian.spirit.compat.forge;

import me.codexadrian.spirit.compat.forge.tinkers.data.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;

public class TinkersCompat {
    public static void gatherData(GatherDataEvent event) {
        if (event.includeClient()) {
            DataGenerator generator = event.getGenerator();
            generator.addProvider(new SpiritModifiers(generator));
            SpiritMaterials arg = new SpiritMaterials(generator);
            generator.addProvider(arg);
            SpiritMaterialSprites spiritMaterialSprites = new SpiritMaterialSprites();
            generator.addProvider(new SpiritMaterialTraits(generator, arg));
            generator.addProvider(new SpiritMaterialStats(generator, arg));
            generator.addProvider(new MaterialPartTextureGenerator(generator, event.getExistingFileHelper(), new TinkerPartSpriteProvider(), spiritMaterialSprites));
        }
    }

    public static int tinkersSoulReaperBenefit(Player player) {
        ItemStack stack = player.getMainHandItem();
        if(stack.is(TinkerTags.Items.MELEE)) {
            ToolStack tool = ToolStack.from(stack);
            for (ModifierEntry modifierEntry : tool.getModifierList()) {
                if(modifierEntry.getId().equals(SpiritModifiers.SOUL_REAPER)) {
                    return modifierEntry.getLevel();
                }
            }
        }
        return 0;
    }
}
