package techguns.joint;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import techguns.bean.QualityDataBean;
import techguns.constant.QualityConstant;
import techguns.damagesystem.TGDamageSource;
import techguns.entities.projectiles.GenericProjectile;
import techguns.util.ItemUtil;

/**
 * @author srealx
 * @date 2025/4/1
 */
public class ToolsQualityJoint implements IDamageSourceInitProgress,IModifyInformationData,IProjectileCreateProgress ,IExtraAttributeProgress{
    private static ToolsQualityJoint toolsQualityJoint;

    private ToolsQualityJoint(){}

    public static ToolsQualityJoint obtain(){
        if (toolsQualityJoint==null){
            toolsQualityJoint = new ToolsQualityJoint();
        }
        return toolsQualityJoint;
    }


    @Override
    public void progress(TGDamageSource damageSource) {
        // 武器品质加成
        QualityDataBean qualityData = ItemUtil.findItemStackQualityData(damageSource.getSourceItemStack());
        if (qualityData!=null){
            Float extraDamage = qualityData.findAttributeAmount(QualityConstant.PROJECTILE_DAMAGE_NAME);
            if (extraDamage!=null){
                damageSource.setOriginalDamage(damageSource.getOriginalDamage() * (1+extraDamage));
            }
            Float extraPenetrate = qualityData.findAttributeAmount(QualityConstant.PENETRATE_NAME);
            if (extraPenetrate!=null){
                damageSource.setPenetration(damageSource.getPenetration()+extraPenetrate);
            }
            Float extraCriticalRate = qualityData.findAttributeAmount(QualityConstant.CRITICAL_HIT_RATE_NAME);
            if (extraCriticalRate!=null){
                damageSource.setExtraCriticalHitRate(damageSource.getExtraCriticalHitRate() + extraCriticalRate);
            }
        }
    }

    @Override
    public float progress(float speed, ItemStack stack, EntityLivingBase livingBase) {
        QualityDataBean qualityData = ItemUtil.findItemStackQualityData(stack);
        if (qualityData!=null){
            Float extraSpeed = qualityData.findAttributeAmount(QualityConstant.SPEED_NAME);
            if (extraSpeed==null){
                return speed;
            }
            float speedRate = 1 + extraSpeed;
            if (speedRate <= 0){
                speedRate=0.5f;
            }
            speed *= speedRate;
        }
        return speed;
    }



    @Override
    public String modName() {
        return "ToolsQuality";
    }

    @Override
    public Float modify(ItemStack stack, World worldIn, Float data, String informationName) {
        QualityDataBean qualityData = ItemUtil.findItemStackQualityData(stack);
        if (qualityData == null){
            return data;
        }
        Float attributeAmount = null;
        if (informationName.equals("techguns.gun.tooltip.damage")){
            attributeAmount = qualityData.findAttributeAmount(QualityConstant.PROJECTILE_DAMAGE_NAME);
            if (attributeAmount!=null){
                // 最低减少50%
                if (attributeAmount<-1.0f){
                    attributeAmount = -0.5f;
                }
                return data * (1 + attributeAmount);
            }
            return data;
        }
        if (informationName.equals("techguns.gun.tooltip.velocity")){
            attributeAmount = qualityData.findAttributeAmount(QualityConstant.SPEED_NAME);
            if (attributeAmount!=null){
                return data * (1 + attributeAmount);
            }
            return data;
        }
        if (informationName.equals("techguns.gun.tooltip.extraCriticalHitRate")){
            attributeAmount = qualityData.findAttributeAmount(QualityConstant.CRITICAL_HIT_RATE_NAME);
        }
        if (informationName.equals("techguns.gun.tooltip.armorPen")){
            attributeAmount = qualityData.findAttributeAmount(QualityConstant.PENETRATE_NAME);
        }
        if (attributeAmount!=null){
            data+=attributeAmount;
        }
        return data;
    }

    @Override
    public float progress(World worldIn, ItemStack itemStack, String attributeName) {
        QualityDataBean qualityData = ItemUtil.findItemStackQualityData(itemStack);
        if (qualityData == null){
            return 0;
        }
        if (attributeName.equals("CriticalRate")){
           Float extra = qualityData.findAttributeAmount(QualityConstant.CRITICAL_HIT_RATE_NAME);
           if (extra==null){
               return 0;
           }else {
               return extra;
           }
        }
        return 0;
    }
}
