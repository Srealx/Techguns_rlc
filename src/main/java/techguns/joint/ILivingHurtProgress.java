package techguns.joint;

import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * @author Srealx
 * @date 2025/4/1
 */
public interface ILivingHurtProgress extends IModsJoint {
    /**
     * progress after tg damage source init
     * @param event
     */
    void progress(LivingHurtEvent event);
}
