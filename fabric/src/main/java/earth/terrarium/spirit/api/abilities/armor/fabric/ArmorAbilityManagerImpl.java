package earth.terrarium.spirit.api.abilities.armor.fabric;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.api.abilities.armor.BlankArmorAbility;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;

public class ArmorAbilityManagerImpl {
    private static final DefaultedRegistry<ArmorAbility> REGISTRY = FabricRegistryBuilder.createDefaulted(
            ArmorAbilityManager.ARMOR_ABILITY_REGISTRY_KEY,
            ArmorAbilityManager.NO_ABILITY
    ).buildAndRegister();

    static {
        Registry.register(REGISTRY, ArmorAbilityManager.NO_ABILITY, new BlankArmorAbility());
    }

    public static DefaultedRegistry<ArmorAbility> getAbilityRegistry() {
        return REGISTRY;
    }
}
