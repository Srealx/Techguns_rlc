package techguns.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import techguns.Techguns;
import techguns.damagesystem.TGDamageSource;
import techguns.items.guns.GenericGun;

import java.util.Collection;

/**
 *
 * @author srealx
 * @date 2025/3/8
 */
public class GunsCriticalHitPotionDelayed extends GenericGunEnchantment {

    public GunsCriticalHitPotionDelayed() {
        super(Rarity.RARE,
                EnumEnchantmentType.WEAPON,
                new EntityEquipmentSlot[]{EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND});
        this.setRegistryName(new ResourceLocation(Techguns.MODID,"guns_critical_hit_potion_delayed"))
                .setName("guns_critical_hit_potion_delayed");
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
        return  50;
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
    public void afterGunKill(TGDamageSource damageSource){
        if (Boolean.FALSE.equals(damageSource.getCriticalHitFlat())){
            return;
        }
        Entity attacker = damageSource.attacker;
        if (attacker instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)attacker;
            Collection<PotionEffect> activePotionEffects = player.getActivePotionEffects();
            activePotionEffects.forEach(effect->{
                Potion potion = effect.getPotion();
                if (potion.isBeneficial()){
                    // 计算新持续时间（原时间 + 1秒）
                    int newDuration = effect.getDuration() + 30;
                    player.removePotionEffect(potion);
                    player.addPotionEffect(new PotionEffect(
                            potion,
                            newDuration,
                            effect.getAmplifier(),
                            effect.getIsAmbient(),
                            effect.doesShowParticles()
                    ));
                }
            });
        }
    }

}
