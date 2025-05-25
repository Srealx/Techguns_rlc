package techguns.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;

/**
 * Extra Guns Penetrate
 * @author srealx
 * @date 2025/3/7
 */
public class AdvancedExtraGunsPenetrateEnchantment extends ExtraGunsPenetrateEnchantment{
    public static final float EXTRA_PENETRATE_EVERY_LEVEL = 0.05f;

    public AdvancedExtraGunsPenetrateEnchantment() {
        super(Rarity.RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"adv_extra_guns_penetrate"))
                .setName("adv_extra_guns_penetrate");
    }

    @Override
    public float getPen(){
        return AdvancedExtraGunsPenetrateEnchantment.EXTRA_PENETRATE_EVERY_LEVEL;
    }

    @Override
    public int getMinEnchantability(int level) {
        return 40 + level * 4;
    }
}
