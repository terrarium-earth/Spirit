package earth.terrarium.spirit.common.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;

public class KeybindUtils {
    public static final HashMap<UUID, Boolean> PLAYER_KEYS = new HashMap<>();

    private boolean clickingJump;

    public static boolean jumpKeyDown(Player player) {
        PLAYER_KEYS.putIfAbsent(player.getUUID(), false);
        return PLAYER_KEYS.get(player.getUUID());
    }

    public static void setJumpKeyDown(Player player, boolean value) {
        PLAYER_KEYS.put(player.getUUID(), value);
    }
}
