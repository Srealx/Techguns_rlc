package techguns.joint;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author srealx
 * @date 2025/4/1
 */
public interface IExtraAttributeProgress extends IModsJoint {
    /**
     * progress after tg damage source init
     * @param itemStack
     * @param attributeName
     */
    float progress(World worldIn, ItemStack itemStack, String attributeName);
}
