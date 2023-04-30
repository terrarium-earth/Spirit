package earth.terrarium.spirit.common.registry;

import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.spirit.Spirit;
import earth.terrarium.spirit.common.entity.SoulReceptacle;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import org.apache.commons.lang3.NotImplementedException;

import java.util.function.Supplier;

public class SpiritEntities {
    public static final ResourcefulRegistry<EntityType<?>> ENTITIES = ResourcefulRegistries.create(BuiltInRegistries.ENTITY_TYPE, Spirit.MODID);

    public static final RegistryEntry<EntityType<SoulReceptacle>> SOUL_RECEPTACLE = ENTITIES.register("soul_receptacle", () -> EntityType.Builder.of(SoulReceptacle::new, MobCategory.MISC).sized(.75f, .75f).build("soul_recptacle"));
}
