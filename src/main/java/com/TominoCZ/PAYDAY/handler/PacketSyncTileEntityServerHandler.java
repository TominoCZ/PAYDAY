package com.TominoCZ.PAYDAY.handler;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.block.LobbyTileEntity;
import com.TominoCZ.PAYDAY.packet.PacketSyncTileEntityClient;
import com.TominoCZ.PAYDAY.packet.PacketSyncTileEntityServer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class PacketSyncTileEntityServerHandler implements IMessageHandler<PacketSyncTileEntityServer, IMessage> {
	@Override
	public IMessage onMessage(final PacketSyncTileEntityServer packet, final MessageContext ctx) {
		// THIS IS ON SERVER
		new Thread() {
			@Override
			public void run() {
				EntityPlayerMP p = ctx.getServerHandler().playerEntity;
				World w = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers[packet.dimensionID];
				LobbyTileEntity te = (LobbyTileEntity) w.getTileEntity(packet.x, packet.y, packet.z);

				if (te != null) {
					te.players = packet.players;
					te.lobbyHost = packet.lobbyHost;
					te.isGameLive = packet.isGameLive;
					te.check();
				}

				w = p.worldObj;
				te = (LobbyTileEntity) w.getTileEntity(packet.x, packet.y, packet.z);
				te.check();
				PAYDAY.INSTANCE.sendToAll(new PacketSyncTileEntityClient(te));

				// PAYDAY.INSTANCE.sendToServer(new
				// LobbyPlayerOpenedGuiPacket(packet.x, packet.y,
				// packet.z));//new PacketSyncTileEntityClient(te));
			}
		}.start();

		return null;
	}
}