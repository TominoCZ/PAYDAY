package com.TominoCZ.PAYDAY.packet;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class PacketSyncPlayerPropertiesClient implements IMessage {
	public PacketSyncPlayerPropertiesClient() {

	}

	public boolean inLobby;
	public boolean ready;

	public UUID playerProfileUUID;

	public PacketSyncPlayerPropertiesClient(UUID playerProfileUUID, boolean ready, boolean inLobby) {
		this.inLobby = inLobby;
		this.ready = ready;
		this.playerProfileUUID = playerProfileUUID;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(inLobby);
		buf.writeBoolean(ready);

		buf.writeLong(playerProfileUUID.getMostSignificantBits());
		buf.writeLong(playerProfileUUID.getLeastSignificantBits());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		inLobby = buf.readBoolean();
		ready = buf.readBoolean();

		playerProfileUUID = new UUID(buf.readLong(), buf.readLong());
	}
}