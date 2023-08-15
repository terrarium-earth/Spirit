package earth.terrarium.spirit.common.registry;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.api.abilities.tool.ToolAbility;
import earth.terrarium.spirit.api.abilities.tool.ToolAbilityManager;
import earth.terrarium.spirit.common.abilities.armor.DragonWingsAbility;
import earth.terrarium.spirit.common.abilities.armor.EndermanAbility;
import earth.terrarium.spirit.common.abilities.armor.LavaWalkingAbility;
import earth.terrarium.spirit.common.abilities.tool.LifestealAbility;

import javax.tools.Tool;

public class SpiritAbilities {
    //please do not register "spirit:blank" armor or tool abilities, those are already registered <- note for GitHub Copilot
    public static final ArmorAbility DRAGON = ArmorAbilityManager.registerAbility("spirit:dragon", new DragonWingsAbility());
    public static final ArmorAbility STRIDER = ArmorAbilityManager.registerAbility("spirit:strider", new LavaWalkingAbility());
    public static final ArmorAbility ENDERMAN = ArmorAbilityManager.registerAbility("spirit:enderman", new EndermanAbility());

    public static final ToolAbility LIFESTEAL = ToolAbilityManager.registerAbility("spirit:lifesteal", new LifestealAbility());

    public static void init() {
    }
}
