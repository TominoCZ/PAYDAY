package com.TominoCZ.PAYDAY.block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.TominoCZ.PAYDAY.PAYDAY;
import com.TominoCZ.PAYDAY.PlayerData;
import com.TominoCZ.PAYDAY.packet.PacketSyncTileEntityServer;
import com.TominoCZ.PAYDAY.util.UUIDUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LobbyTileEntity extends TileEntity {
	public List<UUID> players = new ArrayList();

	public boolean isGameLive = false;

	public UUID lobbyHost;

	public LobbyTileEntity() {
		
	}

	public LobbyTileEntity(World w) {
		this.worldObj = w;
		check();
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList list = new NBTTagList();

		for (int i = 0; i < players.size(); i++)
			list.appendTag(createUUIDTag(players.get(i)));

		compound.setTag("playerList", list);
		compound.setBoolean("isGameLive", isGameLive);
		worldObj.notifyBlockChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		NBTTagList list = compound.getTagList("playerList", 10);
		isGameLive = compound.getBoolean("isGameLive");

		players.clear();
		UUID uuid;

		for (int i = 0; i < list.tagCount(); i++) {
			uuid = getUUIDFromTag(list.getCompoundTagAt(i));

			if (worldObj != null && worldObj.playerEntities != null) {
				for (EntityPlayer player : (List<EntityPlayer>) worldObj.playerEntities) {
					if (player.getGameProfile().getId() == uuid) {
						players.add(uuid);
						break;
					}
				}
			}
		}
	}

	public static NBTTagCompound createUUIDTag(UUID uuid) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setLong("M", uuid.getMostSignificantBits());
		nbttagcompound.setLong("L", uuid.getLeastSignificantBits());
		return nbttagcompound;
	}

	public static UUID getUUIDFromTag(NBTTagCompound tag) {
		return new UUID(tag.getLong("M"), tag.getLong("L"));
	}

	public void addPlayer(EntityPlayer player) {
		if (!players.contains(player.getGameProfile().getId()) && players.size() < 4
				&& !PlayerData.get(player).isInLobby()) {

			PlayerData data = PlayerData.get(player);

			data.setInLobby(true);
			data.setReady(false);
			// data.setLobbyHash(hashCode());

			data.sendToServer();

			players.add(player.getGameProfile().getId());

			if (lobbyHost == null)
				lobbyHost = players.get(0);

			check();

			markDirty();

			syncTileEntity();
		}
	}

	public void removePlayer(EntityPlayer player) {
		if (players.contains(player.getGameProfile().getId())) {
			players.remove(player.getGameProfile().getId());

			if (player.getGameProfile().getId().equals(lobbyHost))
				lobbyHost = null;

			PlayerData data = PlayerData.get(player);

			data.setInLobby(false);
			data.setReady(false);
			// data.setLobbyHash(hashCode());

			data.sendToServer();

			check();

			markDirty();

			syncTileEntity();
		}
	}

	public UUID getLobbyHost() {
		return lobbyHost;
	}

	public void setLobbyHost(UUID player) {
		lobbyHost = player;

		syncTileEntity();
	}

	public boolean isHost(EntityPlayer player) {
		return lobbyHost == null ? false : lobbyHost.equals(player.getGameProfile().getId());
	}

	public void check() {
		List<UUID> toRemove = new ArrayList();

		EntityPlayer plr;
		PlayerData data;

		for (UUID id : players) {
			if ((plr = UUIDUtil.getPlayerFromProfileUUID(worldObj, id)) == null
					|| !(worldObj.getBlock(xCoord, yCoord, zCoord) instanceof LobbyBlock)
					|| !worldObj.getTileEntity(xCoord, yCoord, zCoord).equals(this)) {

				if (plr != null) {
					data = PlayerData.get(plr);
					data.setInLobby(false);
					data.setReady(false);
				}

				toRemove.add(id);
			}
		}

		players.removeAll(toRemove);

		if (toRemove.contains(lobbyHost) || !players.contains(lobbyHost)) {
			plr = UUIDUtil.getPlayerFromProfileUUID(getWorldObj(), lobbyHost);

			if (plr != null) {
				data = PlayerData.get(plr);
				data.setInLobby(false);
				data.setReady(false);
			}
			lobbyHost = null;
		}

		if (lobbyHost == null && players.size() > 0)
			lobbyHost = players.get(0);
		
		if (worldObj != null)
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, this.blockMetadata = players.size(), 2);
	}

	public boolean isGameLive() {
		return isGameLive;
	}

	public void setGameLive(boolean live) {
		isGameLive = live;

		syncTileEntity();
	}

	public boolean allReady() {
		PlayerData data;
		for (UUID uuid : players) {
			if (lobbyHost != null && !uuid.equals(lobbyHost)) {
				data = PlayerData.get(UUIDUtil.getPlayerFromProfileUUID(worldObj, uuid));

				if (!data.isReady())
					return false;
			}
		}

		return true;
	}

	public void syncTileEntity() {
		PAYDAY.INSTANCE.sendToServer(new PacketSyncTileEntityServer(this));
	}
}
