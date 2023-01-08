package earth.terrarium.spirit.client.fabric;

import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.client.renderer.block.SoulCageRenderer;
import earth.terrarium.spirit.common.blockentity.SoulCageBlockEntity;
import earth.terrarium.spirit.common.handlers.MobCrystalHandler;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;

public class SpiritClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemProperties.register(SpiritItems.MOB_CRYSTAL.get(), new ResourceLocation(Spirit.MODID, "mob"), (itemStack, clientLevel, livingEntity, i) -> MobCrystalHandler.mobCrystalProperty(itemStack));
        BlockEntityRendererRegistry.register(SpiritBlockEntities.SOUL_CAGE.get(), SoulCageRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(SpiritBlocks.SOUL_CAGE.get(), RenderType.cutout());
    }
}
