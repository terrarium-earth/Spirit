package earth.terrarium.spirit.client.renderer.armor.forge;

import earth.terrarium.spirit.client.renderer.armor.ArmorRenderers;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.Map;

public class ArmorRenderersImpl {
    private static final Map<Item, ArmorRenderers.ArmorModelSupplier> ARMOR_ITEM_MODELS = new HashMap<>();

    public static void registerArmour(ArmorRenderers.ArmorModelSupplier modelSupplier, Item... items) {
        for (Item item : items) {
            registerArmourRenderer(item, modelSupplier);
        }
    }

    public static ArmorRenderers.ArmorModelSupplier getArmourRenderer(ItemLike item) {
        return ARMOR_ITEM_MODELS.get(item.asItem());
    }

    private static void registerArmourRenderer(ItemLike item, ArmorRenderers.ArmorModelSupplier renderer) {
        ARMOR_ITEM_MODELS.put(item.asItem(), renderer);
    }
}
