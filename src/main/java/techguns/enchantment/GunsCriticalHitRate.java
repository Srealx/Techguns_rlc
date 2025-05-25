package techguns.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.damagesystem.TGDamageSource;
import techguns.items.guns.GenericGun;

/**
 * Extra critical hit rate enchantment
 * @author srealx
 * @date 2025/3/8
 */
public class GunsCriticalHitRate extends GenericGunEnchantment {
    public static final float EXTRA_CRITICAL_HIT_RATE = 0.03f;

    public GunsCriticalHitRate() {
        super(Rarity.VERY_RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"guns_critical_hit_rate"))
                .setName("guns_critical_hit_rate");
    }

    protected GunsCriticalHitRate(Rarity p_i46731_1_, EnumEnchantmentType p_i46731_2_, EntityEquipmentSlot[] p_i46731_3_) {
        super(p_i46731_1_, p_i46731_2_, p_i46731_3_);
    }

     
    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof GenericGun;
    }

      
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canApply(stack);
    }

    // 额外暴击率
    public float extraCriticalHitRate(){
        return GunsCriticalHitRate.EXTRA_CRITICAL_HIT_RATE;
    }

     
    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  20 + level * 10;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 100;
    }

    @Override
    public void afterDamageSourceInit(TGDamageSource damageSource){
        ItemStack sourceItemStack = damageSource.getSourceItemStack();
        if (sourceItemStack == null){
            return;
        }
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, damageSource.getSourceItemStack());
        damageSource.setExtraCriticalHitRate(damageSource.getExtraCriticalHitRate() + enchantmentLevel * this.extraCriticalHitRate());
    }

}
