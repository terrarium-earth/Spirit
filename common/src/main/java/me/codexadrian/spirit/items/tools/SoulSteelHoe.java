package me.codexadrian.spirit.items.tools;

import me.codexadrian.spirit.data.ToolType;
import me.codexadrian.spirit.items.SoulMetalMaterial;
import me.codexadrian.spirit.utils.ToolUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

public class SoulSteelHoe extends HoeItem {
    public SoulSteelHoe(Properties properties) {
        super(SoulMetalMaterial.INSTANCE, 3, -2.4F, properties);
    }

    @Override
    public InteractionResult useOn(@NotNull UseOnContext useOnContext) {
        return ToolUtils.handleOnHitBlock(super.useOn(useOnContext), ToolType.HOE, useOnContext.getPlayer(), useOnContext.getItemInHand(), useOnContext.getLevel(), useOnContext.getClickedPos());
    }
}
