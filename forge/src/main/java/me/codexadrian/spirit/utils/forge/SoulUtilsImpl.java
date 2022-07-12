package me.codexadrian.spirit.utils.forge;

import me.codexadrian.spirit.compat.forge.TinkersCompat;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

public class SoulUtilsImpl {
    public static ItemStack searchTrinkets(Player player, @Nullable LivingEntity victim) {
        return ItemStack.EMPTY;
    }

    public static int tinkersAmount(Player player) {
        if(ModList.get().isLoaded("tconstruct")) {
            return TinkersCompat.tinkersSoulReaperBenefit(player);
        } else return 0;
    }
}
