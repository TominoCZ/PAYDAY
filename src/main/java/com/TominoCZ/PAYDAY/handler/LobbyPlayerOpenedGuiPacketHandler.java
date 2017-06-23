package com.TominoCZ.PAYDAY.handler;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.block.LobbyTileEntity;
import com.TominoCZ.PAYDAY.packet.LobbyPlayerOpenedGuiPacket;
import com.TominoCZ.PAYDAY.packet.PacketSyncTileEntityClient;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class LobbyPlayerOpenedGuiPacketHandler implements IMessageHandler<LobbyPlayerOpenedGuiPacket, IMessage> {
	@Override
	public IMessage onMessage(LobbyPlayerOpenedGuiPacket packet, MessageContext ctx) {
		// SERVER
		int x = packet.x;
		int y = packet.y;
		int z = packet.z;

		EntityPlayerMP p = ctx.getServerHandler().playerEntity;

		World w = p.worldObj;
		if (w == null)
			return null;

		LobbyTileEntity tile = (LobbyTileEntity) w.getTileEntity(x, y, z);

		if (tile == null)
			return null;
		
		PAYDAY.INSTANCE.sendTo(new PacketSyncTileEntityClient(tile), p);

		return null;
	}
}