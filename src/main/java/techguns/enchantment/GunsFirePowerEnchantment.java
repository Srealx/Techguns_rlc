package techguns.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import techguns.damagesystem.TGDamageSource;

/**
 * 枪械火力附魔抽象
 * @author srealx
 * @date 2025/3/7
 */
public abstract class GunsFirePowerEnchantment extends GenericGunEnchantment {

    protected GunsFirePowerEnchantment(Rarity p_i46731_1_, EnumEnchantmentType p_i46731_2_, EntityEquipmentSlot[] p_i46731_3_) {
        super(p_i46731_1_, p_i46731_2_, p_i46731_3_);
    }

    @Override
    protected boolean canApplyTogether(Enchantment enchantment) {
        return Boolean.FALSE.equals(enchantment instanceof GunsFirePowerEnchantment)
                && super.canApplyTogether(enchantment);
    }

    /**
     * 获取每个附魔等级下额外的伤害倍率
     * @return {@link float}
     */
    public abstract float getAdditionalDamageEveryLevel();


    /**
     * before guns fire bullet
     */
    @Override
    public void afterDamageSourceInit(TGDamageSource damageSource){
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, damageSource.getSourceItemStack());
        float rate = this.getAdditionalDamageEveryLevel() * enchantmentLevel;
        damageSource.addAdditionalDamageRate(rate);
    }


}
