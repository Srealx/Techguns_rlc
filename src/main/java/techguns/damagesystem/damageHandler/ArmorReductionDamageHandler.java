package techguns.damagesystem.damageHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techguns.damagesystem.TGDamageSource;

/**
 * @Author Srealx
 * @Date 2025/5/25
 */
public class ArmorReductionDamageHandler implements IChainedDamageSystemHandler {
    private static ArmorReductionDamageHandler armorReductionDamageHandler;
    private ArmorReductionDamageHandler(){}

    public static ArmorReductionDamageHandler obtain(){
        if (armorReductionDamageHandler == null){
            armorReductionDamageHandler = new ArmorReductionDamageHandler();
        }
        return armorReductionDamageHandler;
    }

    @Override
    public IChainedDamageSystemHandler getNextNode() {
        return null;
    }

    @Override
    public float handler(LivingHurtEvent event,TGDamageSource source,EntityLivingBase shooter, int gunLevel, float damage) {
        // Armor damage reduction algorithm for firearms (zh_cn.枪械的专属护甲减伤算法)
        float penetration = source.getPenetration();
        if (penetration<0){
            penetration = 0;
        }
        EntityLivingBase mob = event.getEntityLiving();
        // Each level of firearm has an armor threshold, and if the target's armor exceeds
        // this value, the target will be protected from damage by 5% for every 50% exceeded. (zh_cn.每个等级的枪械有一个护甲阈值， 如果目标的护甲超过该值，则每超过50%，目标免伤5%)
        float maxArmor;
        float levelPressingCoefficient = 0;
        switch (gunLevel){
            case 3:maxArmor = 10f;break;
            case 4:maxArmor = 20f;break;
            case 5:maxArmor = 25f;break;
            default:maxArmor = 5;
        }
        // Damage is calculated based on the armor-piercing factor (zh_cn.根据穿甲系数计算伤害)
        float mobArmor = (float) mob.getTotalArmorValue();
        float actualDamage = damage;
        if (mobArmor>0){
            if (mobArmor>maxArmor){
                // If the target entity's armor value exceeds the armor threshold, 50% + 5% damage reduction for each excess (zh_cn.如果目标实体的护甲值超过了护甲阈值, 则每超50% + 5%的伤害减免
                float exceedRatio  = (mobArmor-maxArmor)/(maxArmor/2);
                levelPressingCoefficient += exceedRatio * 0.05;
                switch (gunLevel){
                    case 3:{
                        if (levelPressingCoefficient>0.2f){
                            levelPressingCoefficient = 0.2f;
                        }
                    }break;
                    case 4: {
                        if (levelPressingCoefficient>0.15f){
                            levelPressingCoefficient = 0.15f;
                        }
                    }break;
                    case 5: {
                        if (levelPressingCoefficient>0.1f){
                            levelPressingCoefficient = 0.1f;
                        }
                    }break;
                    default:{
                        if (levelPressingCoefficient>0.25f){
                            levelPressingCoefficient = 0.25f;
                        }
                    }
                }
            }
            //  Monsters have up to 20 armor and a maximum of 94% damage reduction, and the reduction per point
            //  of armor is calculated based on the maximum armor value that can be recognized   (zh_cn.怪物最多生效20护甲,减免伤害最多百分之94, 每点护甲的减少值基于可识别的最大护甲值计算
            if (mobArmor > 20f){
                mobArmor = 20f;
            }
            if (penetration>0){
                mobArmor = mobArmor * (1-penetration);
            }
            float v = 1 - (mobArmor * 0.047f) - levelPressingCoefficient;
            // Deal at least 5% of effective damage (zh_cn.最少造成 5% 有效伤害
            if (v<0.05f){
                v = 0.05f;
            }
            actualDamage = actualDamage * v;
        }
        return actualDamage;
    }
}
