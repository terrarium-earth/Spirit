package me.codexadrian.spirit.network.packet;

import net.minecraft.resources.ResourceLocation;

public interface IPacket<T> {
    ResourceLocation getID();
    IPacketHandler<T> getHandler();
}
