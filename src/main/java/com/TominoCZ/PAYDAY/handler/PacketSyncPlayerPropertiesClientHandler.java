package com.TominoCZ.PAYDAY.handler;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.PlayerData;
import com.TominoCZ.PAYDAY.packet.PacketSyncPlayerPropertiesClient;
import com.TominoCZ.PAYDAY.util.UUIDUtil;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;

public class PacketSyncPlayerPropertiesClientHandler implements IMessageHandler<PacketSyncPlayerPropertiesClient, IMessage> {
	@Override
	public IMessage onMessage(final PacketSyncPlayerPropertiesClient packet, final MessageContext ctx) {
		// CLIENT

		new Thread() {
			@Override
			public void run() {
				PlayerData data = PlayerData.get(UUIDUtil.getPlayerFromProfileUUID(Minecraft.getMinecraft().theWorld, packet.playerProfileUUID));
				data.setInLobby(packet.inLobby);
				data.setReady(packet.ready);
			}
		}.start();

		return null;
	}
}