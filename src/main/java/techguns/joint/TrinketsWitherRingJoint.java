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

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author srealx
 * @date 2025/4/27
 */
public class TrinketsWitherRingJoint implements ILivingHurtProgress {
    private static TrinketsWitherRingJoint trinketsWitherRingJoint;

    private TrinketsWitherRingJoint(){}

    public static TrinketsWitherRingJoint obtain(){
        if (trinketsWitherRingJoint==null){
            trinketsWitherRingJoint = new TrinketsWitherRingJoint();
        }
        return trinketsWitherRingJoint;
    }

    private static Map<String,Long> healCoolDataCache = new HashMap<>(4);

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
            if (!handler.getStackInSlot(a).isEmpty() && handler.getStackInSlot(a).getItem().getRegistryName().toString().endsWith("wither_ring")){
                // 检查目标是否凋零
                PotionEffect wither = entity.getActivePotionEffect(MobEffects.WITHER);
                // 检查冷却时间
                Long coolTime = healCoolDataCache.get(player.getUniqueID().toString()+ entity.getUniqueID());
                if (wither != null && (coolTime ==null || coolTime - System.currentTimeMillis() <= 0)){
                    // 回复生命值
                    player.heal(2f);
                    // 冷却时间  单个目标 0.35秒
                    healCoolDataCache.put(player.getUniqueID().toString()+ entity.getUniqueID(),System.currentTimeMillis() + 350);
                }
                // 计算几率给目标施加凋零效果 2秒
                int ran = new Random().nextInt(100)+1;
                int baseRate = 5;
                int extraRate = (int)( (damageSource.getOriginalDamage()-10) <= 0 ? 0:(damageSource.getOriginalDamage()-10f) / 10f * 3f);
                if ((baseRate+extraRate) >= ran){
                    entity.addPotionEffect(new PotionEffect(MobEffects.WITHER, 40, 0, false, true));
                }
                break;
            }
        }
    }

    @Override
    public String modName() {
        return "Trinkets and Baubles";
    }
}
