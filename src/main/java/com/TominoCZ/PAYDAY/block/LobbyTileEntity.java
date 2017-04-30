package com.TominoCZ.PAYDAY.block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class LobbyTileEntity extends TileEntity {
	public List<UUID> players = new ArrayList();

	public LobbyTileEntity() {

	}

	public LobbyTileEntity(World w) {
		this.worldObj = w;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagList list = new NBTTagList();

		for (int i = 0; i < players.size(); i++)
			list.appendTag(createUUIDTag(players.get(i)));

		compound.setTag("playerList", list);

		worldObj.notifyBlockChange(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		NBTTagList list = compound.getTagList("playerList", 10);

		players.clear();
		UUID uuid;

		for (int i = 0; i < list.tagCount(); i++) {
			uuid = getUUIDFromTag(list.getCompoundTagAt(i));

			if (worldObj != null && worldObj.playerEntities != null) {
				for (EntityPlayer player : (List<EntityPlayer>) worldObj.playerEntities) {
					if (player.getUniqueID() == uuid) {
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
		if (!players.contains(player.getUniqueID()) && players.size() < 4
				&& !player.getEntityData().getBoolean("inPAYDAYLobby")) {
			players.add(player.getUniqueID());

			check();

			markDirty();
		}
	}

	public void removePlayer(EntityPlayer player) {
		if (players.contains(player.getUniqueID())) {

			players.remove(player.getUniqueID());

			check();

			markDirty();
		}
	}

	public void check() {
		List<UUID> toRemove = new ArrayList();

		for (UUID id : players) {
			boolean found = false;

			for (EntityPlayer p : (List<EntityPlayer>) worldObj.playerEntities) {
				if (p.getUniqueID().equals(id))
					found = true;
			}

			if (!found)
				toRemove.add(id);
		}

		players.removeAll(toRemove);

		if (worldObj != null)
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, this.blockMetadata = players.size(), 2);
	}

	public List<EntityPlayer> getPlayers() {
		List<EntityPlayer> l = new ArrayList();

		for (EntityPlayer player : (List<EntityPlayer>) worldObj.playerEntities) {
			for (UUID uuid : players) {
				if (uuid.equals(player.getUniqueID())) {
					l.add(player);
					break;
				}
			}
		}
		
		return l;
	}
}
