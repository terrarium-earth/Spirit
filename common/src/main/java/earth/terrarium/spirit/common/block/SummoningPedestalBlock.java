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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SummoningPedestalBlock extends BaseEntityBlock {
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
                    if (soulPedestal.getContainer().insert(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.SIMULATE) == 1) {
                        soulPedestal.getContainer().insert(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.NO_TAKE_BACKSIES);
                        soulContainer.extract(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.NO_TAKE_BACKSIES);
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    } else if (soulPedestal.getContainer().extract(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.SIMULATE).getAmount() == 1) {
                        soulPedestal.getContainer().extract(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.NO_TAKE_BACKSIES);
                        soulContainer.insert(new SoulStack(soulContainer.getSoulStack(0).getEntity(), 1), InteractionMode.NO_TAKE_BACKSIES);
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    }
                    var recipes = SummoningRecipe.getRecipesForEntity(soulPedestal.getContainer().getSoulStack(0), stack, level.getRecipeManager());
                    if (!recipes.isEmpty()) {
                        for (var recipe : recipes) {
                            if (RecipeUtils.validatePedestals(blockPos, level, new ArrayList<>(recipe.ingredients()), false)) {
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
}
