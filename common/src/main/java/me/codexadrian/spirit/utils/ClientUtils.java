package me.codexadrian.spirit.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ClientUtils {
    public static boolean isItemInHand(ItemStack stack) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player != null) {
            return player.getMainHandItem().equals(stack) || player.getOffhandItem().equals(stack);
        }
        return false;
    }
}
