package earth.terrarium.spirit.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.util.KeybindUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record JumpKeyPacket(boolean pressed) implements Packet<JumpKeyPacket> {
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "keybind_packet");
    public static final Handler HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<JumpKeyPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<JumpKeyPacket> {
        @Override
        public void encode(JumpKeyPacket packet, FriendlyByteBuf buf) {
            buf.writeBoolean(packet.pressed);
        }

        @Override
        public JumpKeyPacket decode(FriendlyByteBuf buf) {
            return new JumpKeyPacket(buf.readBoolean());
        }

        @Override
        public PacketContext handle(JumpKeyPacket packet) {
            return (player, level) -> KeybindUtils.setJumpKeyDown(player, packet.pressed());
        }
    }
}
