package techguns.joint;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import techguns.damagesystem.TGDamageSource;

/**
 * @author srealx
 * @date 2025/4/27
 */
public class TrinketsPoisonStoneJoint implements ILivingHurtProgress ,IOriginalRestoreProgress{
    private static TrinketsPoisonStoneJoint trinketsPoisonStoneJoint;

    private TrinketsPoisonStoneJoint(){}

    public static TrinketsPoisonStoneJoint obtain(){
        if (trinketsPoisonStoneJoint==null){
            trinketsPoisonStoneJoint = new TrinketsPoisonStoneJoint();
        }
        return trinketsPoisonStoneJoint;
    }

    @Override
    public void progress(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (!(trueSource instanceof EntityPlayer) || !(event.getSource() instanceof TGDamageSource)){
            return;
        }
        TGDamageSource damageSource =  (TGDamageSource) event.getSource();
        EntityPlayer player = (EntityPlayer) trueSource;
        EntityLivingBase entity =  event.getEntityLiving();

        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        for(int a = 0; a < handler.getSlots(); ++a) {
            if (!handler.getStackInSlot(a).isEmpty() && handler.getStackInSlot(a).getItem().getRegistryName().toString().endsWith("poison_stone")) {
                // 检查目标是否中毒
                PotionEffect poison = entity.getActivePotionEffect(MobEffects.POISON);
                if (poison != null){
                    damageSource.addAdditionalDamageRate(0.25f);
                }
            }
        }
    }

    @Override
    public float progress(EntityLivingBase shooter, EntityLivingBase entity, float amount) {
        if (!(shooter instanceof EntityPlayer)){
            return amount;
        }
        EntityPlayer player = (EntityPlayer) shooter;
        IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
        for(int a = 0; a < handler.getSlots(); ++a) {
            if (!handler.getStackInSlot(a).isEmpty() && handler.getStackInSlot(a).getItem().getRegistryName().toString().endsWith("poison_stone")) {
                // 检查目标是否中毒
                PotionEffect poison = entity.getActivePotionEffect(MobEffects.POISON);
                if (poison != null){
                    // 还原伤害 除以 1.4f
                    return amount / 1.4f;
                }
            }
        }
        return amount;
    }

    @Override
    public String modName() {
        return "Trinkets and Baubles";
    }
}
