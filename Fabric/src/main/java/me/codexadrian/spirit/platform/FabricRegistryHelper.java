package me.codexadrian.spirit.platform;

import me.codexadrian.spirit.FabricSpirit;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlock;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlockEntity;
import me.codexadrian.spirit.items.DivineCrystalItem;
import me.codexadrian.spirit.platform.services.IRegistryHelper;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

import static me.codexadrian.spirit.Constants.MODID;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        var register = Registry.register(Registry.ITEM, new ResourceLocation(MODID, id), item.get());
        return () -> register;
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String id, Supplier<T> item) {
        var register = Registry.register(Registry.BLOCK, new ResourceLocation(MODID, id), item.get());
        return () -> register;
    }

    @Override
    public <E extends BlockEntity, T extends BlockEntityType<E>> Supplier<T> registerBlockEntity(String id, Supplier<T> item) {
        var register = Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(MODID, id), item.get());
        return () -> register;
    }

    @Override
    public <E extends BlockEntity> BlockEntityType<E> createBlockEntityType(BlockEntityFactory<E> factory, Block... blocks) {
        return FabricBlockEntityTypeBuilder.create(factory::create, blocks).build();
    }

    @Override
    public CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> supplier) {
        return FabricItemGroupBuilder.build(tab, supplier);
    }


}
