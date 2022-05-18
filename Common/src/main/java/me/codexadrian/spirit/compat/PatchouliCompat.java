package me.codexadrian.spirit.compat;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.patchouli.api.PatchouliAPI;

import static me.codexadrian.spirit.Constants.MODID;

public class PatchouliCompat {

    public static void bookRecipe(BlockPos blockPos, ItemEntity itemE, CallbackInfo ci) {
        itemE.setInvulnerable(true);
        ItemStack glossary = PatchouliAPI.get().getBookStack(new ResourceLocation(MODID, "revenant_enchiridion"));
        glossary.setCount(itemE.getItem().getCount());
        ItemEntity book = new ItemEntity(itemE.level, itemE.getX(), itemE.getY(), itemE.getZ(), glossary);
        book.setInvulnerable(true);
        for(int i = 0; i < itemE.getItem().getCount();) {
            itemE.getLevel().addFreshEntity(book);
            itemE.getItem().shrink(1);
        }
        if (!itemE.level.isClientSide()) {
            ServerLevel sLevel = (ServerLevel) itemE.level;
            sLevel.sendParticles(ParticleTypes.SOUL, blockPos.getX(), blockPos.getY(),
                    blockPos.getZ(), 40, 1, 2, 1, 0);
        }
        ci.cancel();
    }
}
