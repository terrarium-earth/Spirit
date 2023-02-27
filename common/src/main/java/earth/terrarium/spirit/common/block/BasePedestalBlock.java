package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.common.blockentity.AbstractPedestalBlockEntity;
import earth.terrarium.spirit.common.recipes.PedestalRecipe;
import earth.terrarium.spirit.common.util.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public abstract class BasePedestalBlock<T extends PedestalRecipe<?>> extends BaseEntityBlock {
    private final RecipeType<T> recipeType;

    public BasePedestalBlock(RecipeType<T> recipeType, Properties properties) {
        super(properties);
        this.recipeType = recipeType;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (interactionHand != InteractionHand.OFF_HAND) {
            ItemStack stack = player.getItemInHand(interactionHand);
            if (level.getBlockEntity(blockPos) instanceof AbstractPedestalBlockEntity<?>) {
                AbstractPedestalBlockEntity<T> soulPedestal = (AbstractPedestalBlockEntity<T>) level.getBlockEntity(blockPos);
                var recipes = PedestalRecipe.getRecipesForEntity(recipeType, stack, level.getRecipeManager());
                if (!recipes.isEmpty()) {
                    var items = RecipeUtils.getPedestalItems(blockPos, level);
                    var souls = RecipeUtils.getPedestalSouls(blockPos, level);
                    for (var recipe : recipes) {
                        if (RecipeUtils.validatePedestals(level, recipe, items, souls, false)) {
                            soulPedestal.setRecipe(recipe);
                            if (recipe.consumesActivator()) stack.shrink(1);
                            return InteractionResult.sidedSuccess(level.isClientSide());
                        }
                    }
                }
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}
