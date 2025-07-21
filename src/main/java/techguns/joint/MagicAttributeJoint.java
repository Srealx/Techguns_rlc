package techguns.joint;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import techguns.bean.QualityDataBean;
import techguns.constant.QualityConstant;
import techguns.damagesystem.TGDamageSource;
import techguns.util.ItemUtil;

/**
 * @author srealx
 * @date 2025/4/3
 */
public class MagicAttributeJoint implements IDamageSourceInitProgress{
    private static MagicAttributeJoint magicAttributeJoint;

    private MagicAttributeJoint(){}

    public static MagicAttributeJoint obtain(){
        if (magicAttributeJoint==null){
            magicAttributeJoint = new MagicAttributeJoint();
        }
        return magicAttributeJoint;
    }

    @Override
    public void progress(TGDamageSource damageSource) {
        if (damageSource.attacker instanceof EntityPlayer && damageSource.isMagicDamage()){
            EntityPlayer player = (EntityPlayer)damageSource.attacker;
            // 检查饰品栏的魔法伤害增伤
            IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
            if (handler == null){
                return;
            }
            for(int a = 0; a < handler.getSlots(); ++a) {
                if (!handler.getStackInSlot(a).isEmpty()){
                    ItemStack stackInSlot = handler.getStackInSlot(a);
                    QualityDataBean qualityData = ItemUtil.findItemStackQualityData(stackInSlot);
                    if (qualityData == null){
                        continue;
                    }
                    Float attributeAmount = qualityData.findAttributeAmount(QualityConstant.MAGIC_DAMAGE_NAME);
                    if (attributeAmount!=null){
                        damageSource.addAdditionalDamageRate(attributeAmount);
                    }
                }
            }
        }
    }

    @Override
    public String modName() {
        return "PowerPotion";
    }
}
