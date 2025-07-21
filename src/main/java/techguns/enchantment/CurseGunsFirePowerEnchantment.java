package techguns.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.items.guns.GenericGun;

/**
 * medium promote guns damage
 * @author srealx
 * @date 2025/3/7
 */
public class CurseGunsFirePowerEnchantment extends GunsFirePowerEnchantment{

    public CurseGunsFirePowerEnchantment() {
        super(Rarity.RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"curse_guns_fire_power"))
                .setName("curse_guns_fire_power");
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
    public float getAdditionalDamageEveryLevel() {
        return -0.1f;
    }

    @Override
    public int getMinEnchantability(int level) {
        return  level * 10;
    }

    @Override
    public int getMaxEnchantability(int level) {
        return this.getMinEnchantability(level) + 30;
    }

    @Override
    public boolean isCurse() {
        return Boolean.TRUE;
    }
}
