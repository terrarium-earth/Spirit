package me.codexadrian.spirit.compat;

import mcjty.theoneprobe.api.*;
import me.codexadrian.spirit.Constants;
import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.SpiritRegistry;
import me.codexadrian.spirit.blocks.soulcage.SoulCageBlockEntity;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class TOPCompat implements Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerProvider(new IProbeInfoProvider() {
            @Override
            public ResourceLocation getID() {
                return new ResourceLocation(Constants.MODID, "soul_cage_probe");
            }

            @Override
            public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData probeHitData) {
                if (blockState.is(SpiritRegistry.SOUL_CAGE.get()) && level.getBlockEntity(probeHitData.getPos()) instanceof SoulCageBlockEntity blockEntity) {
                    if(blockEntity.isEmpty()) {
                        probeInfo.horizontal().text("block.spirit.soul_cage.empty_hover_text");
                    } else if(blockEntity.entity != null) {
                        probeInfo.horizontal().entity(blockEntity.entity).text(blockEntity.entity.getName()).text(" - Tier " + SoulUtils.getTierIndex(blockEntity.getItem(0)));
                    }
                }
            }
        });
        return null;
    }
}
