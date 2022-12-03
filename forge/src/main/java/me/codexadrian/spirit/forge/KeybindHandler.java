package me.codexadrian.spirit.forge;

import com.mojang.blaze3d.platform.InputConstants;
import me.codexadrian.spirit.network.NetworkHandler;
import me.codexadrian.spirit.network.messages.ToggleEmpoweredPacket;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

public class KeybindHandler {

    private static final KeyMapping EMPOWER_KEYBIND = new KeyMapping(
            "key.spirit.toggle", // The translation key of the keybinding's name
            InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
            GLFW.GLFW_KEY_V, // The keycode of the key
            "category.spirit.keybinds" // The translation key of the keybinding's category.
    );

    public static void registerKeyBinding(RegisterKeyMappingsEvent event) {
        event.register(EMPOWER_KEYBIND);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
            while (EMPOWER_KEYBIND.consumeClick()) {
                NetworkHandler.sendToServer(new ToggleEmpoweredPacket());
            }
        }
    }
}
