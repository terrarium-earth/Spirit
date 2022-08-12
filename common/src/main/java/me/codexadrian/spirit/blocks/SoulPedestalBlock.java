package me.codexadrian.spirit.blocks;

import me.codexadrian.spirit.blocks.blockentity.SoulPedestalBlockEntity;
import me.codexadrian.spirit.items.SoulSteelWand;
import me.codexadrian.spirit.recipe.PedestalRecipe;
import me.codexadrian.spirit.registry.SpiritBlocks;
import me.codexadrian.spirit.registry.SpiritItems;
import me.codexadrian.spirit.registry.SpiritMisc;
import me.codexadrian.spirit.utils.RecipeUtils;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
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
import java.util.Comparator;

public class SoulPedestalBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 5, 0, 16, 10, 16),
            Block.box(3, 10, 3, 13, 11, 13),
            Block.box(4, 3, 4, 12, 5, 12),
            Block.box(2, 0, 2, 14, 3, 14)
    );

    public SoulPedestalBlock(Properties $$0) {
        super($$0);
    }

    public @NotNull InteractionResult use(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult blockHitResult) {
        if (interactionHand != InteractionHand.OFF_HAND) {
            ItemStack stack = player.getItemInHand(interactionHand);
            if (level.getBlockEntity(blockPos) instanceof SoulPedestalBlockEntity soulPedestal) {
                if ((stack.is(SpiritItems.SOUL_CRYSTAL.get()) || stack.is(SpiritItems.CRUDE_SOUL_CRYSTAL.get())) || stack.is(SpiritItems.SOUL_CRYSTAL_SHARD.get())) {
                    return transferSouls(soulPedestal, stack, level, blockPos, blockState, 1);
                } else if (stack.is(SpiritItems.SOUL_STEEL_WAND.get())) {
                    int wandMode = SoulSteelWand.getMode(stack);
                    if(wandMode == 0 && soulPedestal.type != null) {
                        soulPedestal.setType(SpiritMisc.SOUL_ENTITY.get(), soulPedestal.getSoulCount());
                        if (level instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(ParticleTypes.SOUL, blockPos.getX() + 0.5, blockPos.getY() + 1.25, blockPos.getZ() + 0.5, 10, 0, 0, 0, 0.05);
                            serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, blockPos.getX() + 0.5, blockPos.getY() + 1.25, blockPos.getZ() + 0.5, 10, 0, 0, 0, 0.05);
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    } else if (soulPedestal.type == null) {
                        ItemStack offhand = player.getItemInHand(InteractionHand.OFF_HAND);
                        if(interactionHand == InteractionHand.MAIN_HAND && (offhand.is(SpiritItems.CRUDE_SOUL_CRYSTAL.get()) || offhand.is(SpiritItems.SOUL_CRYSTAL.get())) && SoulUtils.getSoulsInCrystal(offhand) > Math.pow(2, wandMode)) {
                            return transferSouls(soulPedestal, offhand, level, blockPos, blockState, (int) Math.pow(2, wandMode));
                        } else {
                            player.displayClientMessage(Component.translatable("item.spirit.soul_wand_error").withStyle(ChatFormatting.RED), true);
                        }
                    }
                } else if (soulPedestal.type != null) {
                    var recipes = PedestalRecipe.getRecipesForEntity(soulPedestal.type, soulPedestal.getSoulCount(), stack, level.getRecipeManager());
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

    public static InteractionResult transferSouls(SoulPedestalBlockEntity soulPedestal, ItemStack stack, Level level, BlockPos blockPos, BlockState blockState, int transferAmount) {
        if (soulPedestal.type == null && SoulUtils.getSoulsInCrystal(stack) >= transferAmount) {
            if (stack.is(SpiritItems.SOUL_CRYSTAL.get()) || stack.is(SpiritItems.SOUL_CRYSTAL_SHARD.get())) {
                soulPedestal.setType(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(SoulUtils.getSoulCrystalType(stack))), transferAmount);
            } else {
                soulPedestal.setType(SpiritMisc.SOUL_ENTITY.get(), transferAmount);
            }
            SoulUtils.deviateSoulCount(stack, -transferAmount, level, null);
            level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL);
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else if (soulPedestal.type != null && SoulUtils.canCrystalAcceptSoul(stack, level, soulPedestal.type) && SoulUtils.getSoulsInCrystal(stack) >= transferAmount) {
            SoulUtils.deviateSoulCount(stack, 1, level, Registry.ENTITY_TYPE.getKey(soulPedestal.type).toString());
            level.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL);
            soulPedestal.clear();
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, SpiritBlocks.SOUL_PEDESTAL_ENTITY.get(), SoulPedestalBlockEntity::tick);
    }


    @Override
    public boolean isOcclusionShapeFullBlock(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new SoulPedestalBlockEntity(blockPos, blockState);
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
