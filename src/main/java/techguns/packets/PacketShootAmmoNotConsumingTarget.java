package techguns.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import techguns.items.guns.GenericGun;

/**
 * Tells the server that the player who sent this message wants to shoot with his current gun
 *
 */
public class PacketShootAmmoNotConsumingTarget implements IMessage {
	public int ammoCount;
	public ItemStack itemStack;

	@Override
	public void fromBytes(ByteBuf buf) {
		this.ammoCount = buf.readInt();
		itemStack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(ammoCount);
		ByteBufUtils.writeItemStack(buf,itemStack);
	}

	public PacketShootAmmoNotConsumingTarget(){

	}


	public int getAmmoCount(){
		return ammoCount;
	}

	public PacketShootAmmoNotConsumingTarget(int ammoCount, ItemStack itemStack) {
		this.ammoCount = ammoCount;
		this.itemStack = itemStack;
	}

	public static class Handler implements IMessageHandler<PacketShootAmmoNotConsumingTarget, IMessage> {
		@Override
		public IMessage onMessage(PacketShootAmmoNotConsumingTarget message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketShootAmmoNotConsumingTarget message, MessageContext ctx) {
			if(message.itemStack.getItem() instanceof GenericGun){
				NBTTagCompound tags = message.itemStack.getTagCompound();
				if (tags!=null){
					tags.setShort("ammo",(short) message.getAmmoCount());
				}
			}
		}
	}
}
