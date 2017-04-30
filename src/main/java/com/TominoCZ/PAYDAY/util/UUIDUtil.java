package com.TominoCZ.PAYDAY.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class UUIDUtil {
	public static List<EntityPlayer> getPlayersFromUUIDs(World w, List<UUID> uuids) {
		List<EntityPlayer> list = new ArrayList();

		for (UUID uuid : uuids) {
			for (EntityPlayer p : (List<EntityPlayer>) w.playerEntities) {
				if (p.getUniqueID().equals(uuid)){
					list.add(p);
					break;
				}
			}
		}

		return list;
	}

	public static EntityPlayer getPlayerFromUUID(World w, UUID uuid) {
		for (EntityPlayer p : (List<EntityPlayer>) w.playerEntities) {
			if (p.getUniqueID().equals(uuid))
				return p;
		}

		return null;
	}
}
