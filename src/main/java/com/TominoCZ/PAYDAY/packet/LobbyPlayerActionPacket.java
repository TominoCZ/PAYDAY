package com.TominoCZ.PAYDAY.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class LobbyPlayerActionPacket implements IMessage {
	public LobbyPlayerActionPacket() {
	}

	public int x, y, z;

	public boolean leaving;

	public LobbyPlayerActionPacket(int x, int y, int z, boolean leaving) {
		this.leaving = leaving;

		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);

		buf.writeBoolean(leaving);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();

		leaving = buf.readBoolean();
	}
}