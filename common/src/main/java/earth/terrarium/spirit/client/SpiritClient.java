package earth.terrarium.spirit.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.client.renderer.armor.ArmorRenderers;
import earth.terrarium.spirit.client.renderer.armor.SoulSteelArmorModel;
import earth.terrarium.spirit.client.renderer.block.PedestalRenderer;
import earth.terrarium.spirit.client.renderer.block.SoulBasinRenderer;
import earth.terrarium.spirit.client.renderer.block.SoulCageRenderer;
import earth.terrarium.spirit.common.handlers.MobCrystalHandler;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class SpiritClient {
    public static void init() {
        registerItemProperties(SpiritItems.MOB_CRYSTAL.get(), new ResourceLocation(Spirit.MODID, "mob"), (itemStack, clientLevel, livingEntity, i) -> MobCrystalHandler.mobCrystalProperty(itemStack));
        registerBlockEntityRenderers(SpiritBlockEntities.SOUL_CAGE.get(), SoulCageRenderer::new);
        registerBlockEntityRenderers(SpiritBlockEntities.PEDESTAL.get(), PedestalRenderer::new);
        registerBlockEntityRenderers(SpiritBlockEntities.SOUL_BASIN.get(), SoulBasinRenderer::new);
        registerRenderLayer(SpiritBlocks.SOUL_CAGE.get(), RenderType.cutout());
        registerRenderLayer(SpiritBlocks.SOUL_GLASS.get(), RenderType.translucent());
        ArmorRenderers.init();
    }

    public static void registerEntityLayers(LayerDefinitionRegistry registry) {
        registry.register(SoulSteelArmorModel.LAYER_LOCATION, SoulSteelArmorModel::createBodyLayer);
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

    public static abstract class LayerDefinitionRegistry {
        public abstract void register(ModelLayerLocation location, Supplier<LayerDefinition> definition);
    }
}
