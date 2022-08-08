package me.codexadrian.spirit.forge;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import me.codexadrian.spirit.SpiritClient;
import me.codexadrian.spirit.client.CrudeSoulEntityModel;
import me.codexadrian.spirit.network.NetworkHandler;
import me.codexadrian.spirit.network.messages.ToggleEmpoweredPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = "spirit", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeSpiritClient {

    public ForgeSpiritClient() {
        MinecraftForge.EVENT_BUS.addListener(this::keybindAction);
    }

    private static final KeyMapping EMPOWER_KEYBIND = new KeyMapping(
            "key.spirit.toggle", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_V, // The keycode of the key
            "category.spirit.keybinds" // The translation key of the keybinding's category.
    );

    @SubscribeEvent
    public static void onInitializeClient(FMLClientSetupEvent event) {
        SpiritClient.initClient();
    }

    @SubscribeEvent
    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(EMPOWER_KEYBIND);
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

    public void keybindAction(InputEvent.Key event) {
        while (event.getKey() == EMPOWER_KEYBIND.getKey().getValue()) {
            NetworkHandler.sendToServer(new ToggleEmpoweredPacket());
        }
    }
}