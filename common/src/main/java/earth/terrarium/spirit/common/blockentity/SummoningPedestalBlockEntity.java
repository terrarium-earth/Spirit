package earth.terrarium.spirit.common.blockentity;

import earth.terrarium.spirit.api.souls.SoulfulCreature;
import earth.terrarium.spirit.api.storage.BlockEntitySoulContainer;
import earth.terrarium.spirit.api.storage.InteractionMode;
import earth.terrarium.spirit.api.storage.container.SoulContainer;
import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.containers.SingleTypeContainer;
import earth.terrarium.spirit.common.recipes.SummoningRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import earth.terrarium.spirit.common.util.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SummoningPedestalBlockEntity extends AbstractPedestalBlockEntity<SummoningRecipe> {
    @Nullable
    public SummoningRecipe containedRecipe;
    public int burnTime = 0;
    public int age;

    public SummoningPedestalBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SpiritRecipes.SUMMONING.get(), blockPos, blockState);
    }

    @Override
    public void finishRecipe() {
        if(level == null || this.containedRecipe == null) return;
        Entity entity = this.containedRecipe.output().create(level);
        if (entity != null) {
            entity.setPos(getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.75, getBlockPos().getZ() + 0.5);
            if(this.containedRecipe.outputNbt().isPresent()) entity.load(this.containedRecipe.outputNbt().get());
            level.addFreshEntity(entity);
            for (int i = 0; i < 10; i++) {
                level.addParticle(ParticleTypes.SOUL, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);
            }
        }
    }
}
