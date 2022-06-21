package me.codexadrian.spirit.network.forge;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.network.packet.IPacket;
import me.codexadrian.spirit.network.packet.IPacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandlerImpl {
    private static int id = 0;
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Spirit.MODID, "main_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static <T extends IPacket<T>> void sendToServer(T packet) {
        INSTANCE.sendToServer(packet);
    }

    public static <T> void registerClientToServerPacket(ResourceLocation location, IPacketHandler<T> handler, Class<T> tClass) {
        INSTANCE.registerMessage(++id, tClass, handler::encode, handler::decode, (t, context) -> {
            ServerPlayer sender = context.get().getSender();
            if(sender != null) {
                context.get().enqueueWork(() -> handler.handle(t).accept(sender.server, sender));
            }
            context.get().setPacketHandled(true);
        });
    }
}
