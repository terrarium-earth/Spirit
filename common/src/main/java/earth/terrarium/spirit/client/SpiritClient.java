package earth.terrarium.spirit.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.client.renderer.armor.ArmorRenderers;
import earth.terrarium.spirit.client.renderer.armor.SoulSteelArmorModel;
import earth.terrarium.spirit.client.renderer.block.PedestalRenderer;
import earth.terrarium.spirit.client.renderer.block.SoulBasinRenderer;
import earth.terrarium.spirit.common.handlers.MobCrystalHandler;
import earth.terrarium.spirit.common.handlers.SoulArmorHandler;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritBlocks;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class SpiritClient {
    public static final ItemColor ARMOR_COLOR = (itemStack, i) -> {
        for (int j = 1; j <= 3; j++) {
            if (itemStack.getItem() instanceof SoulSteelArmor armor && i == j) {
                return armor.getAbility(itemStack).getColor().asArray()[j - 1];
            }
        }
        return -1;
    };

    public static void init() {
        registerItemProperties(SpiritItems.MOB_CRYSTAL.get(), new ResourceLocation(Spirit.MODID, "mob"), (itemStack, clientLevel, livingEntity, i) -> MobCrystalHandler.mobCrystalProperty(itemStack));
        registerItemProperties(SpiritItems.SOUL_STEEL_HELMET.get(), new ResourceLocation(Spirit.MODID, "ability"), (itemStack, clientLevel, livingEntity, i) -> SoulArmorHandler.soulArmorProperty(itemStack));
        registerItemProperties(SpiritItems.SOUL_STEEL_CHESTPLATE.get(), new ResourceLocation(Spirit.MODID, "ability"), (itemStack, clientLevel, livingEntity, i) -> SoulArmorHandler.soulArmorProperty(itemStack));
        registerItemProperties(SpiritItems.SOUL_STEEL_LEGGINGS.get(), new ResourceLocation(Spirit.MODID, "ability"), (itemStack, clientLevel, livingEntity, i) -> SoulArmorHandler.soulArmorProperty(itemStack));
        registerItemProperties(SpiritItems.SOUL_STEEL_BOOTS.get(), new ResourceLocation(Spirit.MODID, "ability"), (itemStack, clientLevel, livingEntity, i) -> SoulArmorHandler.soulArmorProperty(itemStack));
        registerBlockEntityRenderers(SpiritBlockEntities.PEDESTAL.get(), PedestalRenderer::new);
        registerBlockEntityRenderers(SpiritBlockEntities.SOUL_BASIN.get(), SoulBasinRenderer::new);
        registerRenderLayer(SpiritBlocks.SOUL_GLASS.get(), RenderType.translucent());
        registerRenderLayer(SpiritBlocks.RAGING_SOUL_FIRE.get(), RenderType.cutout());
        registerRenderLayer(SpiritBlocks.INFUSION_PEDESTAL.get(), RenderType.cutout());
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
