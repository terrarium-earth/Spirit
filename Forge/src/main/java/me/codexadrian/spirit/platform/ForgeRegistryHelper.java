package me.codexadrian.spirit.platform;

import me.codexadrian.spirit.ForgeSpirit;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlock;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlockEntity;
import me.codexadrian.spirit.items.DivineCrystalItem;
import me.codexadrian.spirit.platform.services.IRegistryHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ForgeRegistryHelper implements IRegistryHelper {

    @Override
    public DivineCrystalItem getSoulCrystal() {
        return ForgeSpirit.SOUL_CRYSTAL.get();
    }

    @Override
    public BlockItem getBrokenSpawner() {
        return ForgeSpirit.BROKEN_SPAWNER_ITEM.get();
    }

    @Override
    public SoulCageBlock getSoulCage() {
        return ForgeSpirit.SOUL_CAGE.get();
    }

    @Override
    public BlockEntityType<SoulCageBlockEntity> getSoulCageBlockEntity() {
        return ForgeSpirit.SOUL_CAGE_ENTITY.get();
    }

    @Override
    public BlockItem getSoulCageItem() {
        return ForgeSpirit.SOUL_CAGE_ITEM.get();
    }
}
