package me.codexadrian.spirit.platform;

import me.codexadrian.spirit.FabricSpirit;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlock;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlockEntity;
import me.codexadrian.spirit.items.DivineCrystalItem;
import me.codexadrian.spirit.platform.services.IRegistryHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public DivineCrystalItem getSoulCrystal() {
        return FabricSpirit.SOUL_CRYSTAL;
    }

    @Override
    public BlockItem getBrokenSpawner() {
        return FabricSpirit.BROKEN_SPAWNER_ITEM;
    }

    @Override
    public SoulCageBlock getSoulCage() {
        return FabricSpirit.SOUL_CAGE;
    }

    @Override
    public BlockEntityType<SoulCageBlockEntity> getSoulCageBlockEntity() {
        return FabricSpirit.SOUL_CAGE_ENTITY;
    }

    @Override
    public BlockItem getSoulCageItem() {
        return FabricSpirit.SOUL_CAGE_ITEM;
    }
}
