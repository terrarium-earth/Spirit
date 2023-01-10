package earth.terrarium.spirit.api.event;

import earth.terrarium.spirit.Spirit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class EventHandler {
    private static final Map<ResourceLocation, SoulCollectionEvent> EVENTS = new HashMap<>();

    public static int gatherSoulCount(LivingEntity victim, Player player) {
        int amount = 1;
        for (var event : EVENTS.entrySet()) {
            try {
                amount = event.getValue().execute(victim, player, amount);
            } catch (Exception e) {
                Spirit.LOGGER.error(event.getKey() + " : " + e);
            }
        }
        return amount;
    }

    public static void registerEvent(ResourceLocation id, SoulCollectionEvent event) {
        EVENTS.put(id, event);
    }
}
