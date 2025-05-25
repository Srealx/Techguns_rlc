package techguns.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.items.guns.GenericGun;

/**
 * lot of promote guns damage
 * @author srealx
 * @date 2025/3/7
 */
public class AdvancedGunsFirePowerEnchantment extends GunsFirePowerEnchantment{

    public AdvancedGunsFirePowerEnchantment() {
        super(Rarity.VERY_RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"advanced_guns_fire_power"))
                .setName("advanced_guns_fire_power");
    }

     
    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof GenericGun;
    }

      
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canApply(stack);
    }

     
    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public float getAdditionalDamageEveryLevel() {
        return 0.165f;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  25 + level * 7;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 50;
    }
}
