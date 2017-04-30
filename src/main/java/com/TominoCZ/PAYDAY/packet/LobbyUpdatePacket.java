package com.TominoCZ.PAYDAY.packet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class LobbyUpdatePacket implements IMessage {
	public LobbyUpdatePacket() {
	}

	public int x, y, z;

	public List<EntityPlayer> players = new ArrayList();

	public LobbyUpdatePacket(int x, int y, int z, List<EntityPlayer> list) {
		this.players = list;

		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void toBytes(ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);

		buf.writeInt(players.size());

		for (EntityPlayer player : players) {
			buf.writeLong(player.getUniqueID().getMostSignificantBits());
			buf.writeLong(player.getUniqueID().getLeastSignificantBits());
		}
	}

	public void fromBytes(ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();

		int count = buf.readInt();

		World world = Minecraft.getMinecraft().theWorld;

		if (world == null)
			return;

		UUID uuid;

		for (int i = 0; i < count; i++) {
			uuid = new UUID(buf.readLong(), buf.readLong());

			for (EntityPlayer player : (List<EntityPlayer>) world.playerEntities)
				if (player.getUniqueID().getMostSignificantBits() == uuid.getMostSignificantBits()
						&& player.getUniqueID().getLeastSignificantBits() == uuid.getLeastSignificantBits()) {
					players.add(player);
					break;
				}
		}
	}
}