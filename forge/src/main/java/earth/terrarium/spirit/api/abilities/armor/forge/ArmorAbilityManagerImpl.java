package earth.terrarium.spirit.api.abilities.armor.forge;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ArmorAbilityManagerImpl {

    public static DefaultedRegistry<ArmorAbility> getAbilityRegistry() {
        return (DefaultedRegistry<ArmorAbility>) BuiltInRegistries.REGISTRY.get(ArmorAbilityManager.ARMOR_ABILITY_REGISTRY);
    }
}
