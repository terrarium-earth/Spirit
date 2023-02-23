package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.api.storage.InteractionMode;
import earth.terrarium.spirit.api.storage.SoulContainingObject;
import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.api.utils.SoulUtils;
import earth.terrarium.spirit.common.blockentity.SummoningPedestalBlockEntity;
import earth.terrarium.spirit.common.recipes.SummoningRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritItems;
import earth.terrarium.spirit.common.util.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SummoningPedestalBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 5, 0, 16, 10, 16),
            Block.box(3, 10, 3, 13, 11, 13),
            Block.box(4, 3, 4, 12, 5, 12),
            Block.box(2, 0, 2, 14, 3, 14)
    );

    public SummoningPedestalBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SummoningPedestalBlockEntity(blockPos, blockState);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (interactionHand != InteractionHand.OFF_HAND) {
            ItemStack stack = player.getItemInHand(interactionHand);
            if (level.getBlockEntity(blockPos) instanceof SummoningPedestalBlockEntity soulPedestal) {
                if (stack.getItem() instanceof SoulContainingObject.Item soulContainingObject) {
                    var soulContainer = soulContainingObject.getContainer(stack);
                    if (soulContainer == null) return InteractionResult.FAIL;
                    if (soulPedestal.getContainer().insert(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.SIMULATE) == 1 && soulContainer.extract(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.SIMULATE).getAmount() == 1) {
                        soulPedestal.getContainer().insert(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.NO_TAKE_BACKSIES);
                        soulContainer.extract(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.NO_TAKE_BACKSIES);
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    } else if (soulContainer.insert(new SoulStack(soulPedestal.getContainer().getSoulStack(0).getEntity(), 1), InteractionMode.SIMULATE) == 1) {
                        var soulStack = soulPedestal.getContainer().getSoulStack(0).copy();
                        SoulStack extract = soulPedestal.getContainer().extract(soulStack, InteractionMode.SIMULATE);
                        int inserted = soulContainer.insert(extract.copy(), InteractionMode.NO_TAKE_BACKSIES);
                        soulPedestal.getContainer().extract(new SoulStack(soulStack.getEntity(), inserted), InteractionMode.NO_TAKE_BACKSIES);
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    }
                    var recipes = SummoningRecipe.getRecipesForEntity(soulPedestal.getContainer().getSoulStack(0), stack, level.getRecipeManager());
                    if (!recipes.isEmpty()) {
                        for (var recipe : recipes) {
                            if (RecipeUtils.validatePedestals(blockPos, level, recipe, false)) {
                                soulPedestal.setRecipe(recipe);
                                if (recipe.consumesActivator()) stack.shrink(1);
                                return InteractionResult.sidedSuccess(level.isClientSide());
                            }
                        }
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SpiritBlockEntities.SUMMONING_PEDESTAL.get(), (level1, blockPos, blockState1, blockEntity) -> blockEntity.tick());
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }
}
