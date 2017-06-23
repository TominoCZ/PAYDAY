package com.TominoCZ.PAYDAY.handler;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.block.LobbyTileEntity;
import com.TominoCZ.PAYDAY.packet.LobbyBeginGamePacket;
import com.TominoCZ.PAYDAY.util.UUIDUtil;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LobbyBeginGamePacketHandler implements IMessageHandler<LobbyBeginGamePacket, IMessage> {
	@Override
	public IMessage onMessage(LobbyBeginGamePacket packet, MessageContext ctx) {
		int x = packet.x;
		int y = packet.y;
		int z = packet.z;

		World w = ctx.getServerHandler().playerEntity.worldObj;

		TileEntity te = w.getTileEntity(x, y, z);

		if (te != null && te instanceof LobbyTileEntity) {
			LobbyTileEntity tile = (LobbyTileEntity) te;

			tile.setGameLive(true);

			for (EntityPlayer p : UUIDUtil.getPlayersFromProfileUUIDs(w, tile.players))
				p.travelToDimension(PAYDAY.dimensionID);
		}

		return null;
	}
}
