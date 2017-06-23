package com.TominoCZ.PAYDAY.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class LobbyBeginGamePacket implements IMessage {
	public LobbyBeginGamePacket() {
	}

	public int x, y, z;

	public LobbyBeginGamePacket(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}
}