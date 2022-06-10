package me.codexadrian.spirit;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = "spirit", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeSpiritClient {

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        Minecraft.getInstance().getItemColors().register((stack, tintIndex) -> {
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
        }, SpiritRegistry.SOUL_CRYSTAL.get());
        SpiritClient.initClient();
    }

    @SubscribeEvent
    public static void onShadersRegistered(RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(
                event.getResourceManager(),
                new ResourceLocation("rendertype_entity_corrupted"),
                DefaultVertexFormat.BLOCK
        ), shader -> ForgeSoulShader.rendertypeTranslucentShader = shader);
    }
}
