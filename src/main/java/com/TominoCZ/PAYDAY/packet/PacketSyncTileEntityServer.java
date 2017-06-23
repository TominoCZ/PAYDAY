package com.TominoCZ.PAYDAY.packet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.TominoCZ.PAYDAY.block.LobbyTileEntity;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketSyncTileEntityServer implements IMessage {
	public PacketSyncTileEntityServer() {

	}

	public int x, y, z;

	public List<UUID> players = new ArrayList();

	public UUID lobbyHost;

	public boolean isGameLive;

	public int dimensionID;

	public PacketSyncTileEntityServer(LobbyTileEntity te) {
		this.players = te.players;
		this.lobbyHost = te.getLobbyHost();

		this.dimensionID = te.getWorldObj().provider.dimensionId;

		this.isGameLive = te.isGameLive();

		this.x = te.xCoord;
		this.y = te.yCoord;
		this.z = te.zCoord;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);

		buf.writeInt(dimensionID);

		buf.writeInt(players.size());

		buf.writeBoolean(isGameLive);

		for (UUID player : players) {
			buf.writeLong(player.getMostSignificantBits());
			buf.writeLong(player.getLeastSignificantBits());
		}

		buf.writeBoolean(lobbyHost != null);

		if (lobbyHost != null) {
			buf.writeLong(lobbyHost.getMostSignificantBits());
			buf.writeLong(lobbyHost.getLeastSignificantBits());
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();

		dimensionID = buf.readInt();

		int count = buf.readInt();

		isGameLive = buf.readBoolean();

		for (int i = 0; i < count; i++)
			players.add(new UUID(buf.readLong(), buf.readLong()));

		if (buf.readBoolean())
			lobbyHost = new UUID(buf.readLong(), buf.readLong());
	}
}