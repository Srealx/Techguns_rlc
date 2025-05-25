package techguns.damagesystem.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import techguns.damagesystem.TGDamageSource;

/**
 * @Author Srealx
 * @Date 2025/5/25
 */
public class GunsCriticalHitEvent extends CriticalHitEvent {

    TGDamageSource damageSource;

    public GunsCriticalHitEvent(TGDamageSource source, EntityPlayer player, Entity target, float damageModifier, boolean vanillaCritical) {
        super(player, target, damageModifier, vanillaCritical);
        damageSource = source;
    }


    public TGDamageSource getDamageSource() {
        return damageSource;
    }
}
