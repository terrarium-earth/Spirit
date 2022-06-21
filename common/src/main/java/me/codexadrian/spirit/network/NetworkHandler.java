package me.codexadrian.spirit.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import me.codexadrian.spirit.network.messages.ToggleEmpoweredPacket;
import me.codexadrian.spirit.network.packet.IPacket;
import me.codexadrian.spirit.network.packet.IPacketHandler;
import net.minecraft.resources.ResourceLocation;

public class NetworkHandler {

    @ExpectPlatform
    public static <T extends IPacket<T>> void sendToServer(T packet) {
    }

    @ExpectPlatform
    public static <T> void registerClientToServerPacket(ResourceLocation location, IPacketHandler<T> handler, Class<T> tClass) {
    }

    public static void register() {
        registerClientToServerPacket(ToggleEmpoweredPacket.ID, ToggleEmpoweredPacket.HANDLER, ToggleEmpoweredPacket.class);
    }
}
