package me.codexadrian.spirit.items.tools;

import me.codexadrian.spirit.data.MobTrait;
import me.codexadrian.spirit.data.MobTraitData;
import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.registry.SpiritItems;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class SoulSteelShovel extends ShovelItem {
    public SoulSteelShovel(Tier tier, float f, float g, Properties properties) {
        super(tier, f, g, properties);
    }

    @Override
    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            ItemStack soulCrystal = SoulUtils.findCrystal(player, null, true, true);
            if (soulCrystal.is(SpiritItems.SOUL_CRYSTAL.get()) && SoulUtils.getSoulsInCrystal(soulCrystal) > 0) {
                if (itemStack.getOrCreateTag().getBoolean("Charged")) {
                    var entityEffect = MobTraitData.getEffectForEntity(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(Objects.requireNonNull(SoulUtils.getSoulCrystalType(soulCrystal)))), level.getRecipeManager());
                    if (entityEffect.isPresent()) {
                        for (MobTrait<?> trait : entityEffect.get().traits()) {
                            trait.onHitBlock(ToolType.SHOVEL, livingEntity, blockState, level, blockPos);
                        }
                        SoulUtils.deviateSoulCount(soulCrystal, -1, level, null);
                    }
                }
            }
        }
        return super.mineBlock(itemStack, level, blockState, blockPos, livingEntity);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        var result = super.useOn(useOnContext);
        if(result == InteractionResult.SUCCESS) {
            ItemStack soulCrystal = SoulUtils.findCrystal(useOnContext.getPlayer(), null, true, true);
            if (soulCrystal.is(SpiritItems.SOUL_CRYSTAL.get()) && SoulUtils.getSoulsInCrystal(soulCrystal) > 0) {
                if (useOnContext.getItemInHand().getOrCreateTag().getBoolean("Charged")) {
                    var entityEffect = MobTraitData.getEffectForEntity(Registry.ENTITY_TYPE.get(ResourceLocation.tryParse(Objects.requireNonNull(SoulUtils.getSoulCrystalType(soulCrystal)))), useOnContext.getLevel().getRecipeManager());
                    if (entityEffect.isPresent()) {
                        for (MobTrait<?> trait : entityEffect.get().traits()) {
                            trait.onHitBlock(ToolType.SHOVEL, useOnContext.getPlayer(), useOnContext.getLevel().getBlockState(useOnContext.getClickedPos()), useOnContext.getLevel(), useOnContext.getClickedPos());
                        }
                        SoulUtils.deviateSoulCount(soulCrystal, -1, useOnContext.getLevel(), null);
                    }
                }
            }
        }
        return result;
    }
}
