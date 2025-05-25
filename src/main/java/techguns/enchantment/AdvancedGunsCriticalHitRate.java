package techguns.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;

/**
 * Extra critical hit rate enchantment
 * @author srealx
 * @date 2025/3/8
 */
public class AdvancedGunsCriticalHitRate extends GunsCriticalHitRate {
    public static final float EXTRA_CRITICAL_HIT_RATE = 0.05f;

    public AdvancedGunsCriticalHitRate() {
        super(Rarity.VERY_RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"advanced_guns_critical_hit_rate"))
                .setName("advanced_guns_critical_hit_rate");
    }

    // 额外暴击率
    @Override
    public float extraCriticalHitRate(){
        return AdvancedGunsCriticalHitRate.EXTRA_CRITICAL_HIT_RATE;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  44 + level * 4;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return Boolean.TRUE;
    }

}
