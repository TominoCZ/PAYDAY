package com.TominoCZ.PAYDAY.handler;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.block.LobbyTileEntity;
import com.TominoCZ.PAYDAY.packet.PacketSyncTileEntityClient;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class PacketSyncTileEntityClientHandler implements IMessageHandler<PacketSyncTileEntityClient, IMessage> {
	@Override
	public IMessage onMessage(final PacketSyncTileEntityClient packet, final MessageContext ctx) {
		// THIS IS ON CLIENT
		new Thread() {
			@Override
			public void run() {
				World w = Minecraft.getMinecraft().theWorld;
				LobbyTileEntity te = (LobbyTileEntity) w.getTileEntity(packet.x, packet.y, packet.z);
				
				if (te != null) {
					te.players = packet.players;
					te.lobbyHost = packet.lobbyHost;
					te.isGameLive = packet.isGameLive;
					te.check();
				}
				
				int x = packet.x;
				int y = packet.y;
				int z = packet.z;
			}
		}.start();

		return null;
	}
}