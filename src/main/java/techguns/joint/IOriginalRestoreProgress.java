package techguns.joint;

import net.minecraft.entity.EntityLivingBase;

/**
 * @author srealx
 * @date 2025/4/1
 */
public interface IOriginalRestoreProgress extends IModsJoint {
    /**
     * @param shooter
     * @param entity
     * @param amount
     * @return
     */
    float progress(EntityLivingBase shooter, EntityLivingBase entity, float amount);
}
