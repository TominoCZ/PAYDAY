package com.TominoCZ.PAYDAY.handler;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.PlayerData;
import com.TominoCZ.PAYDAY.packet.PacketSyncPlayerPropertiesClient;
import com.TominoCZ.PAYDAY.packet.PacketSyncPlayerPropertiesServer;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketSyncPlayerPropertiesServerHandler implements IMessageHandler<PacketSyncPlayerPropertiesServer, IMessage> {
	@Override
	public IMessage onMessage(final PacketSyncPlayerPropertiesServer packet, final MessageContext ctx) {
		// SERVER
		new Thread() {
			@Override
			public void run() {
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				
				PlayerData data = PlayerData.get(player);
				data.setInLobby(packet.inLobby);
				data.setReady(packet.ready);
				
				PAYDAY.INSTANCE.sendToAll(new PacketSyncPlayerPropertiesClient(player.getGameProfile().getId(), packet.ready, packet.inLobby));
			}
		}.start();

		return null;
	}
}