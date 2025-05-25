package techguns.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.bean.ShootGunSpawnProjectileEventBean;
import techguns.items.guns.GenericGun;

/**
 * ShotGun Bullet Increase
 * @author srealx
 * @date 2025/3/8
 */
public class ShootGunBulletIncrease extends GenericGunEnchantment {
    public static final int BULLET_INCREASE_EVERY_LEVEL = 1;

    public ShootGunBulletIncrease() {
        super(Rarity.RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"shoot_gun_bullet_increase"))
                .setName("shoot_gun_bullet_increase");
    }

     
    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof GenericGun && ((GenericGun)stack.getItem()).shotgun;
    }

    @Override
    public boolean canApplyAnvil(ItemStack stack){
        return stack.getItem() instanceof GenericGun && ((GenericGun)stack.getItem()).shotgun;
    }

      
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canApply(stack);
    }

    @Override
    protected boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment)
                && Boolean.FALSE.equals(enchantment instanceof ExtraCatapultBulletIncrease)
                && Boolean.FALSE.equals(enchantment instanceof NoThinkShoot);
    }

     
    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  20 + level * 12;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 100;
    }

    @Override
    public void beforeShotGunSpawnProjectile(ItemStack itemStack,ShootGunSpawnProjectileEventBean shootGunSpawnProjectileEventBean){
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, itemStack);
        shootGunSpawnProjectileEventBean.setBulletCount(shootGunSpawnProjectileEventBean.getBulletCount() + enchantmentLevel * BULLET_INCREASE_EVERY_LEVEL);
    }

}
