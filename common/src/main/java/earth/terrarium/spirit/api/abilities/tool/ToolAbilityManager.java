package earth.terrarium.spirit.api.abilities.tool;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.Spirit;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;

public class ToolAbilityManager {
    public static final ResourceLocation TOOL_ABILITY_REGISTRY = new ResourceLocation(Spirit.MODID, "tool_ability");
    public static final ResourceKey<Registry<ToolAbility>> TOOL_ABILITY_REGISTRY_KEY = ResourceKey.createRegistryKey(TOOL_ABILITY_REGISTRY);

    public static final ResourceLocation NO_ABILITY = new ResourceLocation(Spirit.MODID, "no_ability");

    @ExpectPlatform
    public static DefaultedRegistry<ToolAbility> getAbilityRegistry() {
        throw new NotImplementedException();
    }
}
