package earth.terrarium.spirit.api.abilities.tool.fabric;

import earth.terrarium.spirit.api.abilities.armor.BlankArmorAbility;
import earth.terrarium.spirit.api.abilities.tool.BlankToolAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbilityManager;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;

public class ToolAbilityManagerImpl {
    private static final DefaultedRegistry<ToolAbility> REGISTRY = FabricRegistryBuilder.createDefaulted(
            ToolAbilityManager.TOOL_ABILITY_REGISTRY_KEY,
            ToolAbilityManager.NO_ABILITY
    ).buildAndRegister();

    static {
        Registry.register(REGISTRY, ToolAbilityManager.NO_ABILITY, new BlankToolAbility());
    }

    public static DefaultedRegistry<ToolAbility> getAbilityRegistry() {
        return REGISTRY;
    }
}
