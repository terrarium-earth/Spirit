package me.codexadrian.spirit.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BrokenSpawnerBlock extends Block {

    //I only made a class because abused_master AKA pain in my ass AKA moe told me to.
    public BrokenSpawnerBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.SPAWNER).requiresCorrectToolForDrops());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter getter, List<Component> components, TooltipFlag flag) {
        MutableComponent component;
        if(Minecraft.getInstance().player.isShiftKeyDown()) {
            component = new TranslatableComponent("block.spirit.broken_spawner.tooltip")
                    .withStyle(ChatFormatting.GRAY);
        } else {
            component = new TranslatableComponent("block.spirit.shiftkey.tooltip")
                    .withStyle(ChatFormatting.GRAY);
        }
        components.add(component);
    }
}
