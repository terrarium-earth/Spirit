package me.codexadrian.spirit.platform;

import me.codexadrian.spirit.platform.services.IRegistryHelper;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

import static me.codexadrian.spirit.Spirit.MODID;

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
    public <T extends Enchantment> Supplier<T> registerEnchantment(String id, Supplier<T> enchantment) {
        var register = Registry.register(Registry.ENCHANTMENT, new ResourceLocation(MODID, id), enchantment.get());
        return () -> register;
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> factory, MobCategory group, float width, float height) {
        var register = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(MODID, name), FabricEntityTypeBuilder.create(group, factory).dimensions(EntityDimensions.fixed(width, height)).build());
        return () -> register;
    }

    @Override
    public <R extends Recipe<?>, T extends RecipeType<R>> Supplier<T> registerRecipeType(String name, Supplier<T> recipe) {
        var register = Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(MODID, name), recipe.get());
        return () -> register;
    }

    @Override
    public <R extends Recipe<?>, T extends RecipeSerializer<R>> Supplier<T> registerRecipeSerializer(String name, Supplier<T> recipe) {
        var register = Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(MODID, name), recipe.get());
        return () -> register;
    }

    @Override
    public CreativeModeTab registerCreativeTab(ResourceLocation tab, Supplier<ItemStack> supplier) {
        return FabricItemGroupBuilder.build(tab, supplier);
    }


}
