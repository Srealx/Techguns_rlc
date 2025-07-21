package techguns.damagesystem.damageHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techguns.damagesystem.TGDamageSource;
import techguns.damagesystem.event.GunsCriticalHitEvent;

/**
 * @Author Srealx
 * @Date 2025/5/25
 */
public class ExtraDamageCalculationHandler implements IChainedDamageSystemHandler {
    private static ExtraDamageCalculationHandler extraDamageCalculationHandler;
    private ExtraDamageCalculationHandler(){}

    public static ExtraDamageCalculationHandler obtain(){
        if (extraDamageCalculationHandler == null){
            extraDamageCalculationHandler = new ExtraDamageCalculationHandler();
        }
        return extraDamageCalculationHandler;
    }

    @Override
    public IChainedDamageSystemHandler getNextNode() {
        return ArmorReductionDamageHandler.obtain();
    }

    @Override
    public float handler(LivingHurtEvent event,TGDamageSource source,EntityLivingBase shooter, int gunLevel, float damage) {
        // 先计算基础伤害加成
        damage = damage * (1 + source.getAdditionalDamageRate());
        // 防止负数
        if (damage<0){
            damage = 1f;
        }
        // 处理暴击
        if (Boolean.TRUE.equals(source.getCriticalHitFlat())){
            damage *= (1 + source.getExtraCriticalHitDamageRate());
            // 触发一个暴击事件
            if (shooter instanceof EntityPlayer){
                GunsCriticalHitEvent criticalHitEvent = new GunsCriticalHitEvent(source,(EntityPlayer)shooter,event.getEntity(),source.getExtraCriticalHitDamageRate(),false);
                MinecraftForge.EVENT_BUS.post(criticalHitEvent);
            }

        }
        return damage;
    }
}
