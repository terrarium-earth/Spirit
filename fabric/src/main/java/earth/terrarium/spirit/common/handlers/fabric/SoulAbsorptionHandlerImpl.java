package earth.terrarium.spirit.common.handlers.fabric;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import earth.terrarium.spirit.common.config.SpiritConfig;
import earth.terrarium.spirit.common.item.trinkets.PetrificationCharm;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SoulAbsorptionHandlerImpl {
    public static int getPetrificationBonus(LivingEntity victim, Player player) {
        Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(player);
        if (component.isPresent()) {
            TrinketComponent trinketComponent = component.get();
            List<Tuple<SlotReference, ItemStack>> equipped = trinketComponent.getEquipped(SpiritItems.PETRIFICATION_CRYSTAL.get());
            if (equipped.isEmpty()) return 0;

            Optional<Tuple<SlotReference, ItemStack>> max = equipped.stream().max(Comparator.comparingInt(o -> PetrificationCharm.getAttunementBonus(o.getB(), victim.getType())));

            return max.map(slotResult -> PetrificationCharm.getAttunementBonus(slotResult.getB(), victim.getType())).orElse(0);
        }
        return 0;
    }
}
