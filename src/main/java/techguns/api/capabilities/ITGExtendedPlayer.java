package techguns.api.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Should only be used on EntityPlayer
 * 
 */
public interface ITGExtendedPlayer extends ITGShooterValues {

	public EntityPlayer getEntity();
	
	public int getFireDelay(EnumHand hand);
	public void setFireDelay(EnumHand hand, int delay);

	long getFireNextTimeOfGun(ItemStack itemStack);
	void setFireNextTimeOfGun(ItemStack itemStack,long delay);
	long gunCanShootResidualTime(World world, ItemStack itemStack);

	public IInventory getTGInventory();
	
	public void saveToNBT(final NBTTagCompound tags);
	public void loadFromNBT(final NBTTagCompound tags);
}
