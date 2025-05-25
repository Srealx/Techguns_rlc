package techguns.joint;

import net.minecraft.entity.Entity;

/**
 * @author Srealx
 * @date 2025/4/1
 */
public interface ICollisionEntityFindProgress extends IModsJoint {
    /**
     *
     * @param entity
     */
    Entity progress(Entity entity);
}
