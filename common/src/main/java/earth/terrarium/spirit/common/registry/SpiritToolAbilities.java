package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbilityManager;
import earth.terrarium.spirit.common.abilities.tool.GlowSquidAbility;
import earth.terrarium.spirit.common.abilities.tool.MagmaratorAbililty;

public class SpiritToolAbilities {
    public static final ResourcefulRegistry<ToolAbility> TOOL_ABILITIES = ResourcefulRegistries.create(ToolAbilityManager.getAbilityRegistry(), Spirit.MODID);

    public static final RegistryEntry<ToolAbility> GLOW_SQUID = TOOL_ABILITIES.register("glow_squid", GlowSquidAbility::new);
    public static final RegistryEntry<ToolAbility> MAGMARATOR = TOOL_ABILITIES.register("magmarator", MagmaratorAbililty::new);
}
