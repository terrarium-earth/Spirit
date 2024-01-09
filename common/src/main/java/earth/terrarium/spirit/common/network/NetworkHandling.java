package earth.terrarium.spirit.common.network;

import com.teamresourceful.resourcefullib.common.networking.NetworkChannel;
import com.teamresourceful.resourcefullib.common.networking.base.NetworkDirection;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.network.messages.JumpKeyPacket;

public class NetworkHandling {
    public static final NetworkChannel CHANNEL = new NetworkChannel(Spirit.MODID, 0, "main");

    public static void init() {
        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, JumpKeyPacket.ID, JumpKeyPacket.HANDLER, JumpKeyPacket.class);
    }
}
