package earth.terrarium.spirit.common.handlers.forge;

import earth.terrarium.spirit.common.config.SpiritConfig;
import earth.terrarium.spirit.common.item.trinkets.PetrificationCharm;
import earth.terrarium.spirit.common.registry.SpiritItems;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class SoulAbsorptionHandlerImpl {
    public static int getPetrificationBonus(LivingEntity victim, Player player) {
        LazyOptional<ICuriosItemHandler> component = CuriosApi.getCuriosInventory(player);
        if (component.isPresent()) {
            ICuriosItemHandler trinketComponent = component.orElse(null);
            List<SlotResult> equipped = trinketComponent.findCurios(SpiritItems.PETRIFICATION_CRYSTAL.get());
            if (equipped.isEmpty()) return 0;

            Optional<SlotResult> max = equipped.stream().max(Comparator.comparingInt(o -> PetrificationCharm.getAttunementBonus(o.stack(), victim.getType())));

            return max.map(slotResult -> PetrificationCharm.getAttunementBonus(slotResult.stack(), victim.getType())).orElse(0);
        }
        return 0;
    }
}
