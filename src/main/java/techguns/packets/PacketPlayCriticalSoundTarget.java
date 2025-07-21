package techguns.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import techguns.TGPackets;
import techguns.TGSounds;
import techguns.Techguns;
import techguns.client.audio.TGSoundCategory;
import techguns.util.SoundUtil;

/**
 * Tells the server that the player who sent this message wants to shoot with his current gun
 *
 */
public class PacketPlayCriticalSoundTarget implements IMessage {

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public PacketPlayCriticalSoundTarget(){

	}

	public static class Handler implements IMessageHandler<PacketPlayCriticalSoundTarget, IMessage> {
		@Override
		public IMessage onMessage(PacketPlayCriticalSoundTarget message, MessageContext ctx) {
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
			return null;
		}

		private void handle(PacketPlayCriticalSoundTarget message, MessageContext ctx) {
			EntityPlayer ply = TGPackets.getPlayerFromContext(ctx);
//			SoundUtil.playSoundOnEntityGunPosition(ply.world, ply, TGSounds.CRITICAL_STRIKE, 1.0f, 1.0F, false, false, TGSoundCategory.GUN_FIRE);
			Techguns.proxy.playSoundOnEntity(ply, TGSounds.CRITICAL_STRIKE, 1.0f, 1.0f, false, false,true, true, TGSoundCategory.GUN_FIRE);
		}
	}
}
