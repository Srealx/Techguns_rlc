package techguns.damagesystem.damageHandler;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techguns.damagesystem.TGDamageSource;

/**
 * @Author Srealx
 * @Date 2025/5/24
 */
public interface IDamageSystemHandler{
    float handler(LivingHurtEvent event, TGDamageSource source, EntityLivingBase shooter, int gunLevel, float damage);

}
