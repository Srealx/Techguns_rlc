package techguns.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import techguns.items.guns.GenericGun;

import java.lang.reflect.Field;

/**
 * Tells the server that the player who sent this message wants to shoot with his current gun
 *
 */
public class PacketGunsLossDurableTarget implements IMessage {
	public Integer nextDurableDamage;
	public ItemStack itemStack;

	@Override
	public void fromBytes(ByteBuf buf) {
		this.nextDurableDamage = buf.readInt();
		itemStack = ByteBufUtils.readItemStack(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(nextDurableDamage);
		ByteBufUtils.writeItemStack(buf,itemStack);
	}

	public PacketGunsLossDurableTarget(){

	}

	public PacketGunsLossDurableTarget(Integer nextDurableDamage, ItemStack itemStack) {
		this.nextDurableDamage = nextDurableDamage;
		this.itemStack = itemStack;
	}

	public static class Handler implements IMessageHandler<PacketGunsLossDurableTarget, IMessage> {
		@Override
		public IMessage onMessage(PacketGunsLossDurableTarget message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketGunsLossDurableTarget message, MessageContext ctx) {
			if(message.itemStack.getItem() instanceof GenericGun){
				ItemStack itemStack =  message.itemStack;
				Item item = itemStack.getItem();
				if (item instanceof GenericGun){
					((GenericGun) item).setItemDamage(itemStack,message.nextDurableDamage);
				}
			}
		}
	}
}
