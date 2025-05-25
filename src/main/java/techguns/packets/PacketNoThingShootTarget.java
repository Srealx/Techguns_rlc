package techguns.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Tells the server that the player who sent this message wants to shoot with his current gun
 *
 */
public class PacketNoThingShootTarget implements IMessage {
	public long gunFireCooling;
	public ItemStack itemStack;

	@Override
	public void fromBytes(ByteBuf buf) {
		this.gunFireCooling = buf.readLong();
		itemStack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(gunFireCooling);
		ByteBufUtils.writeItemStack(buf,itemStack);
	}

	public PacketNoThingShootTarget(){

	}

	public PacketNoThingShootTarget(long gunFireCooling,ItemStack itemStack) {
		this.gunFireCooling = gunFireCooling;
		this.itemStack = itemStack;
	}

	public static class Handler implements IMessageHandler<PacketNoThingShootTarget, IMessage> {
		@Override
		public IMessage onMessage(PacketNoThingShootTarget message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketNoThingShootTarget message, MessageContext ctx) {
			NBTTagCompound tagCompound = message.itemStack.getTagCompound();
			tagCompound.setLong("gunFireCooling",message.gunFireCooling);
		}
	}
}
