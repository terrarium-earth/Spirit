package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.armor_abilities.ArmorAbility;
import earth.terrarium.spirit.api.armor_abilities.ArmorAbilityManager;
import earth.terrarium.spirit.api.armor_abilities.BlankAbility;
import earth.terrarium.spirit.common.abilities.BlazeAbility;
import earth.terrarium.spirit.common.abilities.CowAbility;
import earth.terrarium.spirit.common.abilities.WolfAbility;

public class SpiritArmorAbilities {
    public static final ResourcefulRegistry<ArmorAbility> ARMOR_ABILITIES = ResourcefulRegistries.create(ArmorAbilityManager.getAbilityRegistry(), Spirit.MODID);

    public static final RegistryEntry<ArmorAbility> BLAZE = ARMOR_ABILITIES.register("blaze", BlazeAbility::new);
    public static final RegistryEntry<ArmorAbility> WOLF = ARMOR_ABILITIES.register("wolf", WolfAbility::new);
    public static final RegistryEntry<ArmorAbility> COW = ARMOR_ABILITIES.register("cow", CowAbility::new);
}
