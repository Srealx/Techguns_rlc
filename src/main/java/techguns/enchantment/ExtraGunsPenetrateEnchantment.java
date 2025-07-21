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
 * Extra Guns Penetrate
 * @author srealx
 * @date 2025/3/7
 */
public class ExtraGunsPenetrateEnchantment extends GenericGunEnchantment{
    private static final float EXTRA_PENETRATE_EVERY_LEVEL = 0.025f;

    public ExtraGunsPenetrateEnchantment() {
        super(Rarity.VERY_RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"extra_guns_penetrate"))
                .setName("extra_guns_penetrate");
    }
    protected ExtraGunsPenetrateEnchantment(Rarity p_i46731_1_, EnumEnchantmentType p_i46731_2_, EntityEquipmentSlot[] p_i46731_3_) {
        super(p_i46731_1_, p_i46731_2_, p_i46731_3_);
    }

    public float getPen(){
        return ExtraGunsPenetrateEnchantment.EXTRA_PENETRATE_EVERY_LEVEL;
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
        return 30 + level * 5;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 100;
    }

    @Override
    public void afterDamageSourceInit(TGDamageSource damageSource){
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, damageSource.getSourceItemStack());
        damageSource.setPenetration(damageSource.getPenetration() + enchantmentLevel * this.getPen());
    }
}
