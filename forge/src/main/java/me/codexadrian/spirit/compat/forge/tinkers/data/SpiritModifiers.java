package me.codexadrian.spirit.compat.forge.tinkers.data;

import me.codexadrian.spirit.Spirit;
import me.codexadrian.spirit.compat.forge.tinkers.SoulReaperModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;
import slimeknights.tconstruct.tools.data.ModifierProvider;

public class SpiritModifiers extends AbstractModifierProvider {
    public static final ModifierId SOUL_REAPER = new ModifierId(new ResourceLocation(Spirit.MODID, "soul_reaper"));

    public SpiritModifiers(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addModifiers() {
        addModifier(SOUL_REAPER, new Modifier());
    }

    @Override
    public @NotNull String getName() {
        return "Spirit Modifiers";
    }
}
