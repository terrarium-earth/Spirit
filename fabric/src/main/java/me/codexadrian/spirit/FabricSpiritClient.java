package me.codexadrian.spirit;

import com.mojang.blaze3d.platform.InputConstants;
import me.codexadrian.spirit.network.NetworkHandler;
import me.codexadrian.spirit.network.messages.ToggleEmpoweredPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class FabricSpiritClient implements ClientModInitializer {

    private static KeyMapping keyBinding;

    @Override
    public void onInitializeClient() {
        SpiritClient.initClient();
        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.spirit.toggle", // The translation key of the keybinding's name
                InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_V, // The keycode of the key
                "category.spirit.soul_steel_tools" // The translation key of the keybinding's category.
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.consumeClick()) {
                NetworkHandler.sendToServer(new ToggleEmpoweredPacket());
            }
        });
    }
}
