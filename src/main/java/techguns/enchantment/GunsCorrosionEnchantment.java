package techguns.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.items.guns.GenericGun;

/**
 *
 * @author srealx
 * @date 2025/3/7
 */
public class GunsCorrosionEnchantment extends GenericGunEnchantment{

    public GunsCorrosionEnchantment() {
        super(Rarity.RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"guns_corrosion"))
                .setName("guns_corrosion");
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
        return 3;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  15 + level * 5;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 100;
    }

    @Override
    public boolean isCurse() {
        return Boolean.TRUE;
    }

    @Override
    public int beforeDurabilityLoss(ItemStack stack, int consumingDurabilityEveryShoot){
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, stack);
        float con =  consumingDurabilityEveryShoot * (1 + enchantmentLevel * 0.5f);
        return Math.round(con);
    }
}
