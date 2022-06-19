package me.codexadrian.spirit.items.tools;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.data.MobTrait;
import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.items.SoulMetalMaterial;
import me.codexadrian.spirit.registry.SpiritItems;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

public class SoulSteelSword extends SwordItem {
    public SoulSteelSword(Properties properties) {
        super(SoulMetalMaterial.INSTANCE, 3, -2.4F, properties);
    }

    @Override
    public int getBarColor(@NotNull ItemStack itemStack) {
        return Spirit.SOUL_COLOR;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity victim, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            ItemStack soulCrystal = SoulUtils.findCrystal(player, null, true, true);
            if (soulCrystal.is(SpiritItems.SOUL_CRYSTAL.get()) && SoulUtils.getSoulsInCrystal(soulCrystal) > 0) {
                if (itemStack.getOrCreateTag().getBoolean("Charged")) {
                    var entityEffect = MobTraitData.getEffectForEntity(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(Objects.requireNonNull(SoulUtils.getSoulCrystalType(soulCrystal)))), attacker.getLevel().getRecipeManager());
                    if (entityEffect.isPresent()) {
                        for (MobTrait<?> trait : entityEffect.get().traits()) {
                            trait.onHitEntity(ToolType.SWORD, attacker, victim);
                        }
                        SoulUtils.deviateSoulCount(soulCrystal, -1, attacker.level, null);
                    }
                }
            }
        }
        return super.hurtEnemy(itemStack, victim, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        ItemStack crystal = SoulUtils.findCrystal(player, null, true);
        if (player.getAbilities().instabuild || (!crystal.isEmpty() && SoulUtils.getSoulsInCrystal(crystal) > 0)) {
            player.startUsingItem(interactionHand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int drawTime) {
        if (livingEntity instanceof Player player) {
            ItemStack soulCrystal = SoulUtils.findCrystal(player, null, true, true);
            if (drawTime < 30) {
                return;
            }
            if (soulCrystal.is(SpiritItems.SOUL_CRYSTAL.get())) {
                itemStack.getOrCreateTag().putBoolean("Charged", !itemStack.getOrCreateTag().getBoolean("Charged"));
            } else {
                itemStack.getOrCreateTag().putBoolean("Charged", false);
            }
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 72000;
    }
}
