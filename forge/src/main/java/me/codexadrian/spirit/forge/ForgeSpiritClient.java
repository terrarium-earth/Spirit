package me.codexadrian.spirit.forge;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import me.codexadrian.spirit.SpiritClient;
import me.codexadrian.spirit.client.CrudeSoulEntityModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "spirit", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeSpiritClient {

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        SpiritClient.initClient();
        MinecraftForge.EVENT_BUS.register(new KeybindHandler());
    }

    @SubscribeEvent
    public static void onShadersRegistered(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(
                event.getResourceManager(),
                new ResourceLocation("rendertype_entity_corrupted"),
                DefaultVertexFormat.BLOCK
        ), shader -> ForgeSoulShader.rendertypeTranslucentShader = shader);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CrudeSoulEntityModel.LAYER_LOCATION, CrudeSoulEntityModel::createBodyLayer);
    }
}