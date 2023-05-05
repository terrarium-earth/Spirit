package earth.terrarium.spirit.api.utils;

import earth.terrarium.spirit.api.storage.SoulContainingItem;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.common.config.items.CrystalConfig;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SoulUtils {
    public static final String SOULLESS_TAG = "Soulless";

    public static boolean isAllowed(ItemStack stack, TagKey<EntityType<?>> blacklistTag) {
        if (stack.getItem() instanceof SoulContainingItem crystal) {
            SoulStack soulStack = crystal.getContainer(stack).getSoulStack(0);
            if (soulStack.getEntity() != null) {
                return !soulStack.getEntity().is(blacklistTag);
            }
        }
        return false;
    }

    public static String getTierDisplay(ItemStack itemStack, Level level) {
        return CrystalConfig.initialTierName;
    }

    public static SoulContainer getContainer(ItemStack stack) {
        if (stack.getItem() instanceof SoulContainingItem object) {
            return object.getContainer(stack);
        }
        return null;
    }


}
