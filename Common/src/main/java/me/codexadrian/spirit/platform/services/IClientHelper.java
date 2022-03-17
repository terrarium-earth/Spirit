package me.codexadrian.spirit.platform.services;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface IClientHelper {
    
    void registerItemProperty(Item pItem, ResourceLocation pName, ClampedItemPropertyFunction pProperty);

    <T extends BlockEntity> void registerEntityRenderer(BlockEntityType<T> type, BlockEntityRendererProvider<T> provider);

    void setRenderLayer(Block block, RenderType type);
}
