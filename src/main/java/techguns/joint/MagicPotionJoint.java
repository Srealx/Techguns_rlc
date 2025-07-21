package techguns.joint;

import com.tmtravlr.potioncore.potion.PotionMagicFocus;
import com.tmtravlr.potioncore.potion.PotionMagicInhibition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import techguns.damagesystem.TGDamageSource;

/**
 * @author srealx
 * @date 2025/4/3
 */
public class MagicPotionJoint implements IDamageSourceInitProgress{
    private static MagicPotionJoint magicPotionJoint;

    private MagicPotionJoint(){}

    public static MagicPotionJoint obtain(){
        if (magicPotionJoint==null){
            magicPotionJoint = new MagicPotionJoint();
        }
        return magicPotionJoint;
    }

    @Override
    public void progress(TGDamageSource damageSource) {
        if (damageSource.attacker instanceof EntityLivingBase && damageSource.isMagicDamage()){
            EntityLivingBase ent = (EntityLivingBase)damageSource.attacker;
            // 获取玩家身上的所有药水效果
            PotionEffect activeFocusEffect = ent.getActivePotionEffect(PotionMagicFocus.INSTANCE);
            if (activeFocusEffect!=null){
                // 每级加0.5额外伤害比例
                damageSource.addAdditionalDamageRate( (activeFocusEffect.getAmplifier()+1) * 0.5f );
            }
            PotionEffect activeInhibitionEffect = ent.getActivePotionEffect(PotionMagicInhibition.INSTANCE);
            if (activeFocusEffect!=null){
                // 每级减少 0.2 伤害比例
                damageSource.addAdditionalDamageRate( - ((activeFocusEffect.getAmplifier()+1) * 0.2f));
            }
        }
    }

    @Override
    public String modName() {
        return "PowerPotion";
    }
}
