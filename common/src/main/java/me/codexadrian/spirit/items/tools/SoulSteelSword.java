package me.codexadrian.spirit.items.tools;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.items.SoulMetalMaterial;
import me.codexadrian.spirit.utils.ToolUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SoulSteelSword extends SwordItem {
    public SoulSteelSword(Properties properties) {
        super(SoulMetalMaterial.INSTANCE, 3, -2.4F, properties);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return Spirit.SOUL_COLOR;
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity victim, @NotNull LivingEntity attacker) {
        if(attacker instanceof Player player) ToolUtils.handleOnHitEntity(itemStack, ToolType.SWORD, victim, player);
        return super.hurtEnemy(itemStack, victim, attacker);
    }
}
