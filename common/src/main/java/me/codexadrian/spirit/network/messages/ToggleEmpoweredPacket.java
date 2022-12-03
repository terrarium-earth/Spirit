package me.codexadrian.spirit.network.messages;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.network.packet.IPacket;
import me.codexadrian.spirit.network.packet.IPacketHandler;
import me.codexadrian.spirit.registry.SpiritItems;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public record ToggleEmpoweredPacket() implements IPacket<ToggleEmpoweredPacket> {
    public static Handler HANDLER = new Handler();
    public static final ResourceLocation ID = new ResourceLocation(Spirit.MODID, "empower");

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public IPacketHandler<ToggleEmpoweredPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements IPacketHandler<ToggleEmpoweredPacket> {

        @Override
        public void encode(ToggleEmpoweredPacket message, FriendlyByteBuf buffer) {

        }

        @Override
        public ToggleEmpoweredPacket decode(FriendlyByteBuf buffer) {
            return new ToggleEmpoweredPacket();
        }

        @Override
        public BiConsumer<MinecraftServer, Player> handle(ToggleEmpoweredPacket message) {
            return (server, player) -> {
                ItemStack stack = player.getMainHandItem();
                if (stack.is(Spirit.SOUL_STEEL_MAINHAND)) {
                    stack.getOrCreateTag().putBoolean("Charged", !stack.getOrCreateTag().getBoolean("Charged"));
                }
            };
        }
    }
}
