package me.codexadrian.spirit.compat.forge.tinkers.data;

import me.codexadrian.spirit.items.SoulMetalMaterial;
import net.minecraft.data.DataGenerator;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.tools.stats.HandleMaterialStats;
import slimeknights.tconstruct.tools.stats.HeadMaterialStats;

public class SpiritMaterialStats extends AbstractMaterialStatsDataProvider {

    public SpiritMaterialStats(DataGenerator gen, AbstractMaterialDataProvider materials) {
        super(gen, materials);
    }

    @Override
    protected void addMaterialStats() {
        addMaterialStats(SpiritMaterials.SOUL_STEEL,
                new HeadMaterialStats(SoulMetalMaterial.INSTANCE.getUses(), SoulMetalMaterial.INSTANCE.getSpeed(), SoulMetalMaterial.INSTANCE, SoulMetalMaterial.INSTANCE.getAttackDamageBonus()),
                new HandleMaterialStats(0.85F, .95F, 1.15F, 1.15F)
                );
    }

    @Override
    public String getName() {
        return "Spirit Material Stats";
    }
}
