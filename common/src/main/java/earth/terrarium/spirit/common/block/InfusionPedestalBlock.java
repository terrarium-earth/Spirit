package earth.terrarium.spirit.common.block;

import earth.terrarium.spirit.api.utils.SoulStack;
import earth.terrarium.spirit.common.blockentity.InfusionTableBlockEntity;
import earth.terrarium.spirit.common.blockentity.PedestalBlockEntity;
import earth.terrarium.spirit.common.item.armor.SoulSteelArmor;
import earth.terrarium.spirit.common.item.tools.SoulSteelTool;
import earth.terrarium.spirit.common.recipes.ArmorInfusionRecipe;
import earth.terrarium.spirit.common.recipes.PedestalRecipe;
import earth.terrarium.spirit.common.recipes.ToolInfusionRecipe;
import earth.terrarium.spirit.common.registry.SpiritBlockEntities;
import earth.terrarium.spirit.common.registry.SpiritItems;
import earth.terrarium.spirit.common.registry.SpiritRecipes;
import earth.terrarium.spirit.common.util.RecipeUtils;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class InfusionPedestalBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(2, 0, 2, 14, 3, 14),
            Block.box(5, 3, 3, 11, 8, 13),
            Block.box(3, 3, 5, 13, 8, 11),
            Block.box(1, 8, 1, 15, 12, 15)
    );

    public InfusionPedestalBlock(Properties properties) {
        super(properties);
    }

    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (!level.isClientSide && interactionHand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getItemInHand(interactionHand);
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof InfusionTableBlockEntity cage) {
                if (stack.is(SpiritItems.SOUL_FIRE_CHARGE.get()) || stack.is(SpiritItems.IGNITION_CRYSTAL.get())) {
                    if (!cage.isEmpty()) {
                        ItemStack itemStack = cage.getItem(0);
                        if (itemStack.getItem() instanceof SoulSteelArmor) {
                            List<ArmorInfusionRecipe> recipesForEntity = PedestalRecipe.getRecipesForEntity(SpiritRecipes.ARMOR_INFUSION.get(), itemStack, level.getRecipeManager());
                            Map<BlockPos, ItemStack> pedestalItems = RecipeUtils.getPedestalItems(blockPos, level);
                            Map<BlockPos, SoulStack> pedestalSouls = RecipeUtils.getPedestalSouls(blockPos, level);
                            for (ArmorInfusionRecipe recipe : recipesForEntity) {
                                if (!recipe.allowInfusion(itemStack)) continue;
                                if (RecipeUtils.validatePedestals(level, recipe, pedestalItems, pedestalSouls, false)) {
                                    cage.setRecipe(recipe);
                                    triggerFire(player, stack);
                                    return InteractionResult.SUCCESS;
                                }
                            }
                        } else if (itemStack.getItem() instanceof SoulSteelTool) {
                            List<ToolInfusionRecipe> recipesForEntity = PedestalRecipe.getRecipesForEntity(SpiritRecipes.TOOL_INFUSION.get(), itemStack, level.getRecipeManager());
                            Map<BlockPos, ItemStack> pedestalItems = RecipeUtils.getPedestalItems(blockPos, level);
                            Map<BlockPos, SoulStack> pedestalSouls = RecipeUtils.getPedestalSouls(blockPos, level);
                            for (ToolInfusionRecipe recipe : recipesForEntity) {
                                if (!recipe.allowInfusion(itemStack)) continue;
                                if (RecipeUtils.validatePedestals(level, recipe, pedestalItems, pedestalSouls, false)) {
                                    cage.setRecipe(recipe);
                                    triggerFire(player, stack);
                                    return InteractionResult.SUCCESS;
                                }
                            }
                        }
                    }
                } else if (cage.isEmpty() && !stack.isEmpty()) {
                    cage.setItem(0, stack);
                    player.setItemInHand(interactionHand, ItemStack.EMPTY);
                    cage.update();
                    return InteractionResult.SUCCESS;
                } else if (stack.isEmpty()) {
                    player.setItemInHand(interactionHand, cage.removeItemNoUpdate(0));
                    cage.update();
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    public void triggerFire(Player player, ItemStack stack) {
        if (player.isCreative()) return;
        if (stack.is(SpiritItems.SOUL_FIRE_CHARGE.get())) {
            stack.shrink(1);
        } else if (stack.is(SpiritItems.IGNITION_CRYSTAL.get())) {
            stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SpiritBlockEntities.INFUSION_PEDESTAL.get(), InfusionTableBlockEntity::tick);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(@NotNull BlockState blockState, LootContext.@NotNull Builder builder) {
        List<ItemStack> drops = super.getDrops(blockState, builder);
        BlockEntity blockE = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockE instanceof PedestalBlockEntity pedestalBlock) {
            drops.add(pedestalBlock.getItem(0));
        }

        return drops;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new InfusionTableBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }
}
