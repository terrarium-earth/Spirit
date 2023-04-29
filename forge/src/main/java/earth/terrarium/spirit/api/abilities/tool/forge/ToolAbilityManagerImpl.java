package earth.terrarium.spirit.api.abilities.tool.forge;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbilityManager;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ToolAbilityManagerImpl {
    public static DefaultedRegistry<ToolAbility> getAbilityRegistry() {
        return (DefaultedRegistry<ToolAbility>) BuiltInRegistries.REGISTRY.get(ToolAbilityManager.TOOL_ABILITY_REGISTRY);
    }
}
