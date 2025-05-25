package techguns.joint;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import techguns.damagesystem.TGDamageSource;

/**
 * @author srealx
 * @date 2025/4/23
 */
public class SinWrathEffectJoint implements IDamageSourceInitProgress{
    private static SinWrathEffectJoint sinWrathEffectJoint;

    private SinWrathEffectJoint(){}

    public static SinWrathEffectJoint obtain(){
        if (sinWrathEffectJoint==null){
            sinWrathEffectJoint = new SinWrathEffectJoint();
        }
        return sinWrathEffectJoint;
    }

    /**
     * 由于罪恶buff直接提供了属性，所以不需要注册
     * @param damageSource
     */
    @Override
    public void progress(TGDamageSource damageSource) {
        if (!(damageSource.attacker instanceof EntityLivingBase)){
            return;
        }
        EntityLivingBase ent = (EntityLivingBase)damageSource.attacker;
        // 检查是否有罪恶buff
        for (PotionEffect effect : ent.getActivePotionEffects()) {
            if (effect.getEffectName().equals("bountifulbaubles.effect.sinful")) {
                // Amplifier 表示药水效果的等级，从 0 开始
                int potionLevel = effect.getAmplifier();
                // 每级添加 8%
                float addRate = potionLevel * 0.08f;
                // 可受暴击增幅
                damageSource.addAdditionalDamageRate(addRate);
                break;
            }
        }
    }

    @Override
    public String modName() {
        return "bountifulBaubles";
    }
}
