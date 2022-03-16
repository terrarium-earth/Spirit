package me.codexadrian.spirit;

import me.codexadrian.spirit.blocks.soulcage.SoulCageRenderer;
import me.codexadrian.spirit.utils.SoulUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;

public class FabricSpiritClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(FabricSpirit.SOUL_CAGE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(FabricSpirit.BROKEN_SPAWNER, RenderType.cutout());
        BlockEntityRendererRegistry.INSTANCE.register(FabricSpirit.SOUL_CAGE_ENTITY, SoulCageRenderer::new);
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                return new ResourceLocation("spirit_reload");
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {

            }
        });

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
            if (tintIndex == 0) {
                int red = 0xC4;
                int green = 0xFF;
                int blue = 0xFE;
                if (stack.hasTag()) {
                    float percentage = Math.min(stack.getTag().getCompound("StoredEntity").getInt("Souls") / (float) SoulUtils.getMaxSouls(stack), 1f);
                    red -= percentage * 91;
                    green -= percentage * 7;
                    blue += percentage;
                }
                return red << 16 | green << 8 | blue;
            } else return -1;
        }, FabricSpirit.SOUL_CRYSTAL);

        FabricModelPredicateProviderRegistry.register(FabricSpirit.SOUL_CRYSTAL, new ResourceLocation(Constants.MODID, "activation"), (stack, level, entity, seed) -> stack.hasTag() ? getActivation(stack) : 0);
    }

    private static float getActivation(ItemStack stack) {
        Tier tier = SoulUtils.getTier(stack);
        if (tier == null) {
            return 0f;
        }

        return ((float) tier.getRequiredSouls()) / SoulUtils.getMaxSouls(stack);
    }
}
