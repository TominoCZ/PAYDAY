package com.TominoCZ.PAYDAY.handler;

import java.util.List;
import java.util.UUID;

import com.TominoCZ.PAYDAY.packet.LobbyDestroyPacket;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LobbyDestroyPacketHandler implements IMessageHandler<LobbyDestroyPacket, IMessage> {
	@Override
	public IMessage onMessage(LobbyDestroyPacket packet, MessageContext ctx) {
		// SERVER
		int x = packet.x;
		int y = packet.y;
		int z = packet.z;

		World w = ctx.getServerHandler().playerEntity.worldObj;

		if (w == null)
			return null;

		// LobbyTileEntity tile = (LobbyTileEntity) w.getTileEntity(x, y, z);

		for (UUID uuid : packet.players) {
			for (EntityPlayer p : (List<EntityPlayer>) w.playerEntities) {
				if (uuid.equals(p.getUniqueID())) {
					p.getEntityData().setBoolean("inPAYDAYLobby", false);
					p.getEntityData().setInteger("PAYDAYLobbyX", 0);
					p.getEntityData().setInteger("PAYDAYLobbyY", -1);
					p.getEntityData().setInteger("PAYDAYLobbyZ", 0);
					break;
				}
			}
		}

		w.removeTileEntity(x, y, z);

		return null;
	}
}