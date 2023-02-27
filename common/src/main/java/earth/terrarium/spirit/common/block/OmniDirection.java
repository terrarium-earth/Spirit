package earth.terrarium.spirit.common.block;

import net.minecraft.core.BlockPos;

public enum OmniDirection {
    NORTH(0, -1),
    SOUTH(0, 1),
    EAST(1, 0),
    WEST(-1, 0),
    NORTH_EAST(1, -1),
    NORTH_WEST(-1, -1),
    SOUTH_EAST(1, 1),
    SOUTH_WEST(-1, 1);

    private final int x;
    private final int z;

    public static final OmniDirection[] VALUES = values();

    OmniDirection(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public BlockPos offset(BlockPos pos) {
        return pos.offset(x, 0, z);
    }
}
