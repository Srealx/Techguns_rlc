package techguns.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.bean.BulletGenericDataBean;
import techguns.items.guns.GenericGun;

/**
 * ShotGun Bullet Increase
 * @author srealx
 * @date 2025/3/8
 */
public class ExtraCatapultBulletIncrease extends GenericGunEnchantment {
    public static final int BULLET_INCREASE_EVERY_LEVEL = 1;

    public ExtraCatapultBulletIncrease() {
        super(Rarity.RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"extra_catapult_bullet_increase"))
                .setName("extra_catapult_bullet_increase");
    }

     
    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof GenericGun && ((GenericGun)stack.getItem()).regenerationBulletGun;
    }

    @Override
    public boolean canApplyAnvil(ItemStack stack){
        return stack.getItem() instanceof GenericGun && ((GenericGun)stack.getItem()).regenerationBulletGun;
    }

      
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canApply(stack);
    }

    @Override
    protected boolean canApplyTogether(Enchantment enchantment) {
        return Boolean.FALSE.equals(enchantment instanceof NoThinkShoot)
                && Boolean.FALSE.equals(enchantment instanceof ShootGunBulletIncrease)
                && super.canApplyTogether(enchantment);
    }

     
    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  45 + level * 5;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 100;
    }

    @Override
    public void beforeProjectilesCreate(BulletGenericDataBean bulletGenericDataBean,ItemStack itemStack){
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, itemStack);
        bulletGenericDataBean.setBulletRegenerationCount(bulletGenericDataBean.getBulletRegenerationCount() +
                enchantmentLevel * BULLET_INCREASE_EVERY_LEVEL);
    }

}
