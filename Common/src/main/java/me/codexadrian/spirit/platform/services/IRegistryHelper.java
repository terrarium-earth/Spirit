package me.codexadrian.spirit.platform.services;

import me.codexadrian.spirit.blocks.soulcage.SoulCageBlock;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlockEntity;
import me.codexadrian.spirit.items.DivineCrystalItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IRegistryHelper {

    DivineCrystalItem getSoulCrystal();

    BlockItem getBrokenSpawner();

    SoulCageBlock getSoulCage();

    BlockEntityType<SoulCageBlockEntity> getSoulCageBlockEntity();

    BlockItem getSoulCageItem();
}
