package earth.terrarium.spirit.api.armor_abilities.forge;

import earth.terrarium.spirit.api.armor_abilities.ArmorAbility;
import earth.terrarium.spirit.api.armor_abilities.ArmorAbilityManager;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ArmorAbilityManagerImpl {

    public static DefaultedRegistry<ArmorAbility> getAbilityRegistry() {
        return (DefaultedRegistry<ArmorAbility>) BuiltInRegistries.REGISTRY.get(ArmorAbilityManager.ARMOR_ABILITY_REGISTRY);
    }
}
