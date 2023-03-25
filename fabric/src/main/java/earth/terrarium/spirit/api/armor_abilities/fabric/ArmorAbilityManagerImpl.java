package earth.terrarium.spirit.api.armor_abilities.fabric;

import earth.terrarium.spirit.api.armor_abilities.ArmorAbility;
import earth.terrarium.spirit.api.armor_abilities.ArmorAbilityManager;
import earth.terrarium.spirit.api.armor_abilities.BlankAbility;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;

public class ArmorAbilityManagerImpl {
    private static final DefaultedRegistry<ArmorAbility> REGISTRY = FabricRegistryBuilder.createDefaulted(
            ArmorAbilityManager.ARMOR_ABILITY_REGISTRY_KEY,
            ArmorAbilityManager.NO_ABILITY
    ).buildAndRegister();

    static {
        Registry.register(REGISTRY, ArmorAbilityManager.NO_ABILITY, new BlankAbility());
    }

    public static DefaultedRegistry<ArmorAbility> getAbilityRegistry() {
        return REGISTRY;
    }
}
