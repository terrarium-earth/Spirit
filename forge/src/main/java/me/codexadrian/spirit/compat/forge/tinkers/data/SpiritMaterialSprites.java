package me.codexadrian.spirit.compat.forge.tinkers.data;

import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;

public class SpiritMaterialSprites extends AbstractMaterialSpriteProvider {
    @Override
    protected void addAllMaterials() {
        buildMaterial(SpiritMaterials.SOUL_STEEL).meleeHarvest().fallbacks("metal").colorMapper(
                GreyToColorMapping
                        .builderFromBlack()
                        .addARGB( 63, 0xFF31294a)
                        .addARGB(102, 0xFF293a52)
                        .addARGB(140, 0xFF30556e)
                        .addARGB(178, 0xFF3f6e8d)
                        .addARGB(216, 0xFF598aa9)
                        .addARGB(255, 0xFF89b0c9)
                        .build()
        );
    }

    @Override
    public String getName() {
        return "Spirit Material Sprites";
    }
}
