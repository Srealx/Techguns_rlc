package techguns.joint;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author srealx
 * @date 2025/4/11
 */
public interface IModifyInformationData extends IModsJoint{
    /**
     * 编辑information
     */
    Float modify(ItemStack stack, World worldIn, Float data, String informationName);
}
