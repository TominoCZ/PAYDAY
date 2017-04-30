package com.TominoCZ.PAYDAY.packet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class LobbyDestroyPacket implements IMessage {
	public LobbyDestroyPacket() {
	}

	public int x, y, z;

	private int playerCount;

	public List<UUID> players = new ArrayList();

	public LobbyDestroyPacket(int x, int y, int z, List<UUID> list) {
		this.players = list;
		this.playerCount = list.size();

		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		buf.writeInt(playerCount);

		for (UUID i : players) {
			buf.writeLong(i.getMostSignificantBits());
			buf.writeLong(i.getLeastSignificantBits());
		}
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		playerCount = buf.readInt();

		for (int i = 0; i < playerCount; i++)
			players.add(new UUID(buf.readLong(), buf.readLong()));
	}
}