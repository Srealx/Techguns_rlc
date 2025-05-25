package techguns.joint;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import techguns.entities.projectiles.GenericProjectile;
import techguns.items.guns.GenericGun;

/**
 * @author srealx
 * @date 2025/4/22
 */
public interface IProjectileCreateProgress extends IModsJoint{
    /**
     * progress after tg damage source init
     * @param speed
     */
    float progress(float speed, ItemStack stack, EntityLivingBase livingBase);
}
