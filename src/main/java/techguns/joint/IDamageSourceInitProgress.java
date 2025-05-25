package techguns.joint;

import techguns.damagesystem.TGDamageSource;

/**
 * @author srealx
 * @date 2025/4/1
 */
public interface IDamageSourceInitProgress extends IModsJoint {
    /**
     * progress after tg damage source init
     * @param damageSource
     */
    void progress(TGDamageSource damageSource);
}
