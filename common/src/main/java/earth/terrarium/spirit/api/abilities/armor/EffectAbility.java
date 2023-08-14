package earth.terrarium.spirit.api.abilities.armor;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class EffectAbility implements ArmorAbility {
    private final MobEffectInstance effect;
    public EffectAbility(MobEffectInstance effect) {
        this.effect = effect;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (level.isClientSide) return;
        player.addEffect(new MobEffectInstance(effect));
    }
}
