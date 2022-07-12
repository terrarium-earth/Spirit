package me.codexadrian.spirit.compat.forge.tinkers.data;

import me.codexadrian.spirit.Spirit;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;

public class SpiritRenderInfo extends AbstractMaterialRenderInfoProvider {
    public SpiritRenderInfo(DataGenerator gen, @Nullable AbstractMaterialSpriteProvider materialSprites) {
        super(gen, materialSprites);
    }

    @Override
    protected void addMaterialRenderInfo() {
        buildRenderInfo(SpiritMaterials.SOUL_STEEL, new ResourceLocation(Spirit.MODID, "item/soul_steel_ingot.png")).color(0xFF234987);
    }

    @Override
    public String getName() {
        return null;
    }
}
