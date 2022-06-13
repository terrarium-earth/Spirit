package me.codexadrian.spirit.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrokenSpawnerBlock extends Block {

    //I only made a class because abused_master AKA pain in my ass AKA moe told me to.
    public BrokenSpawnerBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops());
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter getter, @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        if(Screen.hasShiftDown()) {
            String string = Component.translatable("block.spirit.broken_spawner.tooltip").getString();
            String[] lines = string.split("<br>");
            for(String line : lines) {
                components.add(Component.literal(line).withStyle(ChatFormatting.GRAY));
            }
        } else {
            components.add(Component.translatable("block.spirit.shiftkey.tooltip")
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
