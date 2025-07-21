package techguns.joint;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import techguns.damagesystem.TGDamageSource;

/**
 * @author srealx
 * @date 2025/4/3
 */
public class PowerPotionJoint implements IDamageSourceInitProgress{
    private static PowerPotionJoint powerPotionJoint;

    private PowerPotionJoint(){}

    public static PowerPotionJoint obtain(){
        if (powerPotionJoint==null){
            powerPotionJoint = new PowerPotionJoint();
        }
        return powerPotionJoint;
    }

    @Override
    public void progress(TGDamageSource damageSource) {
        if (damageSource.attacker instanceof EntityLivingBase){
            EntityLivingBase ent = (EntityLivingBase)damageSource.attacker;
            // 携带力量药水效果
            // 获取玩家身上的所有药水效果
            PotionEffect activePotionEffect = ent.getActivePotionEffect(Potion.getPotionById(5));
            if (activePotionEffect!=null){
                int potionLevel = activePotionEffect.getAmplifier() + 1;
                // 每级添加 6.5% 额外基础伤害
                float addRate = potionLevel * 0.065f;
                // 可受暴击增幅
                damageSource.setOriginalDamage(damageSource.getOriginalDamage() * (1+addRate));
            }
        }
    }

    @Override
    public String modName() {
        return "PowerPotion";
    }
}
