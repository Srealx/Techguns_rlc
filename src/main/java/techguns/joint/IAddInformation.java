package techguns.joint;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author srealx
 * @date 2025/4/1
 */
public interface IAddInformation extends IModsJoint{
    /**
     * add information
     */
    void add(ItemStack stack, World worldIn, List<String> list, ITooltipFlag flagIn);
}
