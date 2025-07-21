package techguns.damagesystem.damageHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techguns.damagesystem.TGDamageSource;
import techguns.joint.ILivingHurtProgress;
import techguns.joint.ModsJointRegistrar;

import java.util.List;

/**
 * @Author  Srealx
 * @Date 2025/5/25
 */
public class OtherModJointDamageHandler implements IChainedDamageSystemHandler {
    private static OtherModJointDamageHandler otherModJointDamageHandler;
    private OtherModJointDamageHandler(){}

    public static OtherModJointDamageHandler obtain(){
        if (otherModJointDamageHandler == null){
            otherModJointDamageHandler = new OtherModJointDamageHandler();
        }
        return otherModJointDamageHandler;
    }

    @Override
    public IChainedDamageSystemHandler getNextNode() {
        return ExtraDamageCalculationHandler.obtain();
    }

    @Override
    public float handler(LivingHurtEvent event,TGDamageSource source,EntityLivingBase shooter, int gunLevel, float damage) {
        List<ILivingHurtProgress> jointList = ModsJointRegistrar.getJointList(ILivingHurtProgress.class);
        jointList.forEach(item->item.progress(event));
        // 如果是魔法伤害，则无法暴击
        if (Boolean.TRUE.equals(source.isMagicDamage())){
            source.setCriticalHitFlat(Boolean.FALSE);
        }
        return source.getOriginalDamage();
    }
}
