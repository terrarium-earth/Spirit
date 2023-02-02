package earth.terrarium.spirit.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.client.renderer.block.PedestalRenderer;
import earth.terrarium.spirit.client.renderer.block.SoulBasinRenderer;
import earth.terrarium.spirit.client.renderer.block.SoulCageRenderer;
import earth.terrarium.spirit.client.renderer.block.SummoningPedestalRenderer;
import earth.terrarium.spirit.common.handlers.MobCrystalHandler;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SpiritClient {
    public static void init() {
        registerItemProperties(SpiritItems.MOB_CRYSTAL.get(), new ResourceLocation(Spirit.MODID, "mob"), (itemStack, clientLevel, livingEntity, i) -> MobCrystalHandler.mobCrystalProperty(itemStack));
        registerBlockEntityRenderers(SpiritBlockEntities.SOUL_CAGE.get(), SoulCageRenderer::new);
        registerBlockEntityRenderers(SpiritBlockEntities.PEDESTAL.get(), PedestalRenderer::new);
        registerBlockEntityRenderers(SpiritBlockEntities.SUMMONING_PEDESTAL.get(), SummoningPedestalRenderer::new);
        registerBlockEntityRenderers(SpiritBlockEntities.SOUL_BASIN.get(), SoulBasinRenderer::new);
        registerRenderLayer(SpiritBlocks.SOUL_CAGE.get(), RenderType.cutout());
        registerRenderLayer(SpiritBlocks.EARTH_FIRE.get(), RenderType.cutout());
        registerRenderLayer(SpiritBlocks.EMBER_FIRE.get(), RenderType.cutout());
        registerRenderLayer(SpiritBlocks.ENDER_FIRE.get(), RenderType.cutout());
        registerRenderLayer(SpiritBlocks.WATER_FIRE.get(), RenderType.cutout());
    }

    @ExpectPlatform
    public static <E extends BlockEntity> void registerBlockEntityRenderers(BlockEntityType<E> blockEntityType, BlockEntityRendererProvider<? super E> blockEntityRendererFactory) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerItemProperties(Item item, ResourceLocation resourceLocation, ClampedItemPropertyFunction clampedItemPropertyFunction) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerRenderLayer(Block block, RenderType type) {
        throw new AssertionError();
    }
}
