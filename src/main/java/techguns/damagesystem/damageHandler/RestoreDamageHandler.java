package techguns.damagesystem.damageHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techguns.damagesystem.TGDamageSource;
import techguns.joint.IOriginalRestoreProgress;
import techguns.joint.ModsJointRegistrar;

import java.util.List;

/**
 * @Author  Srealx
 * @Date 2025/5/25
 */
public class RestoreDamageHandler implements IChainedDamageSystemHandler {
    private static RestoreDamageHandler restoreDamageHandler;
    private RestoreDamageHandler(){}

    public static RestoreDamageHandler obtain(){
        if (restoreDamageHandler == null){
            restoreDamageHandler = new RestoreDamageHandler();
        }
        return restoreDamageHandler;
    }

    @Override
    public boolean isStartNode() {
        return Boolean.TRUE;
    }

    @Override
    public IChainedDamageSystemHandler getNextNode() {
        return OtherModJointDamageHandler.obtain();
    }

    @Override
    public float handler(LivingHurtEvent event,TGDamageSource source,EntityLivingBase shooter, int gunLevel, float damage) {
        // 还原事件的伤害值，使其尽量不受其他mod的增伤效果影响
        List<IOriginalRestoreProgress> restoreProgressList = ModsJointRegistrar.getJointList(IOriginalRestoreProgress.class);
        for (IOriginalRestoreProgress item : restoreProgressList) {
            event.setAmount(item.progress(shooter,event.getEntityLiving(),event.getAmount()));
        }
        // 对被减伤的情况特殊处理, 如果事件Chain中傷害被降低，則保持同步
        if (event.getAmount()<source.getOriginalDamage()){
            float maxRate;
            switch (gunLevel){
                case 3:maxRate=0.25f;break;
                case 4:maxRate=0.15f;break;
                case 5:maxRate=0.1f;break;
                case 2:maxRate=0.4f;break;
                case 1:
                default:maxRate=0.5f;break;
            }
            if ((source.getOriginalDamage()-event.getAmount())/source.getOriginalDamage() < maxRate){
                maxRate = (source.getOriginalDamage()-event.getAmount()) / source.getOriginalDamage();
            }
            source.setOriginalDamage(source.getOriginalDamage() * (1 - maxRate));
        }
        return source.getOriginalDamage();
    }
}
