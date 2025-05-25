package techguns.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.items.guns.GenericGun;

/**
 * huge promote guns damage
 * @author Srealx
 * @date 2025/3/7
 */
public class UltraGunsFirePowerEnchantment extends GunsFirePowerEnchantment{

    public UltraGunsFirePowerEnchantment() {
        super(Rarity.VERY_RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"ultra_guns_fire_power"))
                .setName("ultra_guns_fire_power");
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
        return 0.3f;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  50 + level * 2;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 100;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return Boolean.TRUE;
    }

}
