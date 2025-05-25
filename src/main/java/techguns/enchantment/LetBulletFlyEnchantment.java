package techguns.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.items.guns.GenericGun;

/**
 * @author srealx
 * @date 2025/4/29
 */
public class LetBulletFlyEnchantment extends GenericGunEnchantment{
    public static final float projectileSpeedAddRate = 0.2f;

    public LetBulletFlyEnchantment() {
        super(Rarity.RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"let_bullet_fly"))
                .setName("let_bullet_fly");
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
        return 2;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  15 + level * 15;
    }

    @Override
    public int getMaxEnchantability(int p_getMaxEnchantability_1_) {
        return 100;
    }

    @Override
    public float beforeProjectileCreate(ItemStack stack,float speed){
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, stack);
        return speed *  (1 + projectileSpeedAddRate*enchantmentLevel);
    }

}
