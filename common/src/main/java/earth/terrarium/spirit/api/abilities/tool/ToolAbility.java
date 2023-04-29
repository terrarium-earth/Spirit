package earth.terrarium.spirit.api.abilities.tool;

import com.google.common.collect.Multimap;
import earth.terrarium.spirit.api.abilities.armor.ArmorAbilityManager;
import earth.terrarium.spirit.api.abilities.ColorPalette;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.UUID;

public abstract class ToolAbility {
    private String descriptionId;

    public void onHit(Player player, ItemStack stack, LivingEntity target) {}
    public void onKill(Player player, ItemStack stack, LivingEntity target) {}
    public void onBlockBreak(ItemStack stack, Level level, BlockState state, BlockPos pos, Player player) {}
    public void modifyAttributes(ItemStack stack, Multimap<Attribute, AttributeModifier> attributes) {}
    public void modifyDrops(Player player, List<ItemStack> drops) {}
    public void whileHeld(Player player, ItemStack stack) {}
    public int illuminationLevel() { return 0; }

    public abstract ColorPalette getColor();

    public String getOrCreateNameId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("tool_ability", ToolAbilityManager.getAbilityRegistry().getKey(this));
        }
        return this.descriptionId;
    }

    public String getNameId() {
        return this.getOrCreateNameId();
    }

    public String getDescriptionId() {
        return this.getOrCreateNameId() + ".desc";
    }
}
