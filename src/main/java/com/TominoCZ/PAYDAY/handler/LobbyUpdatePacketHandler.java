package com.TominoCZ.PAYDAY.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.block.LobbyTileEntity;
import com.TominoCZ.PAYDAY.packet.LobbyPlayerOpenedGuiPacket;
import com.TominoCZ.PAYDAY.packet.LobbyUpdatePacket;
import com.google.common.collect.Lists;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class LobbyUpdatePacketHandler implements IMessageHandler<LobbyUpdatePacket, IMessage> {
	@Override
	public IMessage onMessage(LobbyUpdatePacket packet, MessageContext ctx) {
		// CLIENT
		int x = packet.x;
		int y = packet.y;
		int z = packet.z;

		if (Minecraft.getMinecraft().theWorld == null)
			return null;

		LobbyTileEntity tile = (LobbyTileEntity) Minecraft.getMinecraft().theWorld.getTileEntity(x, y, z);

		List<UUID> uuids = new ArrayList();

		for (EntityPlayer player : packet.players)
			uuids.add(player.getUniqueID());

		if (tile != null) {
			tile.players = uuids;
			tile.markDirty();
		}

		return null;
	}
}