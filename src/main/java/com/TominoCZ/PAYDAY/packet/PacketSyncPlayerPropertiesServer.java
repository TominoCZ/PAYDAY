package com.TominoCZ.PAYDAY.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketSyncPlayerPropertiesServer implements IMessage {
	public PacketSyncPlayerPropertiesServer() {

	}

	public boolean inLobby;
	public boolean ready;

	public PacketSyncPlayerPropertiesServer(boolean ready, boolean inLobby) {
		this.inLobby = inLobby;
		this.ready = ready;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(inLobby);
		buf.writeBoolean(ready);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		inLobby = buf.readBoolean();
		ready = buf.readBoolean();
	}
}