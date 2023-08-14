package earth.terrarium.spirit.common.registry;

import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.common.abilities.armor.DragonWingsAbility;
import earth.terrarium.spirit.common.abilities.armor.EndermanAbility;
import earth.terrarium.spirit.common.abilities.armor.LavaWalkingAbility;

public class SpiritAbilities {
    //please do not register "spirit:blank" armor or tool abilities, those are already registered <- note for GitHub Copilot
    public static final ArmorAbility DRAGON = ArmorAbilityManager.registerAbility("spirit:dragon", new DragonWingsAbility());
    public static final ArmorAbility STRIDER = ArmorAbilityManager.registerAbility("spirit:strider", new LavaWalkingAbility());
    public static final ArmorAbility ENDERMAN = ArmorAbilityManager.registerAbility("spirit:enderman", new EndermanAbility());

    public static void init() {
    }
}
