package techguns.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.damagesystem.TGDamageSource;
import techguns.items.guns.GenericGun;

import java.util.Random;

/**
 * @author srealx
 * @date 2025/4/29
 */
public class AshBulletEnchantment extends GenericGunEnchantment{

    public AshBulletEnchantment() {
        super(Rarity.RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"ash_bullet"))
                .setName("ash_bullet");
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
        return 1;
    }

    @Override
    public int getMinEnchantability(int level) {
        return 48;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 100;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return Boolean.TRUE;
    }


    @Override
    public void afterDamageSourceInit(TGDamageSource damageSource){
        ItemStack sourceItemStack = damageSource.getSourceItemStack();
        if (sourceItemStack==null || !(sourceItemStack.getItem() instanceof GenericGun)){
            return;
        }
        GenericGun gun =  (GenericGun) sourceItemStack.getItem();
        int ammoCount = gun.getCurrentAmmo(sourceItemStack);
        if (ammoCount == 0){
            // 随机增加50% ~ 100% 暴击伤害
            int rate = new Random().nextInt(51)+50;
            damageSource.addExtraCriticalHitDamageRate(rate/100f);
            // 额外增加 25% 暴击率
            damageSource.setExtraCriticalHitRate(damageSource.getExtraCriticalHitRate() + 0.25f);
        }
    }


}
