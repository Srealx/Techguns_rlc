package techguns.enchantment;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.damagesystem.TGDamageSource;

/**
 * Extra critical hit damage rate enchantment
 * @author srealx
 * @date 2025/3/8
 */
public class AdvancedGunsCriticalHitDamageIncreaseRate extends GunsCriticalHitDamageIncreaseRate {
    public static final String TAG_NAME = "egchdir";
    /**
     * when kill entity, grow rate
     */
    private static final float EXTRA_DAMAGE_RATE_WHEN_KILL = 0.0025f;
    /**
     * max rate every level
     */
    private static final float MAX_RATE_EVERY_LEVEL = 0.25f;

    public AdvancedGunsCriticalHitDamageIncreaseRate() {
        super(Rarity.VERY_RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"advanced_guns_critical_hit_damage_increase_rate"))
                .setName("advanced_guns_critical_hit_damage_increase_rate");
    }

    @Override
    public int getMinEnchantability(int level) {
        return  39 + level * 7;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return Boolean.TRUE;
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
