package me.codexadrian.spirit.compat.forge.tinkers.data;

import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;

public class SpiritMaterialTraits extends AbstractMaterialTraitDataProvider {
    public SpiritMaterialTraits(DataGenerator gen, AbstractMaterialDataProvider materials) {
        super(gen, materials);
    }

    @Override
    protected void addMaterialTraits() {
        addDefaultTraits(SpiritMaterials.SOUL_STEEL, SpiritModifiers.SOUL_REAPER);
    }

    @Override
    public String getName() {
        return "Spirit Material Traits";
    }
}
