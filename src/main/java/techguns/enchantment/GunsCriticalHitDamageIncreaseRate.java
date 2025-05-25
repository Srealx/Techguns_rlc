package techguns.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.damagesystem.TGDamageSource;
import techguns.items.guns.GenericGun;

/**
 * Extra critical hit damage rate enchantment
 * @author srealx
 * @date 2025/3/8
 */
public class GunsCriticalHitDamageIncreaseRate extends GenericGunEnchantment {
    public static final String TAG_NAME = "egchdir";
    /**
     * when kill entity, grow rate
     */
    private static final float EXTRA_DAMAGE_RATE_WHEN_KILL = 0.00125f;
    /**
     * max rate every level
     */
    private static final float MAX_RATE_EVERY_LEVEL = 0.125f;

    public GunsCriticalHitDamageIncreaseRate() {
        super(Rarity.VERY_RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"guns_critical_hit_damage_increase_rate"))
                .setName("guns_critical_hit_damage_increase_rate");
    }

    protected GunsCriticalHitDamageIncreaseRate(Rarity p_i46731_1_, EnumEnchantmentType p_i46731_2_, EntityEquipmentSlot[] p_i46731_3_) {
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

     
    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  30 + ((level-1) * 15);
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
        damageSource.addExtraCriticalHitDamageRate(sourceItemStack.getTagCompound().getFloat(TAG_NAME));
    }

    @Override
    public void afterGunKill(TGDamageSource damageSource){
        if (Boolean.FALSE.equals(damageSource.getCriticalHitFlat())){
            return;
        }
        ItemStack sourceItemStack = damageSource.getSourceItemStack();
        NBTTagCompound tagCompound = sourceItemStack.getTagCompound();
        float tag = tagCompound.getFloat(TAG_NAME);
        int enchantmentLevel = EnchantmentHelper.getEnchantmentLevel(this, damageSource.getSourceItemStack());
        if (enchantmentLevel * MAX_RATE_EVERY_LEVEL - tag >= EXTRA_DAMAGE_RATE_WHEN_KILL){
            tagCompound.setFloat(TAG_NAME, tag + EXTRA_DAMAGE_RATE_WHEN_KILL );
        }
    }

}
