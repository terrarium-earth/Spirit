package earth.terrarium.spirit.api.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface SoulCollectionEvent {
    int execute(LivingEntity victim, Player collector, int amount);
}
