package com.TominoCZ.PAYDAY.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class UUIDUtil {
	@Deprecated
	public static List<EntityPlayer> getPlayersFromUUIDs(World w, List<UUID> uuids) {
		List<EntityPlayer> list = new ArrayList();

		for (UUID uuid : uuids) {
			for (EntityPlayer p : (List<EntityPlayer>) w.playerEntities) {
				if (p.getUniqueID().equals(uuid)) {
					list.add(p);
					break;
				}
			}
		}

		return list;
	}

	@Deprecated
	public static EntityPlayer getPlayerFromUUID(World w, UUID uuid) {
		for (EntityPlayer p : (List<EntityPlayer>) w.playerEntities) {
			if (p.getUniqueID().equals(uuid))
				return p;
		}

		return null;
	}

	public static EntityPlayer getPlayerFromProfileUUID(World worldObj, UUID playerProfileID) {
		for (EntityPlayer p : (List<EntityPlayer>) worldObj.playerEntities) {
			if (p.getGameProfile().getId().equals(playerProfileID))
				return p;
		}

		return null;
	}

	public static List<EntityPlayer> getPlayersFromProfileUUIDs(World w, List<UUID> uuids) {
		List<EntityPlayer> list = new ArrayList();

		for (UUID uuid : uuids) {
			EntityPlayer p = UUIDUtil.getPlayerFromProfileUUID(w, uuid);

			if (w.playerEntities.contains(p))
				list.add(p);
		}

		return list;
	}
}
