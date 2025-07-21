package techguns.damagesystem.damageHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techguns.damagesystem.TGDamageSource;

/**
 * @Author  Srealx
 * @Date 2025/5/25
 */
public interface IChainedDamageSystemHandler extends IDamageSystemHandler,IChainHandler<IChainedDamageSystemHandler>{

    default float execute(LivingHurtEvent event, EntityLivingBase shooter, int gunLevel){
        float damage = this.handler(event, (TGDamageSource)event.getSource() ,shooter, gunLevel, ((TGDamageSource)event.getSource()).getOriginalDamage());
        IChainedDamageSystemHandler nextNode = this.getNextNode();
        if (nextNode!=null){
            return nextNode.execute(event,shooter,gunLevel,damage);
        }
        return damage;
    }

    default float execute(LivingHurtEvent event, EntityLivingBase shooter, int gunLevel, float damage){
        damage = this.handler(event, (TGDamageSource)event.getSource() ,shooter, gunLevel,damage);
        IChainedDamageSystemHandler nextNode = this.getNextNode();
        if (nextNode!=null){
            return nextNode.execute(event,shooter,gunLevel,damage);
        }
        return damage;
    }

}
