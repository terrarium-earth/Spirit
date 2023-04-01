package earth.terrarium.spirit.api.armor_abilities;

import com.google.common.collect.Multimap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class ArmorAbility {
    private String descriptionId;

    public void modifyAttributes(EquipmentSlot slot, ItemStack stack, Multimap<Attribute, AttributeModifier> attributes) {}
    public void onArmorTick(ItemStack stack, Level level, Player player) {}
    public void onEntityMove(ItemStack stack, LivingEntity livingEntity, Level level, BlockPos blockPos) {}
    public abstract ColorPalette getColor();
    public String getOrCreateNameId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("armor_ability", ArmorAbilityManager.getAbilityRegistry().getKey(this));
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
