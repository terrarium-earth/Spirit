package earth.terrarium.spirit.api.storage;

import earth.terrarium.spirit.api.storage.container.SoulContainer;
import org.jetbrains.annotations.Nullable;

public interface SoulContainingBlock {
    @Nullable
    SoulContainer getContainer();
}
