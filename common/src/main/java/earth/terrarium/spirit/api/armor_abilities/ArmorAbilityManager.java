package earth.terrarium.spirit.api.armor_abilities;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.Spirit;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.NotImplementedException;

public class ArmorAbilityManager {
    public static final ResourceLocation ARMOR_ABILITY_REGISTRY = new ResourceLocation(Spirit.MODID, "armor_ability");
    public static final ResourceKey<Registry<ArmorAbility>> ARMOR_ABILITY_REGISTRY_KEY = ResourceKey.createRegistryKey(ARMOR_ABILITY_REGISTRY);

    public static final ResourceLocation NO_ABILITY = new ResourceLocation(Spirit.MODID, "no_ability");

    @ExpectPlatform
    public static DefaultedRegistry<ArmorAbility> getAbilityRegistry() {
        throw new NotImplementedException();
    }
}
