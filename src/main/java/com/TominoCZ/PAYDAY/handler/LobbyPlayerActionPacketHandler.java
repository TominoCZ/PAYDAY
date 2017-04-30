package com.TominoCZ.PAYDAY.handler;

import java.util.List;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.block.LobbyTileEntity;
import com.TominoCZ.PAYDAY.packet.LobbyPlayerActionPacket;
import com.TominoCZ.PAYDAY.packet.LobbyPlayerOpenedGuiPacket;
import com.TominoCZ.PAYDAY.packet.LobbyUpdatePacket;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class LobbyPlayerActionPacketHandler implements IMessageHandler<LobbyPlayerActionPacket, IMessage> {
	@Override
	public IMessage onMessage(LobbyPlayerActionPacket packet, MessageContext ctx) {
		// SERVER
		int x = packet.x;
		int y = packet.y;
		int z = packet.z;

		EntityPlayer notToSendTo = ctx.getServerHandler().playerEntity;

		World w = notToSendTo.worldObj;
		if (w == null)
			return null;

		LobbyTileEntity tile = (LobbyTileEntity) w.getTileEntity(x, y, z);

		if (tile == null)
			return null;

		if (notToSendTo != null) {
			if (packet.leaving)
				tile.removePlayer(notToSendTo);
			else
				tile.addPlayer(notToSendTo);
		}
		
		 PAYDAY.INSTANCE.sendToServer(new LobbyPlayerOpenedGuiPacket(x, y, z));

		//for (EntityPlayer player : (List<EntityPlayer>) w.playerEntities) {
			
			//if (!player.getUniqueID().equals(notToSendTo.getUniqueID()))
				//PAYDAY.INSTANCE.sendTo(new LobbyUpdatePacket(x, y, z, tile.getPlayers()), (EntityPlayerMP)player);
		//}

		return null;
	}
}