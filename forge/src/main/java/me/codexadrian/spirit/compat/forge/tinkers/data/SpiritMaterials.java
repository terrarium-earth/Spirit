package me.codexadrian.spirit.compat.forge.tinkers.data;

import me.codexadrian.spirit.Spirit;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class SpiritMaterials extends AbstractMaterialDataProvider {
    public static final MaterialId SOUL_STEEL = new MaterialId(new ResourceLocation(Spirit.MODID, "soul_steel"));

    public SpiritMaterials(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addMaterials() {
        addMaterial(SOUL_STEEL, 3, ORDER_NETHER, false);
    }

    @Override
    public String getName() {
        return "Spirit Materials";
    }
}
