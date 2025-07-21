package techguns.joint;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;
import techguns.damagesystem.TGDamageSource;

import java.util.Collection;

/**
 * @author srealx
 * @date 2025/4/3
 */
public class PlayerAttributeJoint implements IDamageSourceInitProgress,IProjectileCreateProgress{
    private static PlayerAttributeJoint playerAttributeJoint;

    private PlayerAttributeJoint(){}

    public static PlayerAttributeJoint obtain(){
        if (playerAttributeJoint==null){
            playerAttributeJoint = new PlayerAttributeJoint();
        }
        return playerAttributeJoint;
    }


    @Override
    public void progress(TGDamageSource damageSource) {
        if (damageSource.attacker instanceof EntityLivingBase){
            EntityLivingBase entity = (EntityLivingBase) damageSource.attacker;
            // 检查玩家身上的攻击加成
            IAttributeInstance entityAttribute = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
            if (entityAttribute==null){
                return;
            }
            Collection<AttributeModifier> modifiers = entityAttribute.getModifiers();
            modifiers.forEach(item->{
                int operation = item.getOperation();
                switch (operation){
                    // 类型0表示添加固值伤害
                    case 0:{
                        // 固伤: 每1点加7%
                        damageSource.addAdditionalDamageRate(0.07f *  (float)item.getAmount());
                    }break;
                    // 类型1表示基础伤害百分比加成
                    case 1:{
                        // 收益 50%
                        damageSource.addAdditionalDamageRate((float) item.getAmount() * 0.5f);
                    }break;
                    // 类型2表示总伤害百分比加成
                    case 2:{
                        // 收益 75%
                        damageSource.addAdditionalDamageRate((float) item.getAmount() * 0.75f);
                    }break;
                    default:break;
                }
            });
        }
    }

    @Override
    public String modName() {
        return "PlayerAttribute";
    }

    @Override
    public float progress(float speed, ItemStack stack, EntityLivingBase livingBase) {
        if (livingBase == null){
            return speed;
        }
        // 检查玩家身上的攻速加成
        IAttributeInstance entityAttribute = livingBase.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED);
        if (entityAttribute==null){
            return speed;
        }
        Collection<AttributeModifier> modifiers = entityAttribute.getModifiers();
        for (AttributeModifier item : modifiers) {
            int operation = item.getOperation();
            float amount = (float) item.getAmount();
            float extraAdd = 0;
            switch (operation){
                case 0:{
                    return speed + amount;
                }
                case 1:
                case 2:
                default:{
                    // 默认全部加成一半
                    extraAdd = amount;
                }
            }
            if (extraAdd<0){
                continue;
            }
            return speed * (1+extraAdd);
        }
        return speed;
    }
}
