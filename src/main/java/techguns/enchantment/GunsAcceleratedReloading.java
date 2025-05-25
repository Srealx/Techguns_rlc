package techguns.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.items.guns.GenericGun;

/**
 * Guns Accelerated Reloading
 * @author srealx
 * @date 2025/3/8
 */
public class GunsAcceleratedReloading extends GenericGunEnchantment {
    public static final float REDUCE_RELOAD_TIME_EVERY_LEVEL = 0.1f;

    public GunsAcceleratedReloading() {
        super(Rarity.UNCOMMON,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"guns_accelerated_reloading"))
                .setName("guns_accelerated_reloading");
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
    public int getMinEnchantability(int level) {
        return  level * 10;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 100;
    }

    @Override
    public Integer beforeReloadAmmo(ItemStack itemStack,Integer reloadTime){
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, itemStack);
        return reloadTime -  Math.round(reloadTime * enchantmentLevel * REDUCE_RELOAD_TIME_EVERY_LEVEL);
    }

}
