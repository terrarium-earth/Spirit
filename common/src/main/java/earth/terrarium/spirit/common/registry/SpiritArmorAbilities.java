package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbility;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.common.abilities.armor.*;

public class SpiritArmorAbilities {
    public static final ResourcefulRegistry<ArmorAbility> ARMOR_ABILITIES = ResourcefulRegistries.create(ArmorAbilityManager.getAbilityRegistry(), Spirit.MODID);

    public static final RegistryEntry<ArmorAbility> BLAZE = ARMOR_ABILITIES.register("blaze", BlazeAbility::new);
    public static final RegistryEntry<ArmorAbility> WOLF = ARMOR_ABILITIES.register("wolf", WolfAbility::new);
    public static final RegistryEntry<ArmorAbility> COW = ARMOR_ABILITIES.register("cow", CowAbility::new);
    public static final RegistryEntry<ArmorAbility> STRIDER = ARMOR_ABILITIES.register("strider", StriderAbility::new);
    public static final RegistryEntry<ArmorAbility> AXOLOTL = ARMOR_ABILITIES.register("axolotl", AxolotlAbility::new);
    public static final RegistryEntry<ArmorAbility> BEE = ARMOR_ABILITIES.register("bee", BeeAbility::new);
    public static final RegistryEntry<ArmorAbility> BAT = ARMOR_ABILITIES.register("bat", BatAbility::new);
    public static final RegistryEntry<ArmorAbility> CHICKEN = ARMOR_ABILITIES.register("chicken", ChickenAbility::new);
    public static final RegistryEntry<ArmorAbility> ZOMBIE = ARMOR_ABILITIES.register("zombie", ZombieAbility::new);
}
